package com.inventory.ui;

import com.inventory.dao.BillDAO;
import com.inventory.service.InventoryService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label totalItemsLabel;
    @FXML
    private Label totalBillsLabel;
    @FXML
    private Label lowStockItemsLabel;

    private InventoryService inventoryService;
    private BillDAO billDAO;

    public void initialize() {
        inventoryService = new InventoryService();
        billDAO = new BillDAO();

        int totalItems = inventoryService.getAllItems().size();
        totalItemsLabel.setText(String.valueOf(totalItems));

        int totalBills = billDAO.getAllBills().size();
        totalBillsLabel.setText(String.valueOf(totalBills));

        int lowStockItems = inventoryService.getLowStockItems(10).size();
        lowStockItemsLabel.setText(String.valueOf(lowStockItems));
    }
}
