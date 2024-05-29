package com.haskov.gui;

import com.haskov.gui.brigades.BrigadeFilterGUI;
import com.haskov.gui.crud.EmployeeCreateUpdateGUI;
import com.haskov.gui.employees.EmployeeFilterGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {

    public MainGUI() {
        setTitle("Main Window");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 3));  // 5 строк по 3 кнопки

        // Создание 13 кнопок
        for (int i = 1; i <= 13; i++) {
            JButton button = new JButton("Button " + i);
            button.addActionListener(new ButtonClickListener(i));
            add(button);
        }

        // Создание кнопок для CRUD операций
        JButton createButton = new JButton("Create Employee");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    EmployeeCreateUpdateGUI createGUI = new EmployeeCreateUpdateGUI(null);
                    createGUI.setVisible(true);
                });
            }
        });

        JButton updateButton = new JButton("Update Employee");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog(MainGUI.this, "Enter Employee ID to update:");
                if (idStr != null && !idStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    SwingUtilities.invokeLater(() -> {
                        EmployeeCreateUpdateGUI updateGUI = new EmployeeCreateUpdateGUI(id);
                        updateGUI.setVisible(true);
                    });
                }
            }
        });

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog(MainGUI.this, "Enter Employee ID to delete:");
                if (idStr != null && !idStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    // Здесь добавьте вызов метода для удаления сотрудника по ID
                    // Например: employeeService.deleteEmployee(id);
                }
            }
        });

        add(createButton);
        add(updateButton);
        add(deleteButton);
    }

    private class ButtonClickListener implements ActionListener {
        private int buttonNumber;

        public ButtonClickListener(int buttonNumber) {
            this.buttonNumber = buttonNumber;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (buttonNumber) {
                case 1:
                    SwingUtilities.invokeLater(() -> {
                        EmployeeFilterGUI employeeFilterGUI = new EmployeeFilterGUI();
                        employeeFilterGUI.setVisible(true);
                    });
                    break;
                case 2:
                    SwingUtilities.invokeLater(() -> {
                        BrigadeFilterGUI brigadeFilterGUI = new BrigadeFilterGUI();
                        brigadeFilterGUI.setVisible(true);
                    });
                    break;
                default:
                    JOptionPane.showMessageDialog(MainGUI.this, "Button " + buttonNumber + " clicked");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello world");
        SwingUtilities.invokeLater(() -> {
            MainGUI mainGUI = new MainGUI();
            mainGUI.setVisible(true);
        });
    }
}
