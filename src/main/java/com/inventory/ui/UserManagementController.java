package com.inventory.ui;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UserManagementController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;

    private UserDAO userDAO;

    public void initialize() {
        userDAO = new UserDAO();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        loadUsers();
    }

    private void loadUsers() {
        List<User> users = userDAO.getAllUsers();
        userTable.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    private void handleAddNewUser() {
        showUserForm(null);
    }

    @FXML
    private void handleEditUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            showUserForm(selectedUser);
        } else {
            // Show error message
            System.out.println("No user selected for editing");
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userDAO.deleteUser(selectedUser.getId());
            loadUsers();
        } else {
            // Show error message
            System.out.println("No user selected for deletion");
        }
    }

    private void showUserForm(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserForm.fxml"));
            Parent root = loader.load();

            UserFormController controller = loader.getController();
            controller.setUserManagementController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(user == null ? "Add User" : "Edit User");
            stage.setScene(new Scene(root));

            controller.setStage(stage);
            if (user != null) {
                controller.setUserToEdit(user);
            }

            stage.showAndWait();
            loadUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
