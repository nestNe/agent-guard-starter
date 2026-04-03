package com.seehold.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {

    private Long current;
    private Long size;
    private Long total;
    private Long pages;
    private List<T> records;

    public PageVO(Long current, Long size, Long total, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records;
        this.pages = (total + size - 1) / size;
    }
}