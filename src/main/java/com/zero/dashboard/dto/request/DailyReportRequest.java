package com.zero.dashboard.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class DailyReportRequest {
    private String code;
    private String name;
    private LocalDate focusDate;
    private int backoff;
    private int forward;
    private String bucketName;
    private String objectPath;

    public String getFileName(){
        return code + "_" + name + (focusDate != null ? "_" + focusDate.format(DateTimeFormatter.ISO_DATE) : "");
    }
}
