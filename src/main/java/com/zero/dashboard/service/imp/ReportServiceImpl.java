package com.zero.dashboard.service.imp;

import com.zero.dashboard.component.Reporter;
import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.service.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public void screenshot(ScreenshotRequest request) {
        String filePath = new Reporter().export(request.getParams(), request.getFileName());

    }
}
