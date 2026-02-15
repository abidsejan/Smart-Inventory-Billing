package com.inventory.ui;

import com.inventory.service.AuthenticationService;

import javax.swing.*;
import java.awt.*;

public class RegistrationFrame extends JDialog {

    private final AuthenticationService authService;
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Staff", "Admin"});


    public RegistrationFrame(Frame owner) {
        super(owner, "User Registration", true);
        this.authService = new AuthenticationService();

        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getOwner());

        // Main panel with a modern layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title
        JLabel titleLabel = new JLabel("Create a New Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, 300)); // Constrain form size
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:", SwingConstants.LEFT);
        JLabel passLabel = new JLabel("Password:", SwingConstants.LEFT);
        JLabel confirmPassLabel = new JLabel("Confirm:", SwingConstants.LEFT);
        JLabel roleLabel = new JLabel("Role:", SwingConstants.LEFT);


        // Using FlatLAF specific properties for placeholder text
        usernameField.putClientProperty("JTextField.placeholderText", "Enter your username");
        passwordField.putClientProperty("JPasswordField.placeholderText", "Enter your password");
        confirmPasswordField.putClientProperty("JPasswordField.placeholderText", "Confirm your password");

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(confirmPassLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(roleComboBox, gbc);


        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        registerButton.putClientProperty("JButton.buttonType", "roundRect");
        cancelButton.putClientProperty("JButton.buttonType", "roundRect");

        buttonsPanel.add(registerButton);
        buttonsPanel.add(cancelButton);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(buttonsPanel);

        // Action Listeners
        registerButton.addActionListener(e -> handleRegister());
        cancelButton.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(registerButton);

        add(mainPanel);
        pack();
        setResizable(false);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();


        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = authService.register(username, password, role);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose another one.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
