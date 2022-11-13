package com.zero.dashboard.dto.ctx;

import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.zero.dashboard.dto.request.KLineReportRequest;
import com.zero.dashboard.entity.Stock;
import com.zero.dashboard.mapper.StockMapper;
import com.zero.dashboard.utils.ApplicationContextUtils;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VelocityParamsContext {

    private Stock stock;
    private LocalDate focusDate;
    private int backoff;
    private int forward;


    public VelocityParamsContext(KLineReportRequest request){
        StockMapper stockMapper = ApplicationContextUtils.getApplicationContext().getBean(StockMapper.class);
        this.stock = new LambdaQueryChainWrapper<>(stockMapper).eq(Stock::getCode, request.getCode()).one();
        this.focusDate = request.getFocusDate();
        this.backoff = request.getBackoff();
        this.forward = request.getForward();
    }
}
