package com.inventory.service;

import com.inventory.dao.BillDAO;
import com.inventory.dao.ItemDAO;
import com.inventory.model.Bill;
import com.inventory.model.BillItem;
import com.inventory.model.Item;
import com.inventory.util.OutOfStockException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BillingService {
    private final BillDAO billDAO;
    private final ItemDAO itemDAO;

    public BillingService() {
        this.billDAO = new BillDAO();
        this.itemDAO = new ItemDAO();
    }

    public Bill createBill(int userId, Map<Item, Integer> cart, double discount, double tax) throws OutOfStockException {
        // First, check for stock availability
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            if (item.getStock() < quantity) {
                throw new OutOfStockException("Not enough stock for item: " + item.getName());
            }
        }

        // Calculate totals
        double subtotal = 0;
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }

        double discountAmount = subtotal * (discount / 100);
        double taxAmount = (subtotal - discountAmount) * (tax / 100);
        double total = subtotal - discountAmount + taxAmount;

        // Create and save the bill
        Bill bill = new Bill(0, userId, new Date(), subtotal, discountAmount, taxAmount, total);
        int billId = billDAO.saveBill(bill);
        bill.setId(billId);

        // Save bill items and update stock
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();

            BillItem billItem = new BillItem(0, billId, item, quantity, item.getPrice());
            billDAO.saveBillItem(billItem);
            itemDAO.updateStock(item.getId(), quantity);
        }

        return bill;
    }

    public void generateBillPdf(Bill bill) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Bill_" + bill.getId() + ".pdf"));
            document.open();

            // Title
            Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Invoice", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Add some space

            // Bill Details
            document.add(new Paragraph("Bill ID: " + bill.getId()));
            document.add(new Paragraph("Date: " + bill.getBillDate()));
            document.add(new Paragraph("User ID: " + bill.getUserId()));
            document.add(new Paragraph(" "));

            // Items Table
            PdfPTable table = new PdfPTable(4); // 4 columns for item, quantity, price, total
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            table.addCell(new PdfPCell(new Phrase("Item Name")));
            table.addCell(new PdfPCell(new Phrase("Quantity")));
            table.addCell(new PdfPCell(new Phrase("Price/Item")));
            table.addCell(new PdfPCell(new Phrase("Total")));

            // Add items to table
            List<BillItem> billItems = billDAO.getBillItemsByBillId(bill.getId()); // Assuming this method exists
            for (BillItem item : billItems) {
                table.addCell(item.getItem().getName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.valueOf(item.getPricePerItem()));
                table.addCell(String.valueOf(item.getQuantity() * item.getPricePerItem()));
            }
            document.add(table);

            // Totals
            document.add(new Paragraph("Subtotal: " + bill.getSubtotal()));
            document.add(new Paragraph("Discount: " + bill.getDiscount()));
            document.add(new Paragraph("Tax: " + bill.getTax()));
            document.add(new Paragraph("Total: " + bill.getTotal()));

            document.close();
            System.out.println("Bill PDF generated successfully.");
        } catch (DocumentException | java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
