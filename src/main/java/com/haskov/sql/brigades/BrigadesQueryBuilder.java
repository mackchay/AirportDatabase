package com.haskov.sql.brigades;

import com.haskov.sql.Config;
import com.haskov.sql.Filter;
import com.haskov.sql.QueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BrigadesQueryBuilder extends QueryBuilder {

    public String getEmployeesInBrigadeCount(BrigadeFilter filter) {
        return appendFilter(filter,
                new StringBuilder(readFileToString("brigades/EmployeesInBrigadeCount.sql")));
    }

    public String getEmployeesInBrigadeListQuery(BrigadeFilter filter) {
        return appendFilter(filter,
                new StringBuilder(readFileToString("brigades/EmployeesInBrigadeList.sql")));
    }

    @Override
    protected String appendFilter(Filter f, StringBuilder builder) {
        BrigadeFilter filter = (BrigadeFilter) f;
        if (filter == null) {
            return builder.toString();
        }
        else {
            builder.append(" where ");
        }
        if (filter.getAirport() != null) {
            builder.append("airport=").append(filter.getAirport()).append(" and ");
        }
        if (filter.getBrigade() != null) {
            builder.append("brigade=").append(filter.getBrigade()).append(" and ");
        }
        if (filter.getDepartmentTitle() != null && !filter.getDepartmentTitle().isEmpty()) {
            builder.append("department_title = '").append(filter.getDepartmentTitle()).append("' and ");
        }
        if (filter.getFlight() != null) {
            builder.append("flight.id=").append(filter.getFlight()).append(" and ");
        }
        if (filter.getMinAge() != null || filter.getMaxAge() != null) {
            builder.append("age").append(getMinMaxValues(filter.getMinAge(), filter.getMaxAge())).append(" and ");
        }
        if (filter.getMinAvgSalary() != null || filter.getMaxAvgSalary() != null) {
            builder.append("avg_salary").append(getMinMaxValues(filter.getMinAvgSalary(), filter.getMaxAvgSalary()))
                    .append(" and ");
        }
        if (filter.getMinTotalSalary() != null || filter.getMaxTotalSalary() != null) {
            builder.append("total_salary").append(
                    getMinMaxValues(filter.getMinTotalSalary(), filter.getMaxTotalSalary())).append(" and ");
        }
        builder.append("1=1");
        return builder.toString();
    }

    public int executeCountQuery(String query) throws SQLException {
        int count = 0;
        String url = Config.getProperty("db.url");
        String user = Config.getProperty("db.user");
        String password = Config.getProperty("db.password");
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        return count;
    }

    public List<BrigadeEmployee> executeQuery(String query) throws SQLException {
        List<BrigadeEmployee> brigadeEmployees = new ArrayList<>();
        String url = Config.getProperty("db.url");
        String user = Config.getProperty("db.user");
        String password = Config.getProperty("db.password");
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                BrigadeEmployee brigadeEmployee = new BrigadeEmployee();
                brigadeEmployee.setId(rs.getInt("id"));
                brigadeEmployee.setAirport(rs.getInt("airport"));
                brigadeEmployee.setDepartment(rs.getInt("department"));
                brigadeEmployee.setBrigade(rs.getInt("brigade"));
                brigadeEmployee.setSalary(rs.getInt("salary"));
                brigadeEmployee.setDepartmentTitle(rs.getString("department_title"));
                brigadeEmployee.setEmploymentDate(rs.getDate("employment_date"));
                brigadeEmployee.setExperience(rs.getInt("experience"));
                brigadeEmployee.setFullName(rs.getString("full_name"));
                brigadeEmployee.setGender(rs.getString("gender"));
                brigadeEmployee.setAge(rs.getInt("age"));
                brigadeEmployee.setPhoneNumber(rs.getString("phone_number"));
                brigadeEmployee.setChildren(rs.getInt("children"));
                brigadeEmployee.setAvgSalary(rs.getInt("avg_salary"));
                brigadeEmployee.setTotalSalary(rs.getInt("total_salary"));
                brigadeEmployees.add(brigadeEmployee);
            }
        }
        return brigadeEmployees;
    }
}
