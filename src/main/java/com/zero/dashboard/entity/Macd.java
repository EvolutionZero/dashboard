package com.zero.dashboard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author zero
 * @since 2022-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Macd implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编码
     */
    private String code;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 长周期
     */
    private Integer l;

    /**
     * 短周期
     */
    private Integer s;

    /**
     * 中间周期
     */
    private Integer mid;

    private BigDecimal dif;

    private BigDecimal dea;

    private BigDecimal macd;

    private BigDecimal sEma;

    private BigDecimal lEma;



}
