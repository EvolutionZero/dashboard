package com.zero.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.zero.dashboard.entity.KdjBaseResult;
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
 * @since 2022-05-21
 */
public interface KdjBaseResultMapper extends BaseMapper<KdjBaseResult> {

    void saveAll(List<KdjBaseResult> list);

    @Select("select max(date) from kdj_base_result where code = #{code}")
    LocalDate getLastestDate(@Param("code") String code);

}
