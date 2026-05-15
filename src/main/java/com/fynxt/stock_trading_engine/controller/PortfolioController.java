package com.fynxt.stock_trading_engine.controller;

import com.fynxt.stock_trading_engine.dto.request.AddPortfolioRequest;
import com.fynxt.stock_trading_engine.dto.response.OverlapResponse;
import com.fynxt.stock_trading_engine.dto.response.PortfolioResponse;
import com.fynxt.stock_trading_engine.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;

    // Add holdings API
    @PostMapping("/{traderId}")
    public ResponseEntity<String> addPortfolio(
            @PathVariable String traderId,
            @Valid @RequestBody AddPortfolioRequest request
    ) {

        portfolioService.addPortfolio(traderId, request);

        return ResponseEntity.ok(
                "Portfolio added successfully"
        );
    }

    // Get portfolio API
    @GetMapping("/{traderId}")
    public ResponseEntity<PortfolioResponse> getPortfolio(
            @PathVariable String traderId
    ) {

        return ResponseEntity.ok(
                portfolioService.getPortfolio(traderId)
        );
    }

    // Overlap analysis API
    @GetMapping("/{traderId}/analysis")
    public ResponseEntity<OverlapResponse> analyzeOverlap(
            @PathVariable String traderId
    ) {

        return ResponseEntity.ok(
                portfolioService.analyzeOverlap(traderId)
        );
    }
}
