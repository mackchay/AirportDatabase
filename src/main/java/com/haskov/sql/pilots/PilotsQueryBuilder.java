package com.haskov.sql.pilots;

import com.haskov.sql.Filter;
import com.haskov.sql.QueryBuilder;

public class PilotsQueryBuilder extends QueryBuilder {
    @Override
    protected String appendFilter(Filter f, StringBuilder builder) {
        PilotFilter filter = (PilotFilter) f;
        if (filter == null) {
            return builder.toString();
        }
        builder.append(" where ");
        if (filter.getGender() != null) {
            builder.append("gender=").append(filter.getGender()).append(" and ");
        }
        if (filter.getAirport() != null) {
            builder.append("airport=").append(filter.getAirport()).append(" and ");
        }
        if (filter.getYear() != null) {
            builder.append("extract(year from date)=").append(filter.getYear()).append(" and ");
        }
        if (filter.getResult()) {
            builder.append("result=true").append(" and ");
        }
        else {
            builder.append("(pilot is null or result = false)").append(" and ");
        }
        if (filter.getMinAge() != null && filter.getMaxAge() != null) {
            builder.append("age").append(" between ").append(filter.getMinAge()).append(" and ").
                    append(filter.getMaxAge()).append(" and ");
        }
        if (filter.getMinSalary() != null && filter.getMaxSalary() != null) {
            builder.append("salary").append(" between ").append(filter.getMinSalary()).append(" and ")
                    .append(filter.getMaxSalary()).append(" and ");
        }
        return builder.toString();
    }

    public String getPilotsListQuery(PilotFilter filter) {
        return appendFilter(filter, new StringBuilder(
                readFileToString("pilots/PilotsList.sql")));
    }

    public String getPilotsCountQuery(PilotFilter filter) {
        return appendFilter(filter, new StringBuilder(
                readFileToString("pilots/PilotsCount.sql")));
    }
}
