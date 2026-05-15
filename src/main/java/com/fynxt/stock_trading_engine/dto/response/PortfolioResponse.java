package com.fynxt.stock_trading_engine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
@Getter
@AllArgsConstructor
@Builder
public class PortfolioResponse {

    // Trader id
    private String traderId;

    // Example:
    // AAPL -> 100
    // TSLA -> 50
    private Map<String, Integer> positions;

    // Example:
    // TECH -> 150
    private Map<String, Integer> sectorBreakdown;
}
