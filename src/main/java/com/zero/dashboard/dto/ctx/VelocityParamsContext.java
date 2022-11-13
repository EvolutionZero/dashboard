package com.zero.dashboard.dto.ctx;

import com.zero.dashboard.entity.Stock;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VelocityParamsContext {

    private Stock stock;
    private LocalDate focusDate;
    private int backoff;
    private int forward;
}
