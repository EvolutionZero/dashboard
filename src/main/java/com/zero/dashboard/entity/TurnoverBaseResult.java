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
 * @since 2022-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TurnoverBaseResult implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编码
     */
    private String code;

    /**
     * 日期
     */
    private Date date;

    /**
     * 换手率
     */
    private BigDecimal turnover;

    /**
     * 股票状态
     */
    private String status;

    /**
     * 盘口观察
     */
    private String focus;

    /**
     * 资金介入程度
     */
    private String intervene;

    /**
     * 操作策略
     */
    private String operateStrategy;

    /**
     * 走势趋向
     */
    private String trend;


}
