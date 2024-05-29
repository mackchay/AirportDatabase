package com.haskov.sql.brigades;

import com.haskov.sql.Filter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BrigadeFilter extends Filter {
    private Integer airport;
    private Integer brigade;
    private String departmentTitle;
    private Integer minAge;
    private Integer maxAge;
    private Integer minAvgSalary;
    private Integer maxAvgSalary;
    private Integer minTotalSalary;
    private Integer maxTotalSalary;
    private Integer flight;
}
