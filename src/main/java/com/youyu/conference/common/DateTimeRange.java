package com.youyu.conference.common;

import lombok.Data;

@Data
public class DateTimeRange<T> {
    private T start;
    private T end;
    
    private DateTimeRange(T start, T end) {
        this.start = start;
        this.end = end;
    }
    
    public static <T> DateTimeRange between(T start, T end) {
        return new DateTimeRange<>(start, end);
    }
}
