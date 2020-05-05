package com.interswitch.smartmoveserver.util;

import com.interswitch.smartmoveserver.model.Enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author adebola.owolabi
 */
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

    public PageRequest buildPageRequest(int page, int size){
        if(page == 0) page = 1;
        if(size == 0) size = 10;
        return PageRequest.of(page - 1, size);
    }

    public String buildTitle(Enum.Role role){
        String title = "";
        switch(role)
        {
            case ISW_ADMIN:
                title = "Administrator";
                break;
            case REGULATOR:
                title = "Regulator";
                break;
            case OPERATOR:
                title = "Operator";
                break;
            case VEHICLE_OWNER:
                title = "Vehicle Owner";
                break;
            case AGENT:
                title = "Agent";
                break;
            default:
                title = "No Title";
                break;
        }
        return title;
    }
}
