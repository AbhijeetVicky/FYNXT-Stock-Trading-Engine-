package com.fynxt.stock_trading_engine.dto.request;


import com.fynxt.stock_trading_engine.enums.OrderSide;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

    @NotBlank(message = "Trader ID is required")
    private String traderId;

    @NotBlank(message = "Stock is required")
    private String stock;

    @NotBlank(message = "Sector is required")
    private String sector;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than zero")
    private Integer quantity;

    @NotNull(message = "Order side is required")
    private OrderSide side;
}
