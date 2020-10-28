package com.interswitch.smartmoveserver.model;

import lombok.Data;

import java.util.List;

/**
 * @author adebola.owolabi
 */
@Data
public class PageView<T> {
    private Long count;
    private List<T> content;

    public PageView(Long count, List<T> content) {
        this.count = count;
        this.content = content;
    }
}
