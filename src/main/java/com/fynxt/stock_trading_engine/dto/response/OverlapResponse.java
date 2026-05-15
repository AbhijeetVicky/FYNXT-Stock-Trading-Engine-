package com.fynxt.stock_trading_engine.dto.response;

import com.fynxt.stock_trading_engine.enums.RiskFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
@Builder
public class OverlapResponse {

    // List of all basket overlaps
    private List<BasketOverlap> overlaps;

    // Basket with highest overlap
    private String dominantBasket;

    // HIGH/MEDIUM/LOW
    private RiskFlag riskFlag;
}