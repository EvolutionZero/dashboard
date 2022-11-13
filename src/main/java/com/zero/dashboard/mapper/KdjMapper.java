package com.zero.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.dashboard.entity.Kdj;
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
public interface KdjMapper extends BaseMapper<Kdj> {

    void saveAll(List<Kdj> list);

    List<KdjBaseResult> analyse(@Param("code") String code, @Param("date") LocalDate date);

    @Select("select max(date) from kdj where code = #{code}")
    LocalDate getLastestDate(@Param("code") String code);
}
