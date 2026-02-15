package com.inventory.dao;

import com.inventory.model.Bill;
import com.inventory.model.BillItem;
import com.inventory.model.Item;
import com.inventory.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BillDAO {

    public int saveBill(Bill bill) {
        String sql = "INSERT INTO bills (user_id, bill_date, subtotal, discount, tax, total) VALUES (?, ?, ?, ?, ?, ?)";
        int billId = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, bill.getUserId());
            pstmt.setTimestamp(2, new Timestamp(bill.getBillDate().getTime()));
            pstmt.setDouble(3, bill.getSubtotal());
            pstmt.setDouble(4, bill.getDiscount());
            pstmt.setDouble(5, bill.getTax());
            pstmt.setDouble(6, bill.getTotal());

            if (pstmt.executeUpdate() > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    billId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billId;
    }

    public void saveBillItem(BillItem billItem) {
        String sql = "INSERT INTO bill_items (bill_id, item_id, quantity, price_per_item) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billItem.getBillId());
            pstmt.setInt(2, billItem.getItem().getId());
            pstmt.setInt(3, billItem.getQuantity());
            pstmt.setDouble(4, billItem.getPricePerItem());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Bill> getBillsByDate(java.util.Date date) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE DATE(bill_date) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setUserId(rs.getInt("user_id"));
                bill.setBillDate(rs.getTimestamp("bill_date"));
                bill.setSubtotal(rs.getDouble("subtotal"));
                bill.setDiscount(rs.getDouble("discount"));
                bill.setTax(rs.getDouble("tax"));
                bill.setTotal(rs.getDouble("total"));
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setUserId(rs.getInt("user_id"));
                bill.setBillDate(rs.getTimestamp("bill_date"));
                bill.setSubtotal(rs.getDouble("subtotal"));
                bill.setDiscount(rs.getDouble("discount"));
                bill.setTax(rs.getDouble("tax"));
                bill.setTotal(rs.getDouble("total"));
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    public Map<String, Integer> getTopSellingItems(int limit) {
        Map<String, Integer> topItems = new LinkedHashMap<>(); // LinkedHashMap to maintain order
        String sql = "SELECT i.name, SUM(bi.quantity) AS total_quantity " +
                     "FROM bill_items bi " +
                     "JOIN items i ON bi.item_id = i.id " +
                     "GROUP BY i.name " +
                     "ORDER BY total_quantity DESC " +
                     "LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                topItems.put(rs.getString("name"), rs.getInt("total_quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topItems;
    }

    public Map<String, Integer> getTopSellingItemsForDate(java.util.Date date, int limit) {
        Map<String, Integer> topItems = new LinkedHashMap<>();
        String sql = "SELECT i.name, SUM(bi.quantity) AS total_quantity " +
                     "FROM bill_items bi " +
                     "JOIN items i ON bi.item_id = i.id " +
                     "JOIN bills b ON bi.bill_id = b.id " +
                     "WHERE DATE(b.bill_date) = ? " +
                     "GROUP BY i.name " +
                     "ORDER BY total_quantity DESC " +
                     "LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                topItems.put(rs.getString("name"), rs.getInt("total_quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topItems;
    }

    public List<BillItem> getBillItemsByBillId(int billId) {
        List<BillItem> billItems = new ArrayList<>();
        String sql = "SELECT bi.id, bi.quantity, bi.price_per_item, i.id as item_id, i.name, i.category, i.price, i.supplier, i.stock " +
                     "FROM bill_items bi JOIN items i ON bi.item_id = i.id WHERE bi.bill_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, billId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs.getInt("item_id"), rs.getString("name"), rs.getString("category"),
                                     rs.getDouble("price"), rs.getString("supplier"), rs.getInt("stock"));
                BillItem billItem = new BillItem(rs.getInt("id"), billId, item, rs.getInt("quantity"),
                                                 rs.getDouble("price_per_item"));
                billItems.add(billItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billItems;
    }
}
