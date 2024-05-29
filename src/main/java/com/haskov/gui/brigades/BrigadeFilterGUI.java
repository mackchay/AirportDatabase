package com.haskov.gui.brigades;

import com.haskov.sql.brigades.BrigadeEmployee;
import com.haskov.sql.brigades.BrigadeFilter;
import com.haskov.sql.brigades.BrigadesQueryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class BrigadeFilterGUI extends JFrame {

    private JTextField airportField;
    private JTextField brigadeField;
    private JComboBox<String> departmentComboBox;
    private JTextField flightField;
    private JTextField minAgeField;
    private JTextField maxAgeField;
    private JTextField minAvgSalaryField;
    private JTextField maxAvgSalaryField;
    private JTextField minTotalSalaryField;
    private JTextField maxTotalSalaryField;

    private BrigadesQueryBuilder queryBuilder;

    public BrigadeFilterGUI() {
        queryBuilder = new BrigadesQueryBuilder();

        setTitle("Brigade Filter");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(11, 2));

        add(new JLabel("Airport:"));
        airportField = new JTextField();
        add(airportField);

        add(new JLabel("Brigade:"));
        brigadeField = new JTextField();
        add(brigadeField);

        add(new JLabel("Department:"));
        departmentComboBox = new JComboBox<>(new String[]{"", "pilot", "technician", "maintenance", "dispatcher", "cashier"});
        add(departmentComboBox);

        add(new JLabel("Flight:"));
        flightField = new JTextField();
        add(flightField);

        add(new JLabel("Min Age:"));
        minAgeField = new JTextField();
        add(minAgeField);

        add(new JLabel("Max Age:"));
        maxAgeField = new JTextField();
        add(maxAgeField);

        add(new JLabel("Min Avg Salary:"));
        minAvgSalaryField = new JTextField();
        add(minAvgSalaryField);

        add(new JLabel("Max Avg Salary:"));
        maxAvgSalaryField = new JTextField();
        add(maxAvgSalaryField);

        add(new JLabel("Min Total Salary:"));
        minTotalSalaryField = new JTextField();
        add(minTotalSalaryField);

        add(new JLabel("Max Total Salary:"));
        maxTotalSalaryField = new JTextField();
        add(maxTotalSalaryField);

        JButton getBrigadeButton = new JButton("Get Brigade");
        getBrigadeButton.addActionListener(new BrigadeButtonListener());
        add(getBrigadeButton);
    }

    private class BrigadeButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            BrigadeFilter filter = createFilter();

            String query = queryBuilder.getEmployeesInBrigadeListQuery(filter);
            String countQuery = queryBuilder.getEmployeesInBrigadeCount(filter);
            System.out.println(query);

            try {
                List<BrigadeEmployee> employees = queryBuilder.executeQuery(query);
                int rowCount = queryBuilder.executeCountQuery(countQuery);
                showResults(employees, rowCount);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(BrigadeFilterGUI.this,
                        "Error executing query: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private BrigadeFilter createFilter() {
        BrigadeFilter filter = new BrigadeFilter();
        filter.setAirport(parseInt(airportField.getText()));
        filter.setBrigade(parseInt(brigadeField.getText()));
        filter.setDepartmentTitle((String) departmentComboBox.getSelectedItem());
        filter.setFlight(parseInt(flightField.getText()));
        filter.setMinAge(parseInt(minAgeField.getText()));
        filter.setMaxAge(parseInt(maxAgeField.getText()));
        filter.setMinAvgSalary(parseInt(minAvgSalaryField.getText()));
        filter.setMaxAvgSalary(parseInt(maxAvgSalaryField.getText()));
        filter.setMinTotalSalary(parseInt(minTotalSalaryField.getText()));
        filter.setMaxTotalSalary(parseInt(maxTotalSalaryField.getText()));
        return filter;
    }

    private void showResults(List<BrigadeEmployee> employees, int rowCount) {
        JFrame resultFrame = new JFrame("Brigade Query Results");
        resultFrame.setSize(800, 600);
        resultFrame.setLayout(new BorderLayout());

        String[] columnNames = {
                "ID", "Airport", "Department", "Brigade", "Salary", "Department Title",
                "Employment Date", "Experience", "Full Name", "Gender", "Age", "Phone Number", "Children",
                "Avg Salary", "Total Salary"
        };

        Object[][] data = new Object[employees.size()][16];
        for (int i = 0; i < employees.size(); i++) {
            BrigadeEmployee emp = employees.get(i);
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
            data[i][13] = emp.getAvgSalary();
            data[i][14] = emp.getTotalSalary();
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
