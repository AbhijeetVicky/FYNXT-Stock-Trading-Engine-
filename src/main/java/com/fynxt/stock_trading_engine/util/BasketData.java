package com.fynxt.stock_trading_engine.util;
import java.util.List;
import java.util.Map;
public class BasketData {

    // Private constructor
    // Prevent object creation
    private BasketData() {
    }

    // Static basket data
    public static final Map<String, List<String>> BASKETS = Map.of(

            "TECH_HEAVY",
            List.of(
                    "AAPL",
                    "MSFT",
                    "GOOGL",
                    "TSLA",
                    "NVDA"
            ),

            "FINANCE_HEAVY",
            List.of(
                    "JPM",
                    "GS",
                    "BAC",
                    "MS",
                    "WFC"
            ),

            "BALANCED",
            List.of(
                    "AAPL",
                    "JPM",
                    "XOM",
                    "JNJ",
                    "TSLA"
            )
    );
}
