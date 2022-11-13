package com.zero.dashboard.dto.ctx;

import com.zero.dashboard.dto.request.KLineReportRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VelocityParamsContext {

    private String code;
    private String name;
    private LocalDate focusDate;
    private int backoff;
    private int forward;


    public VelocityParamsContext(KLineReportRequest request){
        this.focusDate = request.getFocusDate();
        this.backoff = request.getBackoff();
        this.forward = request.getForward();
        this.code = request.getCode();
        this.name = request.getName();
    }
}
