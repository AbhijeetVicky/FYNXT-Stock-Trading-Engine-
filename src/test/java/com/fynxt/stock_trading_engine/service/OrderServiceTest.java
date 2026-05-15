package com.fynxt.stock_trading_engine.service;

import com.fynxt.stock_trading_engine.repository.OrderRepository;
import com.fynxt.stock_trading_engine.dto.request.OrderRequest;
import com.fynxt.stock_trading_engine.entity.Order;
import com.fynxt.stock_trading_engine.entity.Portfolio;
import com.fynxt.stock_trading_engine.enums.OrderSide;
import com.fynxt.stock_trading_engine.enums.OrderStatus;
import com.fynxt.stock_trading_engine.exception.BusinessException;
import com.fynxt.stock_trading_engine.repository.OrderRepository;
import com.fynxt.stock_trading_engine.repository.PortfolioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PortfolioRepository portfolioRepository;
    // Inject mocks into service
    @InjectMocks
    private OrderService orderService;

    private OrderRequest request;

    @BeforeEach
    void setup() {

        request = new OrderRequest();

        request.setTraderId("T001");
        request.setStock("AAPL");
        request.setSector("TECH");
        request.setQuantity(10);
        request.setSide(OrderSide.BUY);
    }


    // Test successful order creation
    @Test
    void placeOrder_Success() {

        // Mock pending order count
        when(orderRepository.countByTraderIdAndStatus(
                "T001",
                OrderStatus.PENDING
        )).thenReturn(1L);

        // Mock save operation
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.placeOrder(request);

        Assertions.assertNotNull(order);

        Assertions.assertEquals(
                OrderStatus.PENDING,
                order.getStatus()
        );
    }

    // Test pending order limit
    @Test
    void placeOrder_ShouldThrowException_WhenPendingLimitExceeded() {

        when(orderRepository.countByTraderIdAndStatus(
                "T001",
                OrderStatus.PENDING
        )).thenReturn(3L);

        Assertions.assertThrows(
                BusinessException.class,
                () -> orderService.placeOrder(request)
        );
    }

    // Test SELL validation
    @Test
    void placeOrder_ShouldThrowException_WhenInsufficientShares() {

        request.setSide(OrderSide.SELL);

        when(orderRepository.countByTraderIdAndStatus(
                "T001",
                OrderStatus.PENDING
        )).thenReturn(1L);

        Portfolio portfolio = Portfolio.builder()
                .traderId("T001")
                .stock("AAPL")
                .quantity(5)
                .build();

        when(portfolioRepository.findByTraderIdAndStock(
                "T001",
                "AAPL"
        )).thenReturn(Optional.of(portfolio));

        Assertions.assertThrows(
                BusinessException.class,
                () -> orderService.placeOrder(request)
        );
    }
}
