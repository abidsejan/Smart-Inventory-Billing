package com.inventory.model;

public class BillItem {
    private int id;
    private int billId;
    private Item item;
    private int quantity;
    private double pricePerItem;

    public BillItem() {
    }

    public BillItem(int id, int billId, Item item, int quantity, double pricePerItem) {
        this.id = id;
        this.billId = billId;
        this.item = item;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(double pricePerItem) {
        this.pricePerItem = pricePerItem;
    }
}
