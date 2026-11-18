package control;

import adt.LinkedList;
import adt.LinkedListInterface;
import entity.Drug;
import entity.DrugDispensingRecord;
import entity.PrescriptionRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PharmacyControl {
    private LinkedListInterface<Drug> drugList;
    private LinkedList<DrugDispensingRecord> dispensingRecords;
    private LinkedList<PrescriptionRecord> prescriptionRecords;
    private int nextDrugId;

    public PharmacyControl() {
        this.drugList = new LinkedList<>();
        this.dispensingRecords = new LinkedList<>();
        this.prescriptionRecords = new LinkedList<>();
        this.nextDrugId = 1;
        initializeSampleData();
    }

    public boolean addDrug(String name, int quantity, double price, String description, 
                          String category, String expiryDate, int minStockLevel) {
        try {
            String id = generateDrugId();
            Drug newDrug = new Drug(id, name, quantity, price, description, category, expiryDate, minStockLevel);
            drugList.addLast(newDrug);
            return true;
        } catch (Exception e) {
            System.out.println("Error adding drug: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDrug(String drugId) {
        try {
            for (int i = 1; i <= drugList.size(); i++) {
                Drug drug = drugList.getEntry(i);
                if (drug != null && drug.getId().equals(drugId)) {
                    drugList.deleteAt(i);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error deleting drug: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDrug(String drugId, String name, int quantity, double price, 
                             String description, String category, String expiryDate, int minStockLevel) {
        try {
            for (int i = 1; i <= drugList.size(); i++) {
                Drug drug = drugList.getEntry(i);
                if (drug != null && drug.getId().equals(drugId)) {
                    drug.setName(name);
                    drug.setQuantity(quantity);
                    drug.setPrice(price);
                    drug.setDescription(description);
                    drug.setCategory(category);
                    drug.setExpiryDate(expiryDate);
                    drug.setMinStockLevel(minStockLevel);
                    return true;
                }
            } return false;
        } catch (Exception e) {
            System.out.println("Error updating drug: " + e.getMessage());
            return false;
        }
    }

    public Drug searchDrugById(String drugId) {
        try {
            for (int i = 1; i <= drugList.size(); i++) {
                Drug drug = drugList.getEntry(i);
                if (drug != null && drug.getId().equals(drugId)) {
                    return drug;
                }
            } return null;
        } catch (Exception e) {
            System.out.println("Error searching drug: " + e.getMessage());
            return null;
        }
    }

    public Drug searchDrugByName(String drugName) {
        try {
            for (int i = 1; i <= drugList.size(); i++) {
                Drug drug = drugList.getEntry(i);
                if (drug != null && drug.getName().equalsIgnoreCase(drugName)) {
                    return drug;
                }
            } return null;
        } catch (Exception e) {
            System.out.println("Error searching drug: " + e.getMessage());
            return null;
        }
    }
    
    public boolean stockIn(String drugId, int quantity) {
        try {
            Drug drug = searchDrugById(drugId);
            if (drug != null) {
                drug.setQuantity(drug.getQuantity() + quantity);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error adding stock: " + e.getMessage());
            return false;
        }
    }

    public boolean stockOut(String drugId, int quantity) {
        try {
            Drug drug = searchDrugById(drugId);
            if (drug != null && drug.getQuantity() >= quantity) {
                drug.setQuantity(drug.getQuantity() - quantity);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error removing stock: " + e.getMessage());
            return false;
        }
    }
   
     public boolean createPrescription(String consultationId, String doctorId, String doctorName,
                                     String patientId, String patientName, String drugId, 
                                     String drugName, int quantity, String dosage, String instructions) {
        try {
            PrescriptionRecord prescription = new PrescriptionRecord(
                consultationId, doctorId, doctorName, patientId, patientName,
                drugId, drugName, quantity, dosage, instructions, LocalDateTime.now()
            );
            prescriptionRecords.addLast(prescription);
            return true;
        } catch (Exception e) {
            System.out.println("Error creating prescription: " + e.getMessage());
            return false;
        }
    }

    public boolean dispenseDrug(String prescriptionId, String patientId, String patientName,
                               String drugId, String drugName, int quantity, double unitPrice) {
        try {
            Drug drug = searchDrugById(drugId);
            if (drug == null || drug.getQuantity() < quantity) {
                System.out.println("Insufficient stock for drug: " + drugName);
                return false;
            }

            DrugDispensingRecord dispensing = new DrugDispensingRecord(
                patientId, patientName, drugId, drugName, quantity, unitPrice,
                LocalDateTime.now(), prescriptionId
            );
            dispensingRecords.addLast(dispensing);
            stockOut(drugId, quantity);

            for (int i = 1; i <= prescriptionRecords.size(); i++) {
                PrescriptionRecord prescription = prescriptionRecords.getEntry(i);
                if (prescription.getPrescriptionId().equals(prescriptionId)) {
                    prescription.setDispensed(true);
                    break;
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error dispensing drug: " + e.getMessage());
            return false;
        }
    }

    public void generatePatientDrugPurchaseReport() {
        try {
            LocalDateTime now = LocalDateTime.now();
            String generatedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            System.out.println("\n" + "=".repeat(80));
            System.out.println("TAR UMT CLINIC MANAGEMENT SYSTEM");
            System.out.println("PHARMACY SUBSYSTEM");
            System.out.println("PATIENT DRUG PURCHASE REPORT");
            System.out.println("Generated at: " + generatedDateTime);
            System.out.println("=".repeat(80));
            System.out.printf("%-12s | %-15s | %-15s | %-10s | %-12s\n", 
                            "Patient ID", "Patient Name", "Drug Name", "Quantity", "Date");
            System.out.println("-".repeat(80));

            if (dispensingRecords.isEmpty()) {
                System.out.println("No dispensing records found.");
                System.out.println("=".repeat(80));
                return;
            }

            LinkedList<String> uniquePatients = new LinkedList<>();
            int totalDrugsDispensed = 0;

            for (int i = 1; i <= dispensingRecords.size(); i++) {
                DrugDispensingRecord record = dispensingRecords.getEntry(i);
                System.out.printf("%-12s | %-15s | %-15s | %-10d | %-12s\n",
                                record.getPatientId(),
                                record.getPatientName(),
                                record.getDrugName(),
                                record.getQuantity(),
                                record.getDispensingDate());
                if (!containsPatient(uniquePatients, record.getPatientId())) {
                    uniquePatients.addLast(record.getPatientId());
                }
                totalDrugsDispensed += record.getQuantity();
            }

            System.out.println("-".repeat(80));
            System.out.println("Total Patients Served: " + uniquePatients.size());
            System.out.println("Total Drugs Dispensed: " + totalDrugsDispensed);
            System.out.println("=".repeat(80));
            generateDrugQuantityVerticalBarGraph();
        } catch (Exception e) {
            System.out.println("Error generating patient-drug purchase report: " + e.getMessage());
        }
    }

    public void generateDoctorPrescriptionDrugReport() {
        try {
            LocalDateTime now = LocalDateTime.now();
            String generatedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println("\n" + "=".repeat(80));
            System.out.println("TAR UMT CLINIC MANAGEMENT SYSTEM");
            System.out.println("PHARMACY SUBSYSTEM");
            System.out.println("DOCTOR PRESCRIPTION DRUG REPORT");
            System.out.println("Generated at: " + generatedDateTime);
            System.out.println("=".repeat(80));
            System.out.printf("%-20s | %-15s | %-25s\n","Doctor Name", "Drug Name", "Total Quantity Prescribed");
            System.out.println("-".repeat(80));

            if (prescriptionRecords.isEmpty()) {
                System.out.println("No prescription records found.");
                System.out.println("=".repeat(80));
                return;
            }
            LinkedList<DoctorDrugSummary> summaries = aggregateDoctorDrugData();

            for (int i = 1; i <= summaries.size(); i++) {
                DoctorDrugSummary summary = summaries.getEntry(i);
                System.out.printf("%-20s | %-15s | %-25d\n",
                                summary.doctorName,
                                summary.drugName,
                                summary.totalQuantity);
            }
            System.out.println("-".repeat(80));
            System.out.println("Total Prescriptions: " + prescriptionRecords.size());
            System.out.println("=".repeat(80));
            generateDoctorPrescriptionVerticalBarGraph(summaries);
        } catch (Exception e) {
            System.out.println("Error generating doctor-prescription-drug report: " + e.getMessage());
        }
    }
    
    private boolean containsPatient(LinkedList<String> patients, String patientId) {
        for (int i = 1; i <= patients.size(); i++) {
            if (patients.getEntry(i).equals(patientId)) {
                return true;
            }
        }
        return false;
    }

    private LinkedList<DoctorDrugSummary> aggregateDoctorDrugData() {
        LinkedList<DoctorDrugSummary> summaries = new LinkedList<>();

        for (int i = 1; i <= prescriptionRecords.size(); i++) {
            PrescriptionRecord prescription = prescriptionRecords.getEntry(i);
            String key = prescription.getDoctorName() + "|" + prescription.getDrugName();
            boolean found = false;
            for (int j = 1; j <= summaries.size(); j++) {
                DoctorDrugSummary summary = summaries.getEntry(j);
                if (summary.doctorName.equals(prescription.getDoctorName()) && 
                    summary.drugName.equals(prescription.getDrugName())) {
                    summary.totalQuantity += prescription.getQuantity();
                    found = true;
                    break;
                }
            }
            if (!found) {
                summaries.addLast(new DoctorDrugSummary(
                    prescription.getDoctorName(),
                    prescription.getDrugName(),
                    prescription.getQuantity()
                ));
            }
        }
        return summaries;
    }

    private void generateDrugQuantityVerticalBarGraph() {
        System.out.println("\nDRUG QUANTITY VERTICAL BAR GRAPH:");
        System.out.println("X-axis: Drug Names | Y-axis: Total Quantity Dispensed");
        System.out.println("-".repeat(80));

        LinkedList<DrugQuantitySummary> drugSummaries = new LinkedList<>();

        for (int i = 1; i <= dispensingRecords.size(); i++) {
            DrugDispensingRecord record = dispensingRecords.getEntry(i);
            boolean found = false;

            for (int j = 1; j <= drugSummaries.size(); j++) {
                DrugQuantitySummary summary = drugSummaries.getEntry(j);
                if (summary.drugName.equals(record.getDrugName())) {
                    summary.totalQuantity += record.getQuantity();
                    found = true;
                    break;
                }
            }

            if (!found) {
                drugSummaries.addLast(new DrugQuantitySummary(record.getDrugName(), record.getQuantity()));
            }
        }

        int maxQuantity = 0;
        for (int i = 1; i <= drugSummaries.size(); i++) {
            DrugQuantitySummary summary = drugSummaries.getEntry(i);
            if (summary.totalQuantity > maxQuantity) {
                maxQuantity = summary.totalQuantity;
            }
        }

        if (maxQuantity == 0) {
            System.out.println("No data available for graph.");
            return;
        }

        int graphHeight = Math.min(20, maxQuantity);
        int scale = maxQuantity / graphHeight;
        if (scale == 0) scale = 1;

        for (int row = graphHeight; row >= 0; row--) {
            int currentValue = row * scale;
            if (row == graphHeight) {
                System.out.printf("%3d |", maxQuantity);
            } else if (row == 0) {
                System.out.printf("%3d |", 0);
            } else {
                System.out.printf("%3d |", currentValue);
            }
            
            for (int i = 1; i <= drugSummaries.size(); i++) {
                DrugQuantitySummary summary = drugSummaries.getEntry(i);
                int barHeight = summary.totalQuantity / scale;
                
                if (row <= barHeight && row > 0) {
                    System.out.print("  x  ");
                } else {
                    System.out.print("     ");
                }
            }
            System.out.println();
        }

        System.out.print("    +");
        for (int i = 1; i <= drugSummaries.size(); i++) {
            System.out.print("-----");
        }
        System.out.println();
        
        System.out.print("    |");
        for (int i = 1; i <= drugSummaries.size(); i++) {
            DrugQuantitySummary summary = drugSummaries.getEntry(i);
            String label = summary.drugName.length() > 4 ? 
                          summary.drugName.substring(0, 4) : summary.drugName;
            System.out.printf("%-5s", label);
        }
        System.out.println();
        
        System.out.print("    |");
        for (int i = 1; i <= drugSummaries.size(); i++) {
            DrugQuantitySummary summary = drugSummaries.getEntry(i);
            System.out.printf("%-5d", summary.totalQuantity);
        }
        System.out.println();
    }

    private void generateDoctorPrescriptionVerticalBarGraph(LinkedList<DoctorDrugSummary> summaries) {
        System.out.println("\nDOCTOR PRESCRIPTION VERTICAL BAR GRAPH:");
        System.out.println("X-axis: Doctor Names | Y-axis: Total Prescriptions");
        System.out.println("-".repeat(80));

        LinkedList<DoctorSummary> doctorSummaries = new LinkedList<>();

        for (int i = 1; i <= summaries.size(); i++) {
            DoctorDrugSummary summary = summaries.getEntry(i);
            boolean found = false;

            for (int j = 1; j <= doctorSummaries.size(); j++) {
                DoctorSummary doctorSummary = doctorSummaries.getEntry(j);
                if (doctorSummary.doctorName.equals(summary.doctorName)) {
                    doctorSummary.totalPrescriptions++;
                    found = true;
                    break;
                }
            }

            if (!found) {
                doctorSummaries.addLast(new DoctorSummary(summary.doctorName, 1));
            }
        }

        int maxPrescriptions = 0;
        for (int i = 1; i <= doctorSummaries.size(); i++) {
            DoctorSummary summary = doctorSummaries.getEntry(i);
            if (summary.totalPrescriptions > maxPrescriptions) {
                maxPrescriptions = summary.totalPrescriptions;
            }
        }

        if (maxPrescriptions == 0) {
            System.out.println("No data available for graph.");
            return;
        }

        int graphHeight = Math.min(20, maxPrescriptions);
        int scale = maxPrescriptions / graphHeight;
        if (scale == 0) scale = 1;

        for (int row = graphHeight; row >= 0; row--) {
            int currentValue = row * scale;
            
            if (row == graphHeight) {
                System.out.printf("%3d |", maxPrescriptions);
            } else if (row == 0) {
                System.out.printf("%3d |", 0);
            } else {
                System.out.printf("%3d |", currentValue);
            }
            
            for (int i = 1; i <= doctorSummaries.size(); i++) {
                DoctorSummary summary = doctorSummaries.getEntry(i);
                int barHeight = summary.totalPrescriptions / scale;
                
                if (row <= barHeight && row > 0) {
                    System.out.print("  x  ");
                } else {
                    System.out.print("     ");
                }
            }
            System.out.println();
        }
        
        System.out.print("    +");
        for (int i = 1; i <= doctorSummaries.size(); i++) {
            System.out.print("-----");
        }
        System.out.println();
        
        System.out.print("    |");
        for (int i = 1; i <= doctorSummaries.size(); i++) {
            DoctorSummary summary = doctorSummaries.getEntry(i);
            String label = summary.doctorName.length() > 4 ? 
                          summary.doctorName.substring(0, 4) : summary.doctorName;
            System.out.printf("%-5s", label);
        }
        System.out.println();
        
        System.out.print("    |");
        for (int i = 1; i <= doctorSummaries.size(); i++) {
            DoctorSummary summary = doctorSummaries.getEntry(i);
            System.out.printf("%-5d", summary.totalPrescriptions);
        }
        System.out.println();
    }

    private String generateDrugId() {
        return String.format("M%03d", nextDrugId++);
    }

    private void initializeSampleData() {
        addDrug("Paracetamol", 100, 5.50, "Pain reliever", "Analgesic", "2025-12-31", 20);
        addDrug("Amoxicillin", 50, 12.75, "Antibiotic", "Antibiotic", "2024-06-30", 15);
        addDrug("Omeprazole", 75, 8.25, "Acid reducer", "Proton Pump Inhibitor", "2025-03-15", 25);
        addDrug("Ibuprofen", 120, 6.80, "Anti-inflammatory", "NSAID", "2025-08-20", 30);
        addDrug("Cetirizine", 60, 4.95, "Antihistamine", "Antiallergic", "2024-11-30", 10);

        initializeSampleDispensingRecords();
        initializeSamplePrescriptionRecords();
    }

    private void initializeSampleDispensingRecords() {
        dispenseDrug("PRESC0001", "P001", "Alice Tan", "M001", "Paracetamol", 2, 5.50);
        dispenseDrug("PRESC0002", "P002", "John Lim", "M002", "Amoxicillin", 1, 12.75);
        dispenseDrug("PRESC0003", "P001", "Alice Tan", "M005", "Cetirizine", 1, 4.95);
        dispenseDrug("PRESC0004", "P003", "Siti Aminah", "M004", "Ibuprofen", 3, 6.80);
        dispenseDrug("PRESC0005", "P004", "Michael Wong", "M003", "Omeprazole", 2, 8.25);
    }

    private void initializeSamplePrescriptionRecords() {
        createPrescription("C001", "D001", "Dr. Alice Tan", "P001", "Alice Tan", 
                          "M001", "Paracetamol", 2, "500mg", "Take 1 tablet every 8 hours");
        createPrescription("C002", "D002", "Dr. Ben Ong", "P002", "John Lim", 
                          "M002", "Amoxicillin", 1, "500mg", "Take 1 capsule three times daily");
        createPrescription("C003", "D001", "Dr. Alice Tan", "P001", "Alice Tan", 
                          "M005", "Cetirizine", 1, "10mg", "Take 1 tablet at night");
        createPrescription("C004", "D003", "Dr. Chloe Lee", "P003", "Siti Aminah", 
                          "M004", "Ibuprofen", 3, "400mg", "Take 1 tablet every 6 hours");
        createPrescription("C005", "D002", "Dr. Ben Ong", "P004", "Michael Wong", 
                          "M003", "Omeprazole", 2, "20mg", "Take 1 capsule daily");
    }

    private Drug[] sortDrugsByName() {
        Drug[] drugArray = new Drug[drugList.size()];
        for (int i = 1; i <= drugList.size(); i++) {
            drugArray[i-1] = drugList.getEntry(i);
        }
        for (int i = 0; i < drugArray.length - 1; i++) {
            for (int j = 0; j < drugArray.length - i - 1; j++) {
                if (drugArray[j] != null && drugArray[j+1] != null && 
                    drugArray[j].getName().compareToIgnoreCase(drugArray[j+1].getName()) > 0) {
                    Drug temp = drugArray[j];
                    drugArray[j] = drugArray[j+1];
                    drugArray[j+1] = temp;
                }
            }
        } return drugArray;
    }

    public int getDrugListSize() {
        return drugList.size();
    }

    public boolean isDrugListEmpty() {
        return drugList.isEmpty();
    }
    
    private static class DoctorDrugSummary {
        String doctorName;
        String drugName;
        int totalQuantity;
        DoctorDrugSummary(String doctorName, String drugName, int totalQuantity) {
            this.doctorName = doctorName;
            this.drugName = drugName;
            this.totalQuantity = totalQuantity;
        }
    }

    private static class DrugQuantitySummary {
        String drugName;
        int totalQuantity;
    DrugQuantitySummary(String drugName, int totalQuantity) {
            this.drugName = drugName;
            this.totalQuantity = totalQuantity;
        }
    }

    private static class DoctorSummary {
        String doctorName;
        int totalPrescriptions;
        DoctorSummary(String doctorName, int totalPrescriptions) {
            this.doctorName = doctorName;
            this.totalPrescriptions = totalPrescriptions;
        }
    }
} 