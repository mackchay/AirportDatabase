package com.haskov.sql.employees;

import com.haskov.sql.Filter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeFilter extends Filter {
    private Integer airport;
    private Integer minExperience;
    private Integer maxExperience;
    private Integer minAge;
    private Integer maxAge;
    private String gender;
    private Integer minChildren;
    private Integer maxChildren;
    private Integer minSalary;
    private Integer maxSalary;
    private String departmentTitle;
}
