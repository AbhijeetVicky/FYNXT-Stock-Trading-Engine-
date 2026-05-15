package com.fynxt.stock_trading_engine.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;

    private String message;
}