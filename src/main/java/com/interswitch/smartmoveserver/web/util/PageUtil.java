package com.interswitch.smartmoveserver.web.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PageUtil {
    public <T> List<Integer> getPageNumber(Page<T> page){
        List<Integer> pageNumbers = new ArrayList<>();
        int totalPages = page.getTotalPages();
        if(totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
        }
        return pageNumbers;
    }
}
