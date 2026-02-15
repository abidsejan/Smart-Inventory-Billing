package com.inventory.ui;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserFormController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;

    private UserDAO userDAO;
    private UserManagementController userManagementController;
    private User userToEdit;
    private Stage stage;

    public void initialize() {
        userDAO = new UserDAO();
        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "Staff"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUserManagementController(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
    }

    public void setUserToEdit(User user) {
        this.userToEdit = user;
        usernameField.setText(user.getUsername());
        passwordField.setText(""); // Clear password field for security
        roleComboBox.getSelectionModel().select(user.getRole());
    }

    @FXML
    private void handleSave() {
        String username = usernameField.getText();
        String password = passwordField.getText(); // New password, might be empty
        String role = roleComboBox.getSelectionModel().getSelectedItem();

        if (userToEdit == null) {
            // Add new user
            User newUser = new User(0, username, password, role);
            userDAO.save(newUser);
        } else {
            // Update existing user
            userToEdit.setUsername(username);
            if (!password.isEmpty()) { // Only update password if a new one is provided
                userToEdit.setPassword(password);
            }
            userToEdit.setRole(role);
            userDAO.updateUser(userToEdit);
        }
        userManagementController.initialize(); // Refresh the table
        stage.close();
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }
}
