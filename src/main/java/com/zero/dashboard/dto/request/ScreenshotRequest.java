package com.zero.dashboard.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class ScreenshotRequest {

    private String code;
    private String name;
    private String fileName;
    private Map<String, Object> params;
}
