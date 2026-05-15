package com.fynxt.stock_trading_engine.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "portfolio",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "traderId",
                                "stock"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Trader ID
    @Column(nullable = false)
    private String traderId;

    // Stock name
    @Column(nullable = false)
    private String stock;

    // Sector name
    @Column(nullable = false)
    private String sector;

    // Shares owned
    @Column(nullable = false)
    private Integer quantity;

    // IMPORTANT FOR CONCURRENCY
    // Enables optimistic locking
    @Version
    private Long version;
}
