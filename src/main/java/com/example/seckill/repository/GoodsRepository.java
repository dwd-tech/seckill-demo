package com.example.seckill.repository;

import com.example.seckill.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    // SQL 级并发安全扣减库存
    @Modifying
    @Query("UPDATE Goods g SET g.stock = g.stock - 1 WHERE g.id = :id AND g.stock > 0")
    int decreaseStock(@Param("id") Long id);
}