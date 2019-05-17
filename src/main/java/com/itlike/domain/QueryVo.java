package com.itlike.domain;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class QueryVo {
    private int page;
    private int rows;
    private String keyword;
}
