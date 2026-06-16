CREATE DATABASE IF NOT EXISTS `seckill_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `seckill_db`;

-- 商品表
CREATE TABLE IF NOT EXISTS `tb_goods` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `goods_name` VARCHAR(100) NOT NULL,
    `stock` INT NOT NULL DEFAULT 0,
    `version` INT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE IF NOT EXISTS `tb_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_no` VARCHAR(64) NOT NULL,
    `user_id` BIGINT NOT NULL,
    `goods_id` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入一个基础抢购商品，初始库存 10 个
INSERT INTO `tb_goods` (id, goods_name, stock, version)
VALUES (1, 'iPhone 15 Pro Max', 10, 0)
ON DUPLICATE KEY UPDATE id=id;