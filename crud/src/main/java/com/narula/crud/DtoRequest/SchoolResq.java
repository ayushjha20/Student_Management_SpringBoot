package com.narula.crud.DtoRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolResq {

    private Long id;

    @NotBlank(message = "Name cannot be empty!!")
    private String name;

    @Min(value = 20, message = "Roll number should be above 20")
    private int Roll_No;

    @NotBlank(message = "Course cannot be blank")
    private String Course;

    private String marks;
    private String Password;
}