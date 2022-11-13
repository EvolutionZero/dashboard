package com.zero.dashboard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 极值线
 * </p>
 *
 * @author zero
 * @since 2022-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Extreme implements Serializable {

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
     * 等级，1-短期，2-中期，3-长期
     */
    private Integer level;

    /**
     * 类型，1-低点，2-高点
     */
    private Integer type;

    /**
     * 值
     */
    private BigDecimal value;


}
