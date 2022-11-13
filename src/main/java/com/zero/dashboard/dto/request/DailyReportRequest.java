package com.zero.dashboard.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyReportRequest {
    private String code;
    private String name;
    private String fileName;
    private LocalDate focusDate;
    private int backoff;
    private int forward;
    private String bucketName;
    private String objectPath;

}
