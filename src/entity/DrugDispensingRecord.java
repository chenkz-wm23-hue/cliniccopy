package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DrugDispensingRecord {
    private String dispensingId;
    private String patientId;
    private String patientName;
    private String drugId;
    private String drugName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private LocalDateTime dispensingDateTime;
    private String prescriptionId; // Links to treatment record

    public DrugDispensingRecord(String patientId, String patientName, String drugId, 
                               String drugName, int quantity, double unitPrice, 
                               LocalDateTime dispensingDateTime, String prescriptionId) {
        this.dispensingId = generateDispensingId();
        this.patientId = patientId;
        this.patientName = patientName;
        this.drugId = drugId;
        this.drugName = drugName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
        this.dispensingDateTime = dispensingDateTime;
        this.prescriptionId = prescriptionId;
    }

    private String generateDispensingId() {
        return "DISP" + System.currentTimeMillis() % 10000;
    }

    // Getters
    public String getDispensingId() { return dispensingId; }
    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getDrugId() { return drugId; }
    public String getDrugName() { return drugName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDateTime getDispensingDateTime() { return dispensingDateTime; }
    public String getPrescriptionId() { return prescriptionId; }
    public String getDispensingDate() { 
        return dispensingDateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")); 
    }
    public String getDispensingTime() { 
        return dispensingDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")); 
    }

    @Override
    public String toString() {
        return String.format("Dispensing ID: %s | Patient: %s | Drug: %s | Qty: %d | Date: %s",
                           dispensingId, patientName, drugName, quantity, getDispensingDate());
    }
}