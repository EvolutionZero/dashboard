package com.zero.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @since 2022-04-16
 */
public interface MacdBaseResultMapper extends BaseMapper<MacdBaseResult> {

    void saveAll(List<MacdBaseResult> list);

    @Select("select max(date) from macd_base_result where code = #{code}")
    LocalDate getLastestDate(@Param("code") String code);

}
