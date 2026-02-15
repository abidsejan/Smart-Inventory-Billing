package com.inventory.ui;

import com.inventory.model.Bill;
import com.inventory.model.Item;
import com.inventory.service.ReportService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportController {

    @FXML
    private ComboBox<String> reportTypeComboBox;
    @FXML
    private TableView reportTable;

    private ReportService reportService;

    public void initialize() {
        reportService = new ReportService();
        reportTypeComboBox.setItems(FXCollections.observableArrayList(
                "Daily Sales Report", "Top Selling Items", "Low Stock Items"));
    }

    @FXML
    private void handleGenerateReport() {
        String selectedReport = reportTypeComboBox.getSelectionModel().getSelectedItem();
        if (selectedReport == null) {
            return;
        }

        reportTable.getColumns().clear();
        reportTable.getItems().clear();

        switch (selectedReport) {
            case "Daily Sales Report":
                generateDailySalesReport();
                break;
            case "Top Selling Items":
                generateTopSellingItemsReport();
                break;
            case "Low Stock Items":
                generateLowStockItemsReport();
                break;
        }
    }

    private void generateDailySalesReport() {
        TableColumn<Bill, Integer> idColumn = new TableColumn<>("Bill ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Bill, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("billDate"));

        TableColumn<Bill, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        reportTable.getColumns().addAll(idColumn, dateColumn, totalColumn);
        List<Bill> dailySales = reportService.getDailySalesReport(new Date());
        reportTable.setItems(FXCollections.observableArrayList(dailySales));
    }

    private void generateTopSellingItemsReport() {
        TableColumn<Map.Entry<String, Integer>, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKey()));

        TableColumn<Map.Entry<String, Integer>, Integer> quantityColumn = new TableColumn<>("Quantity Sold");
        quantityColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        reportTable.getColumns().addAll(itemColumn, quantityColumn);
        Map<String, Integer> topSellingItems = reportService.getTopSellingItems(10);
        reportTable.setItems(FXCollections.observableArrayList(topSellingItems.entrySet()));
    }

    private void generateLowStockItemsReport() {
        TableColumn<Item, String> nameColumn = new TableColumn<>("Item Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        reportTable.getColumns().addAll(nameColumn, stockColumn);
        List<Item> lowStockItems = reportService.getLowStockItems(10);
        reportTable.setItems(FXCollections.observableArrayList(lowStockItems));
    }
}
