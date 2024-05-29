package com.haskov.sql.employees;

import com.haskov.sql.Config;
import com.haskov.sql.Filter;
import com.haskov.sql.QueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeesQueryBuilder extends QueryBuilder {

    @Override
    protected String appendFilter(Filter f, StringBuilder builder) {
        EmployeeFilter filter = (EmployeeFilter) f;
        if (filter == null) {
            builder.append(";");
            return builder.toString();
        }
        builder.append(" where ");
        if (filter.getAirport() != null) {
            builder.append("airport = ").append(filter.getAirport()).append(" and ");
        }
        if (filter.getGender() != null && !filter.getGender().isEmpty()) {
            builder.append("gender = '").append(filter.getGender()).append("' and ");
        }
        if (filter.getMaxAge() != null || filter.getMinAge() != null) {
            builder.append("age").append(getMinMaxValues(filter.getMinAge(), filter.getMaxAge())).append(" and ");
        }
        if (filter.getMaxChildren() != null || filter.getMinChildren() != null) {
            builder.append("children").append(getMinMaxValues(filter.getMinChildren(), filter.getMaxChildren()))
                    .append(" and ");
        }
        if (filter.getMaxSalary() != null && filter.getMinSalary() != null) {
            builder.append("salary").append(getMinMaxValues(filter.getMinSalary(), filter.getMaxSalary()))
                    .append(" and ");
        }
        if (filter.getMinExperience() != null && filter.getMaxExperience() != null) {
            builder.append("experience").append(getMinMaxValues(filter.getMinExperience(), filter.getMaxExperience()))
                    .append(" and ");
        }
        if (filter.getDepartmentTitle() != null && !filter.getDepartmentTitle().isEmpty()) {
            builder.append("department_title").append(" = '").append(filter.getDepartmentTitle())
                    .append("' and ");
        }
        builder.append("1=1");
        return builder.toString();
    }

    public String getEmployeesListQuery(EmployeeFilter filter) {
        return appendFilter(filter, new StringBuilder(readFileToString("employees/EmployeesList.sql")));
    }

    public String getEmployeesCountQuery(EmployeeFilter filter) {
        return appendFilter(filter, new StringBuilder(readFileToString("employees/EmployeesCount.sql")));
    }

    public String getDepartmentHeadsListQuery(EmployeeFilter filter) {
        return appendFilter(filter, new StringBuilder(readFileToString("employees/DepartmentHeadsList.sql")));
    }

    public String getDepartmentHeadsCountQuery(EmployeeFilter filter) {
        return appendFilter(filter, new StringBuilder(readFileToString("employees/DepartmentHeadsCount.sql")));
    }

    public List<Employee> executeQuery(String query) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String url = Config.getProperty("db.url");
        String user = Config.getProperty("db.user");
        String password = Config.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setAirport(rs.getInt("airport"));
                employee.setDepartment(rs.getInt("department"));
                employee.setBrigade(rs.getInt("brigade"));
                employee.setSalary(rs.getInt("salary"));
                employee.setDepartmentTitle(rs.getString("department_title"));
                employee.setEmploymentDate(rs.getDate("employment_date"));
                employee.setExperience(rs.getInt("experience"));
                employee.setFullName(rs.getString("full_name"));
                employee.setGender(rs.getString("gender"));
                employee.setAge(rs.getInt("age"));
                employee.setPhoneNumber(rs.getString("phone_number"));
                employee.setChildren(rs.getInt("children"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return employees;
    }

    public int executeCountQuery(String query) throws SQLException {
        String url = Config.getProperty("db.url");
        String user = Config.getProperty("db.user");
        String password = Config.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return 0;
    }
}
