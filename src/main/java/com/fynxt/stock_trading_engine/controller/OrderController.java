package com.fynxt.stock_trading_engine.controller;

import com.fynxt.stock_trading_engine.dto.request.OrderRequest;
import com.fynxt.stock_trading_engine.entity.Order;
import com.fynxt.stock_trading_engine.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Place order API
    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @Valid @RequestBody OrderRequest request
    ) {

        return ResponseEntity.ok(
                orderService.placeOrder(request)
        );
    }

    // Fill order API
    @PutMapping("/{id}/fill")
    public ResponseEntity<String> fillOrder(
            @PathVariable Long id
    ) {

        orderService.fillOrder(id);

        return ResponseEntity.ok(
                "Order filled successfully"
        );
    }

    // Cancel order API
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long id
    ) {

        orderService.cancelOrder(id);

        return ResponseEntity.ok(
                "Order cancelled successfully"
        );
    }
}
