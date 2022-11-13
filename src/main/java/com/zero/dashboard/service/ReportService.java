package com.zero.dashboard.service;

import com.zero.dashboard.dto.request.DailyReportRequest;
import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.dto.response.ScreenshotResponse;

public interface ReportService {
    ScreenshotResponse screenshot(ScreenshotRequest request);

    void whole(DailyReportRequest request);

    void kline(DailyReportRequest request);

}
