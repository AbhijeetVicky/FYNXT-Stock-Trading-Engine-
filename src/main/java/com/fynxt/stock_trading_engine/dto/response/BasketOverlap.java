package com.fynxt.stock_trading_engine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BasketOverlap {

    // Basket name
    private String basket;

    // Overlap percentage
    private String overlap;
}