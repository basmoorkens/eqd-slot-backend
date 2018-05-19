-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 13, 2018 at 07:07 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: 'eqdslots'
--

-- --------------------------------------------------------

--
-- Table structure for table 'incomming_player_wallet_transaction'
--

CREATE TABLE IF NOT EXISTS incomming_player_wallet_transaction (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount double NOT NULL,
  created_date_time datetime DEFAULT NULL,
  processed_date_time datetime DEFAULT NULL,
  processing_date_time datetime DEFAULT NULL,
  public_key varchar(255) DEFAULT NULL,
  transaction_status varchar(255) DEFAULT NULL,
  version int(11) NOT NULL,
  paging_token varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table 'outgoing_player_wallet_transaction'
--

CREATE TABLE IF NOT EXISTS outgoing_player_wallet_transaction (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount double NOT NULL,
  created_date_time datetime DEFAULT NULL,
  processed_date_time datetime DEFAULT NULL,
  processing_date_time datetime DEFAULT NULL,
  public_key varchar(255) DEFAULT NULL,
  transaction_status varchar(255) DEFAULT NULL,
  version int(11) NOT NULL,
  blockchain_hash varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table 'player_wallet'
--

CREATE TABLE IF NOT EXISTS player_wallet (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  balance double NOT NULL,
  public_key varchar(255) DEFAULT NULL,
  version int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table 'stateful_configuration'
--

CREATE TABLE IF NOT EXISTS stateful_configuration (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  last_paging_token varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  version int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table 'stateful_configuration'
--

INSERT INTO stateful_configuration (id, last_paging_token, `name`, version) VALUES
(1, NULL, 'production', 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
