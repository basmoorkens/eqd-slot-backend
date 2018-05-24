package com.basm.slots.service;

import com.basm.slots.config.SlotsProperties;
import com.basm.slots.model.IncomingPlayerWalletStellarTransaction;
import com.basm.slots.model.PlayerWalletStellarTransaction;
import com.basm.slots.model.StatefulConfiguration;
import com.basm.slots.repository.OutgoingPlayerWalletTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stellar.sdk.*;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.SubmitTransactionResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service("stellarService")
public class StellarService {

    private final Logger log = LoggerFactory.getLogger(StellarService.class);

    @Autowired
    private SlotsProperties slotsProperties;

    @Autowired
    private OutgoingPlayerWalletTransactionRepository playerWalletTransactionRepository;

    private Server stellarServer = new Server("https://horizon.stellar.org");

    private KeyPair escrowKeypair;

    private KeyPair tokenIssuer;

    @PostConstruct
    public void initialize() {
        escrowKeypair = KeyPair.fromSecretSeed(slotsProperties.getEscrowWalletPrivateKey());
        tokenIssuer =  KeyPair.fromAccountId(slotsProperties.getSlotTokenIssuer());
        Network.usePublicNetwork();

    }

    /**
     * Fetches the available amount in the game engine wallet.
     * @return
     * @throws IOException
     */
    public double getAvailableAmountInSlotsWallet() throws IOException {
        AccountResponse escrowWallet = getEscrowWallet();
        for(AccountResponse.Balance balance : escrowWallet.getBalances()) {
            if(slotsProperties.getSlotToken().equals(balance.getAssetCode())) {
                return Double.parseDouble(balance.getBalance());
            }
        }
        throw new RuntimeException("No balance found in escrow wallet for " + slotsProperties.getSlotToken());
    }

    /**
     * Process an outstanding transaction to payout funds to a slots player.
     *
     * @param openTx        The transaction to process
     * @return              The generated transaction hash on the blockchain
     * @throws IOException  When an exception occurs trying to write to the stellar network.
     */
    public String processOpenPaymentTransaction(final PlayerWalletStellarTransaction openTx) throws IOException {
        AccountResponse escrowWallet = getEscrowWallet();
        KeyPair destination = buildAndCheckDestinationWallet(openTx.getPublicKey());
        Transaction transaction = new Transaction.Builder(escrowWallet)
                .addOperation(
                        new PaymentOperation.Builder(
                                destination, new AssetTypeCreditAlphaNum4(slotsProperties.getSlotToken(),tokenIssuer), openTx.getAmount()+"")
                                .build())
                .addMemo(Memo.text("EQD slots winnings: " + openTx.getAmount()))
                .build();
        transaction.sign(escrowKeypair);

        try {
            SubmitTransactionResponse transactionResponse = stellarServer.submitTransaction(transaction);
            log.debug("Rate limit remaining: " +transactionResponse.getRateLimitRemaining() + " Rate limit reset:" + transactionResponse.getRateLimitRemaining());
            if(!transactionResponse.isSuccess()) {
                log.error("Failed to submit wallet transaction [" + openTx.getId() + "] to horizon.");
                throw new RuntimeException("Error creating transaction");
            }
            log.info("Generated transaction hash " + transactionResponse.getHash() + " for TRANSACTION [" + openTx.getId() +"]");
            return transactionResponse.getHash();
        } catch ( Exception e){
            throw new RuntimeException("Error creating transaction");
        }
    }

    public List<IncomingPlayerWalletStellarTransaction> scanForUnprocessedIncomingPayments(StatefulConfiguration statefulConfiguration) throws IOException {
        List<IncomingPlayerWalletStellarTransaction> playerWalletTransactions = new ArrayList<>();
        int txScanCounter = 0;
        for(int i = 0; i < 5; i++) {//run 5 times coz paging is broken on stellar side...
            PaymentsRequestBuilder paymentsRequestBuilder = stellarServer.payments().forAccount(escrowKeypair);
            paymentsRequestBuilder.cursor(statefulConfiguration.getLastPagingToken());
            Page<OperationResponse> responsePage = paymentsRequestBuilder.execute();
            log.debug("Remaining rate limit: " + responsePage.getRateLimitRemaining() + " Rate limit reset: " + responsePage.getRateLimitReset());
            if(responsePage == null || responsePage.getRecords().size()==0) {
               break;
            }
            txScanCounter += processPage(playerWalletTransactions, responsePage);
            String lastPagingToken = responsePage.getRecords().get(responsePage.getRecords().size()-1).getPagingToken();
            statefulConfiguration.setLastPagingToken(lastPagingToken);
        }
        log.info("Scanned " + txScanCounter +" transactions on HORIZON.");
        log.debug("Found new lastpagingtoken " + statefulConfiguration.getLastPagingToken());
        return playerWalletTransactions;

    }

    private int processPage(List<IncomingPlayerWalletStellarTransaction> playerWalletTransactions, Page<OperationResponse> responsePage) {
        int txCounter = 0;
        for(OperationResponse response : responsePage.getRecords()) {
            txCounter++;
            if(isIncomingTransaction(response)) {
                PaymentOperationResponse payment = (PaymentOperationResponse) response;
                if(isTokenFromApplication(payment.getAsset())) {
                    IncomingPlayerWalletStellarTransaction playerWalletTransaction = PlayerWalletStellarTransaction.buildIncoming(Double.parseDouble(payment.getAmount()), payment.getFrom().getAccountId(), payment.getTransactionHash());
                    ((IncomingPlayerWalletStellarTransaction) playerWalletTransaction).setPagingToken(payment.getPagingToken());
                    playerWalletTransactions.add(playerWalletTransaction);
                }
            } else {
                log.debug("Disregarding transaction " + response.getTransactionHash() + " as its an outgoing payment. ");
            }
        }
        return txCounter;
    }

    private boolean isTokenFromApplication(final Asset assetInTx ) {
        return assetInTx.equals(new AssetTypeCreditAlphaNum4(slotsProperties.getSlotToken(), tokenIssuer));
    }

    private boolean isIncomingTransaction(OperationResponse payment) {
        if(payment instanceof PaymentOperationResponse) {
            KeyPair txKeypair = ((PaymentOperationResponse) payment).getTo();
            if(txKeypair.getPublicKey() == null) {
                return false;
            }
            return Arrays.equals(txKeypair.getPublicKey(), escrowKeypair.getPublicKey());
        }
        return false;
    }


    private KeyPair buildAndCheckDestinationWallet(final String publicKey) {
        KeyPair destination = KeyPair.fromAccountId(publicKey);
        try {
            stellarServer.accounts().account(destination);
        } catch (IOException e) {
            throw new RuntimeException(publicKey + " is not a valid stellar wallet.");
        }
        return destination;
    }

    public AccountResponse getEscrowWallet() throws IOException {
        KeyPair escrowKeyPair = escrowKeypair;
        AccountResponse account =  stellarServer.accounts().account(escrowKeyPair);
        log.debug("Rate limit remaining: " + account.getRateLimitRemaining() + " Rate limit reset: " + account.getRateLimitReset());
        return account;
    }

    public void printWallets() throws IOException {
        AccountResponse escrowWallet = getEscrowWallet();
        for(AccountResponse.Balance balance : escrowWallet.getBalances()) {
            System.out.println(String.format(
                    "Type: %s, Code: %s, Balance: %s",
                    balance.getAssetType(),
                    balance.getAssetCode(),
                    balance.getBalance()));
        }
    }
}
