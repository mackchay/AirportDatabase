package com.haskov.gui.employees;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import com.haskov.sql.employees.Employee;
import com.haskov.sql.employees.EmployeeFilter;
import com.haskov.sql.employees.EmployeesQueryBuilder;

public class EmployeeFilterGUI extends JFrame {

    private JTextField airportField;
    private JComboBox<String> genderComboBox;
    private JTextField minAgeField;
    private JTextField maxAgeField;
    private JTextField minChildrenField;
    private JTextField maxChildrenField;
    private JTextField minSalaryField;
    private JTextField maxSalaryField;
    private JTextField minExperienceField;
    private JTextField maxExperienceField;
    private JComboBox<String> departmentComboBox;

    private EmployeesQueryBuilder queryBuilder;

    public EmployeeFilterGUI() {
        queryBuilder = new EmployeesQueryBuilder();
        setTitle("Employee Filter");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(13, 2));

        add(new JLabel("Airport:"));
        airportField = new JTextField();
        add(airportField);

        add(new JLabel("Gender:"));
        genderComboBox = new JComboBox<>(new String[]{"", "Male", "Female"});
        add(genderComboBox);

        add(new JLabel("Min Age:"));
        minAgeField = new JTextField();
        add(minAgeField);

        add(new JLabel("Max Age:"));
        maxAgeField = new JTextField();
        add(maxAgeField);

        add(new JLabel("Min Children:"));
        minChildrenField = new JTextField();
        add(minChildrenField);

        add(new JLabel("Max Children:"));
        maxChildrenField = new JTextField();
        add(maxChildrenField);

        add(new JLabel("Min Salary:"));
        minSalaryField = new JTextField();
        add(minSalaryField);

        add(new JLabel("Max Salary:"));
        maxSalaryField = new JTextField();
        add(maxSalaryField);

        add(new JLabel("Min Experience:"));
        minExperienceField = new JTextField();
        add(minExperienceField);

        add(new JLabel("Max Experience:"));
        maxExperienceField = new JTextField();
        add(maxExperienceField);

        add(new JLabel("Department:"));
        departmentComboBox = new JComboBox<>(new String[]{"", "pilot", "technician", "maintenance", "dispatcher", "service"});
        add(departmentComboBox);

        JButton getEmployeeButton = new JButton("Get Employee");
        getEmployeeButton.addActionListener(new EmployeeButtonListener());
        add(getEmployeeButton);

        JButton getBrigadeHeadsButton = new JButton("Get Brigade Heads");
        getBrigadeHeadsButton.addActionListener(new BrigadeHeadsButtonListener());
        add(getBrigadeHeadsButton);
    }

    private class EmployeeButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            EmployeeFilter filter = createFilter();

            String query = queryBuilder.getEmployeesListQuery(filter);
            String countQuery = queryBuilder.getEmployeesCountQuery(filter);
            System.out.println(query);

            try {
                List<Employee> employees = queryBuilder.executeQuery(query);
                int rowCount = queryBuilder.executeCountQuery(countQuery);
                showResults(employees, rowCount);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(EmployeeFilterGUI.this,
                        "Error executing query: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class BrigadeHeadsButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            EmployeeFilter filter = createFilter();

            String query = queryBuilder.getDepartmentHeadsListQuery(filter);
            String countQuery = queryBuilder.getDepartmentHeadsCountQuery(filter);
            System.out.println(query);

            try {
                List<Employee> employees = queryBuilder.executeQuery(query);
                int rowCount = queryBuilder.executeCountQuery(countQuery);
                showResults(employees, rowCount);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(EmployeeFilterGUI.this,
                        "Error executing query: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private EmployeeFilter createFilter() {
        EmployeeFilter filter = new EmployeeFilter();
        filter.setAirport(parseInt(airportField.getText()));
        filter.setGender((String) genderComboBox.getSelectedItem());
        filter.setMinAge(parseInt(minAgeField.getText()));
        filter.setMaxAge(parseInt(maxAgeField.getText()));
        filter.setMinChildren(parseInt(minChildrenField.getText()));
        filter.setMaxChildren(parseInt(maxChildrenField.getText()));
        filter.setMinSalary(parseInt(minSalaryField.getText()));
        filter.setMaxSalary(parseInt(maxSalaryField.getText()));
        filter.setMinExperience(parseInt(minExperienceField.getText()));
        filter.setMaxExperience(parseInt(maxExperienceField.getText()));
        filter.setDepartmentTitle((String) departmentComboBox.getSelectedItem());
        return filter;
    }

    private void showResults(List<Employee> employees, int rowCount) {
        JFrame resultFrame = new JFrame("Query Results");
        resultFrame.setSize(800, 600);
        resultFrame.setLayout(new BorderLayout());

        String[] columnNames = {
                "ID", "Airport", "Department", "Brigade", "Salary", "Department Title",
                "Employment Date", "Experience", "Full Name", "Gender", "Age", "Phone Number", "Children"
        };

        Object[][] data = new Object[employees.size()][13];
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            data[i][0] = emp.getId();
            data[i][1] = emp.getAirport();
            data[i][2] = emp.getDepartment();
            data[i][3] = emp.getBrigade();
            data[i][4] = emp.getSalary();
            data[i][5] = emp.getDepartmentTitle();
            data[i][6] = emp.getEmploymentDate();
            data[i][7] = emp.getExperience();
            data[i][8] = emp.getFullName();
            data[i][9] = emp.getGender();
            data[i][10] = emp.getAge();
            data[i][11] = emp.getPhoneNumber();
            data[i][12] = emp.getChildren();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        resultFrame.add(scrollPane, BorderLayout.CENTER);

        JLabel rowCountLabel = new JLabel("Total Rows: " + rowCount);
        resultFrame.add(rowCountLabel, BorderLayout.SOUTH);

        resultFrame.setVisible(true);
    }

    private Integer parseInt(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
