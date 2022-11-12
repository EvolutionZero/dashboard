package com.zero.dashboard.service;

import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.dto.response.ScreenshotResponse;

public interface ReportService {
    ScreenshotResponse screenshot(ScreenshotRequest request);

    ScreenshotResponse kline(ScreenshotRequest request);
}
