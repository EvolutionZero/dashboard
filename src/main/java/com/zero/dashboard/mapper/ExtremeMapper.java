package com.zero.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.dashboard.entity.Extreme;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 极值线 Mapper 接口
 * </p>
 *
 * @author zero
 * @since 2022-06-08
 */
public interface ExtremeMapper extends BaseMapper<Extreme> {

    void saveAll(List<Extreme> list);

    @Select("select min(date) from (\n" +
            "select max(date) as date from extreme where code = #{code} and  \"level\" = 3 and type = 1\n" +
            "union\n" +
            "select max(date) as date from extreme where code = #{code} and \"level\" = 3 and type = 2\n" +
            ") as t")
    LocalDate getLastestDate(@Param("code") String code);

}
