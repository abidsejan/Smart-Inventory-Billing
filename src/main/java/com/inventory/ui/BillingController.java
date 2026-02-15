package com.inventory.ui;

import com.inventory.model.Bill;
import com.inventory.model.BillItem;
import com.inventory.model.Item;
import com.inventory.service.BillingService;
import com.inventory.service.InventoryService;
import com.inventory.util.OutOfStockException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.Map;

public class BillingController {

    @FXML
    private ComboBox<Item> itemSearchComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private TableView<BillItem> billTable;
    @FXML
    private TableColumn<BillItem, String> itemNameColumn;
    @FXML
    private TableColumn<BillItem, Double> priceColumn;
    @FXML
    private TableColumn<BillItem, Integer> quantityColumn;
    @FXML
    private TableColumn<BillItem, Double> totalColumn;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private Button printBillButton;

    private InventoryService inventoryService;
    private BillingService billingService;
    private ObservableList<BillItem> billItems = FXCollections.observableArrayList();
    private Map<Item, Integer> cart = new HashMap<>();
    private Bill currentBill;

    public void initialize() {
        inventoryService = new InventoryService();
        billingService = new BillingService();

        itemSearchComboBox.setItems(FXCollections.observableArrayList(inventoryService.getAllItems()));

        itemNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getItem().getName()));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerItem"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPricePerItem() * cellData.getValue().getQuantity()).asObject());

        billTable.setItems(billItems);
        printBillButton.setDisable(true);
    }

    @FXML
    private void handleAddItemToBill() {
        Item selectedItem = itemSearchComboBox.getSelectionModel().getSelectedItem();
        int quantity = Integer.parseInt(quantityField.getText());

        if (selectedItem != null && quantity > 0) {
            if (cart.containsKey(selectedItem)) {
                cart.put(selectedItem, cart.get(selectedItem) + quantity);
            } else {
                cart.put(selectedItem, quantity);
            }
            updateBillTable();
        }
    }

    private void updateBillTable() {
        billItems.clear();
        double totalAmount = 0;
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            double price = item.getPrice();
            billItems.add(new BillItem(0, 0, item, quantity, price));
            totalAmount += price * quantity;
        }
        totalAmountLabel.setText(String.valueOf(totalAmount));
    }

    @FXML
    private void handleGenerateBill() {
        if (cart.isEmpty()) {
            // Show error message
            return;
        }

        try {
            // Assuming a logged-in user with ID 1
            currentBill = billingService.createBill(1, cart, 0, 0);
            // Show success message
            System.out.println("Bill generated successfully with ID: " + currentBill.getId());
            cart.clear();
            updateBillTable();
            printBillButton.setDisable(false);
        } catch (OutOfStockException e) {
            // Show error message
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handlePrintBill() {
        if (currentBill != null) {
            billingService.generateBillPdf(currentBill);
            System.out.println("Bill PDF generated for Bill ID: " + currentBill.getId());
        } else {
            System.out.println("No bill to print.");
        }
    }
}
