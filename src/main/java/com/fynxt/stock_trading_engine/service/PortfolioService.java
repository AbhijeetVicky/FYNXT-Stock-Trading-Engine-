package com.fynxt.stock_trading_engine.service;


import com.fynxt.stock_trading_engine.dto.request.AddPortfolioRequest;
import com.fynxt.stock_trading_engine.dto.response.BasketOverlap;
import com.fynxt.stock_trading_engine.dto.response.OverlapResponse;
import com.fynxt.stock_trading_engine.dto.response.PortfolioResponse;
import com.fynxt.stock_trading_engine.entity.Portfolio;
import com.fynxt.stock_trading_engine.enums.RiskFlag;
import com.fynxt.stock_trading_engine.repository.PortfolioRepository;
import com.fynxt.stock_trading_engine.util.BasketData;
import com.fynxt.stock_trading_engine.util.OverlapCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    // Add holdings into portfolio
    @Transactional
    public void addPortfolio(
            String traderId,
            AddPortfolioRequest request
    ) {

        log.info("Adding portfolio for trader: {}", traderId);

        // Check if stock already exists
        Optional<Portfolio> optionalPortfolio =
                portfolioRepository.findByTraderIdAndStock(
                        traderId,
                        request.getStock()
                );

        Portfolio portfolio;

        // If already exists
        // increase quantity
        if (optionalPortfolio.isPresent()) {

            portfolio = optionalPortfolio.get();

            portfolio.setQuantity(
                    portfolio.getQuantity()
                            + request.getQuantity()
            );

        } else {

            // Create new portfolio row
            portfolio = Portfolio.builder()
                    .traderId(traderId)
                    .stock(request.getStock())
                    .sector(request.getSector())
                    .quantity(request.getQuantity())
                    .build();
        }
        portfolioRepository.save(portfolio);

        log.info("Portfolio added successfully");
    }


    public PortfolioResponse getPortfolio(
            String traderId
    ) {

        log.info("Fetching portfolio for trader: {}", traderId);

        List<Portfolio> portfolios =
                portfolioRepository.findByTraderId(traderId);

        if (portfolios.isEmpty()) {
            throw new RuntimeException(
                    "Portfolio not found for trader: " + traderId
            );
        }

        // positions map
        // Example:
        // AAPL -> 100
        Map<String, Integer> positions =
                portfolios.stream()
                        .collect(
                                Collectors.toMap(
                                        Portfolio::getStock,
                                        Portfolio::getQuantity
                                )
                        );

        // sector breakdown
        // Example:
        // TECH -> 150
        Map<String, Integer> sectorBreakdown =
                portfolios.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Portfolio::getSector,
                                        Collectors.summingInt(
                                                Portfolio::getQuantity
                                        )
                                )
                        );
        return PortfolioResponse.builder()
                .traderId(traderId)
                .positions(positions)
                .sectorBreakdown(sectorBreakdown)
                .build();
    }


    // Overlap analysis API
    public OverlapResponse analyzeOverlap(
            String traderId
    ) {

        log.info("Running overlap analysis for trader: {}",
                traderId);

        // Get trader portfolio
        List<Portfolio> portfolios =
                portfolioRepository.findByTraderId(traderId);

        if (portfolios.isEmpty()) {
            throw new RuntimeException(
                    "Portfolio not found for trader: " + traderId
            );
        }

        // Extract only stock names
        Set<String> portfolioStocks =
                portfolios.stream()
                        .map(Portfolio::getStock)
                        .collect(Collectors.toSet());

        List<BasketOverlap> overlapList = new ArrayList<>();

        double highestOverlap = 0;

        String dominantBasket = "NONE";

        // Loop through all baskets
        for (Map.Entry<String, List<String>> entry
                : BasketData.BASKETS.entrySet()) {

            String basketName = entry.getKey();

            List<String> basketStocks = entry.getValue();

            // Calculate overlap percentage
            double overlap =
                    OverlapCalculator.calculateOverlap(
                            portfolioStocks,
                            basketStocks
                    );
            // Store response object
            overlapList.add(
                    BasketOverlap.builder()
                            .basket(basketName)
                            .overlap(
                                    String.format(
                                            "%.2f%%",
                                            overlap
                                    )
                            )
                            .build()
            );

            // Track highest overlap
            if (overlap > highestOverlap) {

                highestOverlap = overlap;

                dominantBasket = basketName;
            }
        }

        // Determine risk level
        RiskFlag riskFlag;

        if (highestOverlap >= 60) {

            riskFlag = RiskFlag.HIGH;

        } else if (highestOverlap >= 40) {

            riskFlag = RiskFlag.MEDIUM;

        } else {

            riskFlag = RiskFlag.LOW;
        }

        return OverlapResponse.builder()
                .overlaps(overlapList)
                .dominantBasket(dominantBasket)
                .riskFlag(riskFlag)
                .build();
    }
}
