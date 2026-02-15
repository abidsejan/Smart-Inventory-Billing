package com.inventory.ui;

import com.inventory.model.Item;
import com.inventory.service.InventoryService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ItemFormController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField supplierField;

    private InventoryService inventoryService;
    private ItemController itemController;
    private Item itemToEdit;
    private Stage stage;

    public void initialize() {
        inventoryService = new InventoryService();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setItemController(ItemController itemController) {
        this.itemController = itemController;
    }

    public void setItemToEdit(Item item) {
        this.itemToEdit = item;
        nameField.setText(item.getName());
        priceField.setText(String.valueOf(item.getPrice()));
        quantityField.setText(String.valueOf(item.getStock()));
        categoryField.setText(item.getCategory());
        supplierField.setText(item.getSupplier());
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        String category = categoryField.getText();
        String supplier = supplierField.getText();

        if (itemToEdit == null) {
            // Add new item
            Item newItem = new Item(0, name, category, price, supplier, quantity);
            inventoryService.addItem(newItem);
        } else {
            // Update existing item
            itemToEdit.setName(name);
            itemToEdit.setPrice(price);
            itemToEdit.setStock(quantity);
            itemToEdit.setCategory(category);
            itemToEdit.setSupplier(supplier);
            inventoryService.updateItem(itemToEdit);
        }
        itemController.initialize(); // Refresh the table
        stage.close();
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }
}
