--this sql file will setup the database with all the needed tables and some config settings like the blockchain transaction it has to start scanning.
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

-- --------------------------------------------------------

--
-- Table structure for table 'incoming_player_wallet_transaction'
--

CREATE TABLE IF NOT EXISTS incoming_player_wallet_transaction (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount double NOT NULL,
  blockchain_hash varchar(255) DEFAULT NULL,
  created_date_time datetime DEFAULT NULL,
  processed_date_time datetime DEFAULT NULL,
  processing_date_time datetime DEFAULT NULL,
  public_key varchar(255) DEFAULT NULL,
  status_reason varchar(255) DEFAULT NULL,
  transaction_status varchar(255) DEFAULT NULL,
  version int(11) NOT NULL,
  paging_token varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

-- --------------------------------------------------------

--
-- Table structure for table 'outgoing_player_wallet_transaction'
--

CREATE TABLE IF NOT EXISTS outgoing_player_wallet_transaction (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount double NOT NULL,
  blockchain_hash varchar(255) DEFAULT NULL,
  created_date_time datetime DEFAULT NULL,
  processed_date_time datetime DEFAULT NULL,
  processing_date_time datetime DEFAULT NULL,
  public_key varchar(255) DEFAULT NULL,
  status_reason varchar(255) DEFAULT NULL,
  transaction_status varchar(255) DEFAULT NULL,
  version int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=271 ;

-- --------------------------------------------------------

--
-- Table structure for table 'player_wallet'
--

CREATE TABLE IF NOT EXISTS player_wallet (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  balance double NOT NULL,
  public_key varchar(255) DEFAULT NULL,
  version int(11) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_ppfcvetq8q6unaxjqopslkgw4 (public_key)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

INSERT INTO player_wallet (id, balance, public_key, version) VALUES
(1, 5000, 'GA7PSSZDIGORA7MK2QFNDLU3IYWIXOIPQU3NC7YHRWPSH43FFFZGVLG7', 0);

-- --------------------------------------------------------

--
-- Table structure for table 'stateful_configuration'
--

CREATE TABLE IF NOT EXISTS stateful_configuration (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  last_paging_token varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  version int(11) NOT NULL,
  wallet_balance double NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table 'stateful_configuration'
--

INSERT INTO stateful_configuration (id, last_paging_token, `name`, version, wallet_balance) VALUES
(1, '77141336372879361', 'production', 30, 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
