-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 18, 2025 at 11:21 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `bookTitle` varchar(100) NOT NULL,
  `author` varchar(100) NOT NULL,
  `bookType` varchar(100) NOT NULL,
  `image` varchar(500) NOT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`bookTitle`, `author`, `bookType`, `image`, `date`) VALUES
('C# cho Người Mới', 'Pham Minh D', 'Non-fiction', 'C:\\Users\\Admin\\Downloads\\backup\\LibraryManagementSystem-main\\libraryManagement\\src\\image\\hehe.jpg', '2022-07-18'),
('Học Python Cơ Bản', 'Tran Thi B', 'Khoa Học', 'C:\\Users\\Admin\\Downloads\\backup\\LibraryManagementSystem-main\\libraryManagement\\src\\image\\python_tutorial.jpg', '2019-06-10');

-- --------------------------------------------------------

--
-- Table structure for table `save`
--

CREATE TABLE `save` (
  `studentNumber` varchar(100) NOT NULL,
  `bookTitle` varchar(100) NOT NULL,
  `author` varchar(100) NOT NULL,
  `bookType` varchar(100) NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `save`
--

INSERT INTO `save` (`studentNumber`, `bookTitle`, `author`, `bookType`, `image`, `date`) VALUES
('2023001', 'Học Python Cơ Bản', 'Tran Thi B', 'Khoa Học', 'C:\\Users\\Admin\\Downloads\\backup\\LibraryManagementSystem-main\\libraryManagement\\src\\image\\python_tutorial.jpg', '2019-06-10');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `studentNumber` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `image` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`studentNumber`, `password`, `image`) VALUES
('2023001', 'pass123', 'C:\\Users\\Admin\\Pictures\\339752985_1926635384368216_7199864786069073937_n.jpg'),
('2023002', 'pass456', 'src/image/logo1.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `take`
--

CREATE TABLE `take` (
  `studentNumber` varchar(100) NOT NULL,
  `bookTitle` varchar(100) NOT NULL,
  `firstName` varchar(100) NOT NULL,
  `lastName` varchar(100) NOT NULL,
  `gender` varchar(100) NOT NULL,
  `author` varchar(100) NOT NULL,
  `bookType` varchar(100) NOT NULL,
  `image` varchar(500) NOT NULL,
  `date` date NOT NULL,
  `checkReturn` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `take`
--

INSERT INTO `take` (`studentNumber`, `bookTitle`, `firstName`, `lastName`, `gender`, `author`, `bookType`, `image`, `date`, `checkReturn`) VALUES
('2023001', 'Học Python Cơ Bản', 'Nguyen', 'Van An', 'Male', 'Tran Thi B', 'Non-fiction', 'C:\\Users\\Admin\\Downloads\\backup\\LibraryManagementSystem-main\\libraryManagement\\src\\image\\python_tutorial.jpg', '2025-05-12', 'Returned');
COMMIT;
--
-- Indexes for dumped tables
--


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
