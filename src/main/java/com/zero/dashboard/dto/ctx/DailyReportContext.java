package com.zero.dashboard.dto.ctx;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DailyReportContext {
    private String code;
    private String name;
    private String bucketName;
    private String objectPath;

    private String fileName;
    private String templateName;
    private Map<String, Object> params;
    private List<String> divs;
}
