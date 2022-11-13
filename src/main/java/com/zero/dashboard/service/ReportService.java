package com.zero.dashboard.service;

import com.zero.dashboard.dto.ctx.VelocityParamsContext;
import com.zero.dashboard.dto.request.KLineReportRequest;
import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.dto.response.ScreenshotResponse;

import java.util.Map;

public interface ReportService {
    ScreenshotResponse screenshot(ScreenshotRequest request);

    void kline(KLineReportRequest request);

}
