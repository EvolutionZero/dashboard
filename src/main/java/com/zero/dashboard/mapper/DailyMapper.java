package com.zero.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.dashboard.entity.Daily;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zero
 * @since 2022-02-07
 */
public interface DailyMapper extends BaseMapper<Daily> {


    @Select("select min(d1.date) from (select row_number() over (order by \"date\" asc) as rownum, * from daily where code = #{code}) as d1\n" +
            "right join (select row_number() over (order by \"date\" asc) as rownum, * from daily where code = #{code}) as d2\n" +
            "on d1.code = d2.code and d1.rownum between d2.rownum - #{n} and d2.rownum\n" +
            "where d2.date = #{date}")
    LocalDate getBackoffTradeDate(@Param("code") String code, @Param("date") LocalDate date, @Param("n") int n);


    @Select("select max(d1.date) from (select row_number() over (order by \"date\" asc) as rownum, * from daily where code = #{code}) as d1\n" +
            "right join (select row_number() over (order by \"date\" asc) as rownum, * from daily where code = #{code}) as d2\n" +
            "on d1.code = d2.code and d1.rownum between d2.rownum and d2.rownum + #{n}\n" +
            "where d2.date = #{date}")
    LocalDate getForwardTradeDate(@Param("code") String code, @Param("date") LocalDate date, @Param("n") int n);
}
