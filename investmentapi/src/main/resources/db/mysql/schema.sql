CREATE TABLE IF NOT EXISTS `product_investing` (
`product_id` int(11) NOT NULL,
`accumulated_investing_amount` bigint(20) NOT NULL,
`investing_user_count` int(11) NOT NULL,
PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `product_meta` (
`product_id` int(11) NOT NULL,
`title` varchar(45) NOT NULL,
`total_investing_amount` bigint(20) NOT NULL,
`started_at` datetime(4) NOT NULL,
`finished_at` datetime(4) NOT NULL,
PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user_product` (
`user_id` bigint(20) NOT NULL,
`product_id` int(11) NOT NULL,
`investing_amount` bigint(20) NOT NULL,
`investing_at` datetime(4) NOT NULL,
PRIMARY KEY (`user_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;