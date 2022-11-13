package com.zero.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.dashboard.entity.Macd;
import com.zero.dashboard.entity.MacdBaseResult;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zero
 * @since 2022-04-12
 */
public interface MacdMapper extends BaseMapper<Macd> {

    void saveAll(List<Macd> list);

    List<MacdBaseResult> analyse(@Param("code") String code, @Param("date") LocalDate date);

    @Select("select max(date) from macd where code = #{code}")
    LocalDate getLastestDate(@Param("code") String code);

}
