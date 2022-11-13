package com.zero.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.dashboard.entity.TurnoverBaseResult;
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
 * @since 2022-04-19
 */
public interface TurnoverBaseResultMapper extends BaseMapper<TurnoverBaseResult> {

    void saveAll(List<TurnoverBaseResult> list);

    @Select("select max(date) from turnover_base_result where code = #{code}")
    LocalDate getLastestDate(@Param("code") String code);

}
