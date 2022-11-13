package com.zero.dashboard.dto.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VolumeInfo {
    private LocalDate date;
    private BigDecimal changePercent;
    private BigDecimal tradingVolume;
    private BigDecimal turnover;
    private BigDecimal avg;
    private BigDecimal open;
    private BigDecimal close;
}
