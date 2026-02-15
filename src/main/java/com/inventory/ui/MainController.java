package com.inventory.ui;

import com.inventory.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label statusLabel;

    @FXML
    private Button userManagementButton;

    private User loggedInUser;

    public void initialize() {
        // Initialize the main view, e.g., load the dashboard by default
        statusLabel.setText("Welcome to the Smart Inventory & Billing System!");
        showDashboard();
    }

    public void setUser(User user) {
        this.loggedInUser = user;
        if (loggedInUser != null && "Admin".equals(loggedInUser.getRole())) {
            userManagementButton.setVisible(true);
        } else {
            userManagementButton.setVisible(false);
        }
    }

    @FXML
    private void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardPanel.fxml"));
            Parent dashboardPanel = loader.load();
            mainPane.setCenter(dashboardPanel);
            statusLabel.setText("Dashboard Loaded");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading Dashboard Panel");
        }
    }

    @FXML
    private void showItems() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ItemPanel.fxml"));
            Parent itemPanel = loader.load();
            mainPane.setCenter(itemPanel);
            statusLabel.setText("Items Panel Loaded");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading Items Panel");
        }
    }

    @FXML
    private void showBilling() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BillingPanel.fxml"));
            Parent billingPanel = loader.load();
            mainPane.setCenter(billingPanel);
            statusLabel.setText("Billing Panel Loaded");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading Billing Panel");
        }
    }

    @FXML
    private void showReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportPanel.fxml"));
            Parent reportPanel = loader.load();
            mainPane.setCenter(reportPanel);
            statusLabel.setText("Reports Panel Loaded");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading Reports Panel");
        }
    }

    @FXML
    private void showUserManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserManagementPanel.fxml"));
            Parent userManagementPanel = loader.load();
            mainPane.setCenter(userManagementPanel);
            statusLabel.setText("User Management Panel Loaded");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading User Management Panel");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
