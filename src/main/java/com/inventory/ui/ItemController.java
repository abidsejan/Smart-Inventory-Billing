package com.inventory.ui;

import com.inventory.model.Item;
import com.inventory.service.InventoryService;
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

public class ItemController {

    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, Integer> idColumn;
    @FXML
    private TableColumn<Item, String> nameColumn;
    @FXML
    private TableColumn<Item, Double> priceColumn;
    @FXML
    private TableColumn<Item, Integer> quantityColumn;

    private InventoryService inventoryService;

    public void initialize() {
        inventoryService = new InventoryService();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        loadItems();
    }

    private void loadItems() {
        List<Item> items = inventoryService.getAllItems();
        itemTable.setItems(FXCollections.observableArrayList(items));
    }

    @FXML
    private void handleAddItem() {
        showItemForm(null);
    }

    @FXML
    private void handleEditItem() {
        Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            showItemForm(selectedItem);
        } else {
            // Show error message
            System.out.println("No item selected for editing");
        }
    }

    @FXML
    private void handleDeleteItem() {
        Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            inventoryService.deleteItem(selectedItem.getId());
            loadItems();
        } else {
            // Show error message
            System.out.println("No item selected for deletion");
        }
    }

    private void showItemForm(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ItemForm.fxml"));
            Parent root = loader.load();

            ItemFormController controller = loader.getController();
            controller.setItemController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(item == null ? "Add Item" : "Edit Item");
            stage.setScene(new Scene(root));

            controller.setStage(stage);
            if (item != null) {
                controller.setItemToEdit(item);
            }

            stage.showAndWait();
            loadItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

