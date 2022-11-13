package com.zero.dashboard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zero
 * @since 2022-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MacdBaseResult implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编码
     */
    private String code;

    private Date date;

    private Integer s;

    private Integer l;

    private Integer mid;

    private String cross;

    private String turn;

    private String lineTrend;

    private String macdTrend;

    private BigDecimal preDif;

    private BigDecimal curDif;

    private BigDecimal preDea;

    private BigDecimal curDea;

    private BigDecimal preMacd;

    private BigDecimal curMacd;

    private BigDecimal difAngle;

    private BigDecimal deaAngle;

    private BigDecimal macdAngle;

    private BigDecimal inclination;


}
