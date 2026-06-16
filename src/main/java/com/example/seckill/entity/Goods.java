package com.example.seckill.entity;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_goods")
@Data
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String goodsName;
    private Integer stock;
    private Integer version;
}