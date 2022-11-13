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
 * @since 2022-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class KdjBaseResult implements Serializable {

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
     * 周期
     */
    private Integer n;

    private BigDecimal preK;

    private BigDecimal preD;

    private BigDecimal preJ;

    private BigDecimal curK;

    private BigDecimal curD;

    private BigDecimal curJ;

    private String area;

    private String market;

    private String cross;

    private BigDecimal kAngle;

    private BigDecimal dAngle;

    private BigDecimal jAngle;

    private BigDecimal inclination;


}
