package com.fynxt.stock_trading_engine.repository;

import com.fynxt.stock_trading_engine.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // Find portfolio by trader and stock
    Optional<Portfolio> findByTraderIdAndStock(
            String traderId,
            String stock
    );

    // Get all holdings of trader
    List<Portfolio> findByTraderId(
            String traderId
    );
}