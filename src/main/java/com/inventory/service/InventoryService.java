package com.inventory.service;

import com.inventory.dao.ItemDAO;
import com.inventory.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {
    private final ItemDAO itemDAO;

    public InventoryService() {
        this.itemDAO = new ItemDAO();
    }

    public List<Item> getAllItems() {
        return itemDAO.getAllItems();
    }

    public boolean addItem(Item item) {
        return itemDAO.addItem(item);
    }

    public boolean updateItem(Item item) {
        return itemDAO.updateItem(item);
    }

    public boolean deleteItem(int id) {
        return itemDAO.deleteItem(id);
    }

    public List<Item> searchItems(String query) {
        return getAllItems().stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Item> getLowStockItems(int threshold) {
        return getAllItems().stream()
                .filter(item -> item.getStock() < threshold)
                .collect(Collectors.toList());
    }
}
