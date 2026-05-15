package com.fynxt.stock_trading_engine.entity;

import com.fynxt.stock_trading_engine.enums.OrderSide;
import com.fynxt.stock_trading_engine.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Trader placing the order
    @Column(nullable = false)
    private String traderId;

    // Stock name like AAPL
    @Column(nullable = false)
    private String stock;

    // Sector like TECH
    @Column(nullable = false)
    private String sector;

    // Quantity of shares
    @Column(nullable = false)
    private Integer quantity;

    // BUY or SELL
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide side;

    // PENDING/FILLED/CANCELLED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
}
