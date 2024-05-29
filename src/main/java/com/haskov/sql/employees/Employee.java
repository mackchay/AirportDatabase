package com.haskov.sql.employees;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Employee {
    private Integer id;
    private Integer airport;
    private Integer department;
    private Integer brigade;
    private Integer salary;
    private String departmentTitle;
    private Date employmentDate;
    private Integer experience;
    private String fullName;
    private String gender;
    private Integer age;
    private String phoneNumber;
    private Integer children;
}
