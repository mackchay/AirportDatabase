package com.haskov.sql.pilots;

import com.haskov.sql.Filter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PilotFilter extends Filter {
    private Integer airport;
    private Boolean result;
    private Integer year;
    private String gender;
    private Integer minAge;
    private Integer maxAge;
    private Integer minSalary;
    private Integer maxSalary;
}
