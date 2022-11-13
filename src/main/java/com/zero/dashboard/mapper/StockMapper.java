package com.zero.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.dashboard.entity.Stock;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zero
 * @since 2022-02-06
 */
public interface StockMapper extends BaseMapper<Stock> {

    void saveAll(List<Stock> list);

    @Select("select * from stock")
    List<Stock> calculateMACD();


}
