package com.zero.dashboard.controller;

import com.zero.dashboard.dto.request.KLineReportRequest;
import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.dto.response.ScreenshotResponse;
import com.zero.dashboard.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping({"/report"})
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping(value = "/screenshot", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ScreenshotResponse screenshot(@RequestBody ScreenshotRequest request){
        return reportService.screenshot(request);
    }

    @PostMapping(value = "/kline", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public void kline(@RequestBody KLineReportRequest request){
        reportService.kline(request);
    }
}
