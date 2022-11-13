package com.zero.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.dashboard.dto.bo.VolumeInfo;
import com.zero.dashboard.entity.VolumeRatioBaseResult;
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
public interface VolumeRatioBaseResultMapper extends BaseMapper<VolumeRatioBaseResult> {

    void saveAll(List<VolumeRatioBaseResult> list);

    @Select("select max(date) from volume_ratio_base_result where code = #{code}")
    LocalDate getLastestDate(@Param("code") String code);

    @Select("<script>"
            + "select d.open, d.close, d.date, d.change_percent, d.trading_volume, case when d.turnover is null then 0 else d.turnover end, case when avg is null then d.trading_volume else vrbr.avg end  from (select * from daily d where code = #{code}) as d\n"
            + "left join volume_ratio_base_result vrbr on d.code = vrbr.code and d.date = vrbr.date\n"
            + "where 1=1\n"
            + "<if test='startDate!=null'>" + "and d.date <![CDATA[ >= ]]> #{startDate}\n" + "</if>\n"
            + "<if test='endDate!=null'>" + "and d.date <![CDATA[ <= ]]> #{endDate}\n" + "</if>\n"
            + "order by d.date"
            + "</script>"
    )
    List<VolumeInfo> listVolumeInfo(@Param("code") String code, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
