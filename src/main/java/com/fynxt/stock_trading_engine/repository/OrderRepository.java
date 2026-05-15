package com.fynxt.stock_trading_engine.repository;

import com.fynxt.stock_trading_engine.entity.Order;
import com.fynxt.stock_trading_engine.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Count pending orders of trader
    long countByTraderIdAndStatus(
            String traderId,
            OrderStatus status
    );
}