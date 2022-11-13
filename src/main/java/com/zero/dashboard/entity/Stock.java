package com.zero.dashboard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author zero
 * @since 2022-02-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class Stock implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 股票号
     */
    private String code;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 上市日期
     */
    private LocalDate listDate;

    private String exchange;

    private String tsCode;

    private String area;

    private String industry;

    private String fullName;

    private String enName;

    private String market;

    private String currType;

    private String listStatus;

    private LocalDate delistDate;

    private String isHs;

}
