-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.2.14-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 ippse-mblog 的数据库结构
CREATE DATABASE IF NOT EXISTS `ippse-mblog` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ippse-mblog`;

-- 导出  表 ippse-mblog.post 结构
CREATE TABLE IF NOT EXISTS `post` (
  `id` varchar(32) NOT NULL,
  `altitude` double DEFAULT NULL,
  `comments_count` int(11) DEFAULT NULL,
  `content` longtext NOT NULL,
  `course` double DEFAULT NULL,
  `ctime` datetime(6) NOT NULL,
  `emoji` varchar(255) DEFAULT NULL,
  `floor` double DEFAULT NULL,
  `forward` varchar(32) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `personal` varchar(32) DEFAULT NULL,
  `photos` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pid` varchar(32) DEFAULT NULL,
  `pubtime` datetime(6) DEFAULT NULL,
  `refurl` longtext DEFAULT NULL,
  `reply` varchar(32) DEFAULT NULL,
  `repost_count` int(11) DEFAULT NULL,
  `speed` double DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `userid` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 ippse-mblog.postlike 结构
CREATE TABLE IF NOT EXISTS `postlike` (
  `id` varchar(32) NOT NULL,
  `postid` varchar(32) DEFAULT NULL,
  `userid` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
