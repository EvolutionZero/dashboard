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
public class VolumeRatioBaseResult implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编码
     */
    private String code;

    private Date date;

    /**
     * 交易量
     */
    private BigDecimal tradingVolume;

    /**
     * 近5日交易量平均数
     */
    private BigDecimal avg;

    /**
     * 量比
     */
    private String volumeRatio;

    /**
     * 状态
     */
    private String status;

    /**
     * 描述
     */
    private String desc;


}
