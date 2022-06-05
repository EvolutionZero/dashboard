package com.zero.dashboard.service.imp;

import com.zero.dashboard.component.Reporter;
import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.dto.response.ScreenshotResponse;
import com.zero.dashboard.service.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public ScreenshotResponse screenshot(ScreenshotRequest request) {
        return new Reporter().export(request);
    }
}
