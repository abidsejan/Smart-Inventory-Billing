package com.inventory.service;

import com.inventory.dao.BillDAO;
import com.inventory.dao.ItemDAO;
import com.inventory.model.Bill;
import com.inventory.model.Item;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportService {

    private final ItemDAO itemDAO;
    private final BillDAO billDAO;

    public ReportService() {
        this.itemDAO = new ItemDAO();
        this.billDAO = new BillDAO();
    }

    /**
     * Gets a list of all bills recorded on a specific date.
     * @param date The date for the report.
     * @return A list of Bill objects.
     */
    public List<Bill> getDailySalesReport(Date date) {
        return billDAO.getBillsByDate(date);
    }

    /**
     * Gets the top selling items.
     * @param limit The number of top items to return.
     * @return A map of item names to the total quantity sold.
     */
    public Map<String, Integer> getTopSellingItems(int limit) {
        return billDAO.getTopSellingItems(limit);
    }

    /**
     * Gets a list of items that are low in stock.
     * @param threshold The stock level to be considered "low".
     * @return A list of Item objects.
     */
    public List<Item> getLowStockItems(int threshold) {
        return itemDAO.getLowStockItems(threshold);
    }
}