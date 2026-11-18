
package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrescriptionRecord {
    private String prescriptionId;
    private String consultationId;
    private String doctorId;
    private String doctorName;
    private String patientId;
    private String patientName;
    private String drugId;
    private String drugName;
    private int quantity;
    private String dosage;
    private String instructions;
    private LocalDateTime prescriptionDateTime;
    private boolean dispensed;

    public PrescriptionRecord(String consultationId, String doctorId, String doctorName,
                             String patientId, String patientName, String drugId, 
                             String drugName, int quantity, String dosage, 
                             String instructions, LocalDateTime prescriptionDateTime) {
        this.prescriptionId = generatePrescriptionId();
        this.consultationId = consultationId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.drugId = drugId;
        this.drugName = drugName;
        this.quantity = quantity;
        this.dosage = dosage;
        this.instructions = instructions;
        this.prescriptionDateTime = prescriptionDateTime;
        this.dispensed = false;
    }

    private String generatePrescriptionId() {
        return "PRESC" + System.currentTimeMillis() % 10000;
    }

    // Getters
    public String getPrescriptionId() { return prescriptionId; }
    public String getConsultationId() { return consultationId; }
    public String getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getDrugId() { return drugId; }
    public String getDrugName() { return drugName; }
    public int getQuantity() { return quantity; }
    public String getDosage() { return dosage; }
    public String getInstructions() { return instructions; }
    public LocalDateTime getPrescriptionDateTime() { return prescriptionDateTime; }
    public boolean isDispensed() { return dispensed; }
    public String getPrescriptionDate() { 
        return prescriptionDateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")); 
    }

    // Setters
    public void setDispensed(boolean dispensed) { this.dispensed = dispensed; }

    @Override
    public String toString() {
        return String.format("Prescription ID: %s | Doctor: %s | Patient: %s | Drug: %s | Qty: %d",
                           prescriptionId, doctorName, patientName, drugName, quantity);
    }
}