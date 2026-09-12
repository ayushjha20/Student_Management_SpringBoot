package com.narula.crud.DtoResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolResp {

    private Long id;
    private String name;
    private int Roll_No;
    private String Course;
    private String marks;
}