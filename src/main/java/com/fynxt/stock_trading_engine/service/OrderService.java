package com.fynxt.stock_trading_engine.service;


import com.fynxt.stock_trading_engine.dto.request.OrderRequest;
import com.fynxt.stock_trading_engine.entity.Order;
import com.fynxt.stock_trading_engine.entity.Portfolio;
import com.fynxt.stock_trading_engine.enums.OrderSide;
import com.fynxt.stock_trading_engine.enums.OrderStatus;
import com.fynxt.stock_trading_engine.exception.BusinessException;
import com.fynxt.stock_trading_engine.exception.ResourceNotFoundException;
import com.fynxt.stock_trading_engine.repository.OrderRepository;
import com.fynxt.stock_trading_engine.repository.PortfolioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    // Constructor injection using Lombok
    private final OrderRepository orderRepository;

    private final PortfolioRepository portfolioRepository;


    // Place new order
    @Transactional
    public Order placeOrder(OrderRequest request) {

        log.info("Placing order for trader: {}", request.getTraderId());

        // BUSINESS RULE:
        // Trader cannot have more than 3 pending orders
        long pendingOrders =
                orderRepository.countByTraderIdAndStatus(
                        request.getTraderId(),
                        OrderStatus.PENDING
                );

        if (pendingOrders >= 3) {

            throw new BusinessException(
                    "Trader already has 3 pending orders"
            );
        }

        // SELL VALIDATION
        // Trader must have enough shares
        if (request.getSide() == OrderSide.SELL) {

            Portfolio portfolio =
                    portfolioRepository
                            .findByTraderIdAndStock(
                                    request.getTraderId(),
                                    request.getStock()
                            )
                            .orElseThrow(() ->
                                    new BusinessException(
                                            "Portfolio not found"
                                    )
                            );

            // Check available quantity
            if (portfolio.getQuantity()
                    < request.getQuantity()) {

                throw new BusinessException(
                        "Insufficient shares"
                );
            }
        }

        // Create order object
        Order order = Order.builder()
                .traderId(request.getTraderId())
                .stock(request.getStock())
                .sector(request.getSector())
                .quantity(request.getQuantity())
                .side(request.getSide())
                .status(OrderStatus.PENDING)
                .build();


        // Save into database
        Order savedOrder = orderRepository.save(order);

        log.info("Order created successfully with id: {}",
                savedOrder.getId());

        return savedOrder;
    }


    // Fill order
    @Transactional
    public void fillOrder(Long orderId) {

        log.info("Filling order id: {}", orderId);

        // Fetch order from database
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        )
                );

        // Only pending orders can be filled
        if (order.getStatus() != OrderStatus.PENDING) {

            throw new BusinessException(
                    "Only pending orders can be filled"
            );
        }


        // Fetch portfolio
        // If trader buys stock first time,
        // create new portfolio object
        Portfolio portfolio =
                portfolioRepository
                        .findByTraderIdAndStock(
                                order.getTraderId(),
                                order.getStock()
                        )
                        .orElse(
                                Portfolio.builder()
                                        .traderId(order.getTraderId())
                                        .stock(order.getStock())
                                        .sector(order.getSector())
                                        .quantity(0)
                                        .build()
                        );

        // BUY increases quantity
        if (order.getSide() == OrderSide.BUY) {

            portfolio.setQuantity(
                    portfolio.getQuantity()
                            + order.getQuantity()
            );

        } else {

            // SELL decreases quantity
            portfolio.setQuantity(
                    portfolio.getQuantity()
                            - order.getQuantity()
            );
        }

        // Save updated portfolio
        portfolioRepository.save(portfolio);

        // Change order status
        order.setStatus(OrderStatus.FILLED);

        orderRepository.save(order);

        log.info("Order filled successfully");
    }

    // Cancel order
    @Transactional
    public void cancelOrder(Long orderId) {

        log.info("Cancelling order id: {}", orderId);

        // Fetch order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        )
                );

        // Only pending orders can be cancelled
        if (order.getStatus() != OrderStatus.PENDING) {

            throw new BusinessException(
                    "Only pending orders can be cancelled"
            );
        }

        // Update status
        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        log.info("Order cancelled successfully");
    }

}