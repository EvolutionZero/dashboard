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
public class Kdj implements Serializable {

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

    /**
     * 未成熟随机值
     */
    private BigDecimal rsv;

    /**
     * 快速确认线
     */
    private BigDecimal k;

    /**
     * 慢速主干线
     */
    private BigDecimal d;

    /**
     * 方向敏感线
     */
    private BigDecimal j;

    public void scale() {
        k = k.setScale(2, BigDecimal.ROUND_HALF_UP);
        d = d.setScale(2, BigDecimal.ROUND_HALF_UP);
        j = j.setScale(2, BigDecimal.ROUND_HALF_UP);
        rsv = rsv.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


}
