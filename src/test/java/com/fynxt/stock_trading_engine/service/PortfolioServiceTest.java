package com.fynxt.stock_trading_engine.service;

import com.fynxt.stock_trading_engine.dto.response.OverlapResponse;
import com.fynxt.stock_trading_engine.entity.Portfolio;
import com.fynxt.stock_trading_engine.repository.PortfolioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {
    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private PortfolioService portfolioService;

    // Test overlap analysis
    @Test
    void analyzeOverlap_ShouldReturnHighRisk() {
        List<Portfolio> portfolios = List.of(

                Portfolio.builder()
                        .stock("AAPL")
                        .quantity(100)
                        .sector("TECH")
                        .build(),

                Portfolio.builder()
                        .stock("TSLA")
                        .quantity(50)
                        .sector("TECH")
                        .build(),

                Portfolio.builder()
                        .stock("NVDA")
                        .quantity(70)
                        .sector("TECH")
                        .build()
        );
        when(portfolioRepository.findByTraderId("T001"))
                .thenReturn(portfolios);

        OverlapResponse response =
                portfolioService.analyzeOverlap("T001");

        Assertions.assertEquals(
                "TECH_HEAVY",
                response.getDominantBasket()
        );
    }
}
