package com.haskov.gui.crud;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeCreateUpdateGUI extends JFrame {
    private JTextField idField;
    private JTextField airportField;
    private JTextField departmentField;
    private JTextField brigadeField;
    private JTextField salaryField;
    private JTextField departmentTitleField;
    private JTextField employmentDateField;
    private JTextField experienceField;
    private JTextField fullNameField;
    private JTextField genderField;
    private JTextField ageField;
    private JTextField phoneNumberField;
    private JTextField childrenField;

    private Integer employeeId;

    public EmployeeCreateUpdateGUI(Integer employeeId) {
        this.employeeId = employeeId;
        setupUI();
        if (employeeId != null) {
            loadEmployeeData(employeeId);
        }
    }

    private void setupUI() {
        setTitle(employeeId == null ? "Create Employee" : "Update Employee");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(14, 2));

        add(new JLabel("ID:"));
        idField = new JTextField();
        idField.setEnabled(false);
        add(idField);

        add(new JLabel("Airport:"));
        airportField = new JTextField();
        add(airportField);

        add(new JLabel("Department:"));
        departmentField = new JTextField();
        add(departmentField);

        add(new JLabel("Brigade:"));
        brigadeField = new JTextField();
        add(brigadeField);

        add(new JLabel("Salary:"));
        salaryField = new JTextField();
        add(salaryField);

        add(new JLabel("Department Title:"));
        departmentTitleField = new JTextField();
        add(departmentTitleField);

        add(new JLabel("Employment Date:"));
        employmentDateField = new JTextField();
        add(employmentDateField);

        add(new JLabel("Experience:"));
        experienceField = new JTextField();
        add(experienceField);

        add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        add(fullNameField);

        add(new JLabel("Gender:"));
        genderField = new JTextField();
        add(genderField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField();
        add(phoneNumberField);

        add(new JLabel("Children:"));
        childrenField = new JTextField();
        add(childrenField);

        JButton saveButton = new JButton(employeeId == null ? "Create" : "Update");
        saveButton.addActionListener(new SaveButtonListener());
        add(saveButton);
    }

    private void loadEmployeeData(int employeeId) {
        String url = "jdbc:your_database_url";
        String user = "your_database_user";
        String password = "your_database_password";

        String query = "SELECT * FROM employee WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idField.setText(String.valueOf(rs.getInt("id")));
                    airportField.setText(rs.getString("airport"));
                    departmentField.setText(rs.getString("department"));
                    brigadeField.setText(rs.getString("brigade"));
                    salaryField.setText(rs.getString("salary"));
                    departmentTitleField.setText(rs.getString("department_title"));
                    employmentDateField.setText(rs.getString("employment_date"));
                    experienceField.setText(rs.getString("experience"));
                    fullNameField.setText(rs.getString("full_name"));
                    genderField.setText(rs.getString("gender"));
                    ageField.setText(rs.getString("age"));
                    phoneNumberField.setText(rs.getString("phone_number"));
                    childrenField.setText(rs.getString("children"));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String url = "jdbc:your_database_url";
            String user = "your_database_user";
            String password = "your_database_password";

            String query = employeeId == null ?
                    "INSERT INTO employee (airport, department, brigade, salary, department_title, employment_date, experience, full_name, gender, age, phone_number, children) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" :
                    "UPDATE employee SET airport = ?, department = ?, brigade = ?, salary = ?, department_title = ?, employment_date = ?, experience = ?, full_name = ?, gender = ?, age = ?, phone_number = ?, children = ? WHERE id = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, airportField.getText());
                stmt.setString(2, departmentField.getText());
                stmt.setString(3, brigadeField.getText());
                stmt.setString(4, salaryField.getText());
                stmt.setString(5, departmentTitleField.getText());
                stmt.setString(6, employmentDateField.getText());
                stmt.setString(7, experienceField.getText());
                stmt.setString(8, fullNameField.getText());
                stmt.setString(9, genderField.getText());
                stmt.setString(10, ageField.getText());
                stmt.setString(11, phoneNumberField.getText());
                stmt.setString(12, childrenField.getText());

                if (employeeId != null) {
                    stmt.setInt(13, employeeId);
                }

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(EmployeeCreateUpdateGUI.this, "Employee saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(EmployeeCreateUpdateGUI.this, "Error saving employee data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeCreateUpdateGUI gui = new EmployeeCreateUpdateGUI(null);
            gui.setVisible(true);
        });
    }
}
