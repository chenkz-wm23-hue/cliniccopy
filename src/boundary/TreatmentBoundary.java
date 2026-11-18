package boundary;

import control.TreatmentControl;
import control.ConsultationControl;
import control.PharmacyControl;
import control.ReportControl;
import entity.Consultation;
import entity.Drug;
import entity.TreatmentRecord;

import java.util.Scanner;

public class TreatmentBoundary {

    private final TreatmentControl control;
    private final ConsultationControl consultationControl;
    private final PharmacyControl pharmacyControl;
    private final ReportControl reportControl;
    private final Scanner sc = new Scanner(System.in);

    public TreatmentBoundary(TreatmentControl treatmentControl, ConsultationControl consultationControl, PharmacyControl pharmacyControl, ReportControl reportControl) {
        this.control = treatmentControl;
        this.consultationControl = consultationControl; 
        this.pharmacyControl = pharmacyControl; 
        this.reportControl = reportControl;
    }

    public void menu() {
        int choice;

        do {
            System.out.println("\n===== Treatment Record Menu =====");
            System.out.println("1. Add Treatment Record");
            System.out.println("2. Display All Records");
            System.out.println("3. Edit Treatment Record");
            System.out.println("4. Delete Treatment Record");
            System.out.println("5. Report: Patient Diagnosis Report");
            System.out.println("6. Report: Medicine Usage Report");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Please enter a number (1-7): ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 ->
                    addRecord();
                case 2 ->
                    displayAll();
                case 3 ->
                    editRecord();
                case 4 ->
                    deleteRecord();
                case 5 ->
                    reportControl.printDiagnosisSummary();
                case 6 ->
                    reportControl.printMedicineUsageSummary();
                case 7 ->
                    System.out.println("Exiting Treatment Record Menu.");
                default ->
                    System.out.println("Invalid choice. Please select 1-7.");
            }
        } while (choice != 7);
    }

    private void addRecord() {
        System.out.print("Enter Consultation ID: ");
        String consultationId = sc.nextLine().trim();

        Consultation consultation = consultationControl.getConsultationById(consultationId);

        if (consultation == null) {
            System.out.println("Consultation not found. Returning to menu.");
            return;
        }

        if (control.searchById(consultationId) != null) {
            System.out.println("A treatment record already exists for this consultation.");
            return;
        }

        System.out.print("Enter Diagnosis: ");
        String diagnosis = sc.nextLine();

        if (pharmacyControl.isDrugListEmpty()) {
            System.out.println("No drugs available in pharmacy.");
            return;
        }

        System.out.println("\nAvailable Drugs:");
        for (int i = 1; i <= pharmacyControl.getDrugListSize(); i++) {
            Drug drug = pharmacyControl.searchDrugById(String.format("M%03d", i));
            if (drug != null) {
                System.out.printf("%d. %s (Stock: %d, Price: RM%.2f)\n",
                        i, drug.getName(), drug.getQuantity(), drug.getPrice());
            }
        }

        System.out.print("\nChoose a drug by number: ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning.");
            return;
        }

        Drug selectedDrug = null;
        if (choice >= 1 && choice <= pharmacyControl.getDrugListSize()) {
            selectedDrug = pharmacyControl.searchDrugById(String.format("M%03d", choice));
        }

        if (selectedDrug == null) {
            System.out.println("Invalid drug choice.");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty;
        try {
            qty = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (!pharmacyControl.stockOut(selectedDrug.getId(), qty)) {
            System.out.println("Not enough stock for " + selectedDrug.getName());
            return;
        }

        System.out.print("Enter Notes: ");
        String notes = sc.nextLine();

        TreatmentRecord record = new TreatmentRecord(consultation, diagnosis, selectedDrug.getName(), notes);
        control.addRecord(record);

        System.out.println("Treatment record added successfully.");
    }

    private void displayAll() {
        if (control.getAllRecords().isEmpty()) {
            System.out.println("No treatment records available.");
            return;
        }

        System.out.printf("%-8s | %-9s | %-7s | %-7s | %-8s | %-20s | %-18s | %-30s%n",
                "CID", "Date", "CheckIn", "Consult", "PatID", "Diagnosis", "Treatment", "Notes");

        for (int i = 1; i <= control.getAllRecords().size(); i++) {
            System.out.println(control.getAllRecords().getEntry(i));
        }
    }

    private void editRecord() {
        System.out.print("Enter Record ID to edit: ");
        String recordId = sc.nextLine().trim();

        TreatmentRecord record = control.searchById(recordId);
        if (record == null) {
            System.out.println("Record not found.");
            return;
        }

        System.out.println("Editing record for Consultation ID: " + record.getConsultation().getConsultationId());

        // --- Diagnosis update
        System.out.print("Enter new Diagnosis (leave blank to keep '" + record.getDiagnosis() + "'): ");
        String newDiagnosis = sc.nextLine();
        if (!newDiagnosis.isEmpty()) {
            record.setDiagnosis(newDiagnosis);
        }

        // --- Show available drugs
        if (pharmacyControl.isDrugListEmpty()) {
            System.out.println("No drugs available in pharmacy.");
        } else {
            System.out.println("\nAvailable Drugs:");
            for (int i = 1; i <= pharmacyControl.getDrugListSize(); i++) {
                Drug drug = pharmacyControl.searchDrugById(String.format("M%03d", i));
                if (drug != null) {
                    System.out.printf("%d. %s (Stock: %d, Price: RM%.2f)\n",
                            i, drug.getName(), drug.getQuantity(), drug.getPrice());
                }
            }

            System.out.print("\nChoose a new drug by number (or press Enter to keep '" + record.getTreatment() + "'): ");
            String drugChoice = sc.nextLine();

            if (!drugChoice.isEmpty()) {
                try {
                    int choice = Integer.parseInt(drugChoice);
                    if (choice >= 1 && choice <= pharmacyControl.getDrugListSize()) {
                        Drug selectedDrug = pharmacyControl.searchDrugById(String.format("M%03d", choice));
                        if (selectedDrug != null) {
                            record.setTreatment(selectedDrug.getName());
                            System.out.println("Drug updated to " + selectedDrug.getName());
                        }
                    } else {
                        System.out.println("Invalid drug choice. Keeping previous treatment.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Keeping previous treatment.");
                }
            }
        }

        // --- Notes update
        System.out.print("Enter new Notes (leave blank to keep '" + record.getNotes() + "'): ");
        String newNotes = sc.nextLine();
        if (!newNotes.isEmpty()) {
            record.setNotes(newNotes);
        }

        System.out.println("Record updated successfully.");
    }

    private void deleteRecord() {
        System.out.print("\nEnter Consultation ID to delete: ");
        String id = sc.nextLine().trim();

        TreatmentRecord record = control.searchById(id);
        if (record == null) {
            System.out.println("No record found with ID: " + id);
            return;
        }

        Consultation c = record.getConsultation();
        System.out.println("\nFound Treatment Record:");
        System.out.printf("%-8s | %-12s | %-10s | %-10s | %-8s | %-25s | %-20s | %-30s%n",
                "CID", "Date", "CheckIn", "Consult", "DocID", "Diagnosis", "Treatment", "Notes");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s | %-12s | %-10s | %-10s | %-8s | %-25s | %-20s | %-30s%n",
                c.getConsultationId(),
                c.getDate(),
                c.getConsultationTime(),
                c.getConsultationId(), // or DoctorId depending on your design
                c.getDoctorId(),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getNotes());

        // Confirm deletion
        System.out.print("Are you sure you want to delete record " + id + "? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            boolean deleted = control.deleteRecord(id);
            if (deleted) {
                System.out.println("Record " + id + " deleted successfully.");
            } else {
                System.out.println("Failed to delete record " + id + ".");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void generatePatientDiagnosisReport() {
        if (control.getAllRecords().isEmpty()) {
            System.out.println("No treatment records available.");
            return;
        }

        System.out.println("\n===== Patient Diagnosis Report =====");
        System.out.printf("%-10s | %-10s | %-20s%n", "PatientID", "ConsultID", "Diagnosis");

        for (int i = 1; i <= control.getAllRecords().size(); i++) {
            TreatmentRecord record = control.getAllRecords().getEntry(i);
            System.out.printf("%-10s | %-10s | %-20s%n",
                    record.getConsultation().getPatientId(),
                    record.getConsultation().getConsultationId(),
                    record.getDiagnosis());
        }
    }

    private void generateMedicineUsageReport() {
        if (control.getAllRecords().isEmpty()) {
            System.out.println("No treatment records available.");
            return;
        }

        String[] medicines = new String[control.getAllRecords().size()];
        int[] counts = new int[control.getAllRecords().size()];
        int uniqueCount = 0;

        for (int i = 1; i <= control.getAllRecords().size(); i++) {
            TreatmentRecord record = control.getAllRecords().getEntry(i);
            String medicine = record.getTreatment();

            // Check if medicine already exists in our list
            int index = -1;
            for (int j = 0; j < uniqueCount; j++) {
                if (medicines[j].equalsIgnoreCase(medicine)) {
                    index = j;
                    break;
                }
            }

            // If found, increase count
            if (index != -1) {
                counts[index]++;
            } else {
                // Add new medicine to list
                medicines[uniqueCount] = medicine;
                counts[uniqueCount] = 1;
                uniqueCount++;
            }
        }

        System.out.println("\n===== Medicine Usage Report =====");
        System.out.printf("%-20s | %-10s%n", "Medicine", "Times Prescribed");

        for (int i = 0; i < uniqueCount; i++) {
            System.out.printf("%-20s | %-10d%n", medicines[i], counts[i]);
        }
    }
}
