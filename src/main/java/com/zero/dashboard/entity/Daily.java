package com.zero.dashboard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zero
 * @since 2022-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class Daily implements Serializable {

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
     * 开盘价
     */
    private BigDecimal open;

    /**
     * 最高价
     */
    private BigDecimal high;

    /**
     * 收盘价
     */
    private BigDecimal close;

    /**
     * 最低价
     */
    private BigDecimal low;

    /**
     * 交易量
     */
    private BigDecimal tradingVolume;

    /**
     * 成交金额
     */
    private BigDecimal tradingValue;

    /**
     * 升跌价格
     */
    private BigDecimal changePrice;

    /**
     * 升跌百分比
     */
    private BigDecimal changePercent;

    private BigDecimal preClose;

    private String tsCode;

    /**
     * 换手率
     */
    private BigDecimal turnover;

    public List<Object> toReportData() {
        List<Object> data = new LinkedList<>();
        data.add(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date));
        data.add(open.doubleValue());
        data.add(close.doubleValue());
        data.add(low.doubleValue());
        data.add(high.doubleValue());
        data.add(tradingVolume != null ? tradingVolume.longValue() : 0);
        return data;
    }

}
