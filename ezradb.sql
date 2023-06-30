-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 30, 2023 at 10:25 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ezradb`
--

-- --------------------------------------------------------

--
-- Table structure for table `loans`
--

CREATE TABLE `loans` (
  `id` int(11) NOT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `status` enum('ACTIVE','REPAID','SWEPT') DEFAULT NULL,
  `subscriber_id` int(11) DEFAULT NULL,
  `due_date` datetime(6) DEFAULT NULL,
  `loan_period` int(11) DEFAULT NULL,
  `is_approved` bit(1) DEFAULT NULL,
  `is_declined` bit(1) DEFAULT NULL,
  `is_overdue` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `loans`
--

INSERT INTO `loans` (`id`, `amount`, `created_at`, `status`, `subscriber_id`, `due_date`, `loan_period`, `is_approved`, `is_declined`, `is_overdue`) VALUES
(1, 30000.00, '2023-06-30 19:23:46', 'ACTIVE', 1, '2023-09-30 22:23:46.000000', NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `loan_period`
--

CREATE TABLE `loan_period` (
  `id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `period` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `loan_period`
--

INSERT INTO `loan_period` (`id`, `description`, `period`) VALUES
(1, '3months', 3);

-- --------------------------------------------------------

--
-- Table structure for table `loan_repayments`
--

CREATE TABLE `loan_repayments` (
  `id` int(11) NOT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `loan_id` int(11) DEFAULT NULL,
  `payment_date` datetime(6) DEFAULT NULL,
  `interest` decimal(38,2) DEFAULT NULL,
  `penalty` decimal(38,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `subscribers`
--

CREATE TABLE `subscribers` (
  `id` int(11) NOT NULL,
  `age` int(11) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `msisdn` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `subscribers`
--

INSERT INTO `subscribers` (`id`, `age`, `first_name`, `last_name`, `msisdn`) VALUES
(1, 25, 'Joseph', 'Kithome', '0717064174');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `loans`
--
ALTER TABLE `loans`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKf76mw3ieovgc5g05o460ttf71` (`subscriber_id`);

--
-- Indexes for table `loan_period`
--
ALTER TABLE `loan_period`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `loan_repayments`
--
ALTER TABLE `loan_repayments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmvfjvk48bhsvwbdis0s9uwn1t` (`loan_id`);

--
-- Indexes for table `subscribers`
--
ALTER TABLE `subscribers`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `loans`
--
ALTER TABLE `loans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `loan_period`
--
ALTER TABLE `loan_period`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `loan_repayments`
--
ALTER TABLE `loan_repayments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `subscribers`
--
ALTER TABLE `subscribers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `loans`
--
ALTER TABLE `loans`
  ADD CONSTRAINT `FKf76mw3ieovgc5g05o460ttf71` FOREIGN KEY (`subscriber_id`) REFERENCES `subscribers` (`id`);

--
-- Constraints for table `loan_repayments`
--
ALTER TABLE `loan_repayments`
  ADD CONSTRAINT `FKmvfjvk48bhsvwbdis0s9uwn1t` FOREIGN KEY (`loan_id`) REFERENCES `loans` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
