package com.interswitch.smartmoveserver.model;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    private Long count;
    private List<T> content;

    public Page(Long count, List<T> content) {
        this.count = count;
        this.content = content;
    }
}
