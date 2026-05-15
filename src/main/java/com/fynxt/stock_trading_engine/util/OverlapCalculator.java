package com.fynxt.stock_trading_engine.util;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class OverlapCalculator {

    private OverlapCalculator() {
    }

    // Calculate overlap percentage
    public static double calculateOverlap(
            Set<String> portfolioStocks,
            List<String> basketStocks
    ) {

        // Convert basket list into set
        Set<String> basketSet = new HashSet<>(basketStocks);

        // Create common set
        Set<String> commonStocks = new HashSet<>(portfolioStocks);

        // retainAll keeps only common elements
        commonStocks.retainAll(basketSet);

        // Formula:
        // [2 × common] / [portfolio + basket] × 100
        return (
                (2.0 * commonStocks.size())
                        /
                        (portfolioStocks.size() + basketSet.size())
        ) * 100;
    }
}


