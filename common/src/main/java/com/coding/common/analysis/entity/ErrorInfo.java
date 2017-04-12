package com.coding.common.analysis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by luoziyihao on 4/12/17.
 */
@Data
@Accessors(fluent = true)
public class ErrorInfo {
    private String message;
    private String type;
    private String text;
}