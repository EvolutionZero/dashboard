package com.zero.dashboard.service.imp;

import com.zero.dashboard.component.Reporter;
import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.dto.response.ScreenshotResponse;
import com.zero.dashboard.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private Reporter reporter;

    @Override
    public ScreenshotResponse screenshot(ScreenshotRequest request) {
        return reporter.export(request);
    }
}
