package com.zero.dashboard.enums;

import lombok.Getter;

public enum ScreenshotTypeEnum {

    HTML("HTML"),
    PNG("PNG")
    ;

    @Getter
    public String value;

    ScreenshotTypeEnum(String value){
        this.value = value;
    }
}
