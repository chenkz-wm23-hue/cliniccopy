package boundary;

import control.PharmacyControl;
import entity.Drug;
import java.util.Scanner;

public class PharmacyBoundary {
    private PharmacyControl pharmacyControl;
    private Scanner scanner;

    public PharmacyBoundary() {
        this.pharmacyControl = new PharmacyControl();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int choice;
        do {
            displayMainMenu();
            choice = getValidChoice(1, 8);
            switch (choice) {
                case 1:
                    addDrugMenu();
                    break;
                case 2:
                    deleteDrugMenu();
                    break;
                case 3:
                    updateDrugMenu();
                    break;
                case 4:
                    searchDrugMenu();
                    break;
                case 5:
                    displayAllDrugs();
                    break;
                case 6:
                    summaryReportsMenu();
                    break;
                case 7:
                    System.out.println("Thank you for using Pharmacy Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            if (choice != 7) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        } while (choice != 7);
    }

    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    PHARMACY MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. Add New Drug");
        System.out.println("2. Delete Drug");
        System.out.println("3. Update Drug");
        System.out.println("4. Search Drug");
        System.out.println("5. Display All Drugs");
        System.out.println("6. Summary Reports");
        System.out.println("7. Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice (1-7): ");
    }

    private void addDrugMenu() {
        System.out.println("\n=== ADD NEW DRUG ===");
        System.out.print("Enter drug name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Drug name cannot be empty!");
            return;
        }
        System.out.print("Enter quantity: ");
        int quantity = getValidInteger();
        System.out.print("Enter price: ");
        double price = getValidDouble();
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();
        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();
        System.out.print("Enter expiry date (YYYY-MM-DD): ");
        String expiryDate = scanner.nextLine().trim();
        System.out.print("Enter minimum stock level: ");
        int minStockLevel = getValidInteger();
        boolean success = pharmacyControl.addDrug(name, quantity, price, description, category, expiryDate, minStockLevel);
        if (success) {
            System.out.println("Drug added successfully!");
        } else {
            System.out.println("Failed to add drug. Please try again.");
        }
    }

    private void deleteDrugMenu() {
        System.out.println("\n=== DELETE DRUG ===");
        if (pharmacyControl.isDrugListEmpty()) {
            System.out.println("No drugs in inventory to delete.");
            return;
        }
        System.out.print("Enter drug ID to delete: ");
        String drugId = scanner.nextLine().trim().toUpperCase();
        
        if (drugId.isEmpty()) {
            System.out.println("Drug ID cannot be empty!");
            return;
        }
        Drug drug = pharmacyControl.searchDrugById(drugId);
        if (drug == null) {
            System.out.println("Drug not found!");
            return;
        }
        System.out.println("Found drug: " + drug);
        System.out.print("Are you sure you want to delete this drug? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean success = pharmacyControl.deleteDrug(drugId);
            if (success) {
                System.out.println("Drug deleted successfully!");
            } else {
                System.out.println("Failed to delete drug.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void updateDrugMenu() {
        System.out.println("\n=== UPDATE DRUG ===");
        
        if (pharmacyControl.isDrugListEmpty()) {
            System.out.println("No drugs in inventory to update.");
            return;
        }

        System.out.print("Enter drug ID to update: ");
        String drugId = scanner.nextLine().trim().toUpperCase();
        
        if (drugId.isEmpty()) {
            System.out.println("Drug ID cannot be empty!");
            return;
        }

        Drug drug = pharmacyControl.searchDrugById(drugId);
        if (drug == null) {
            System.out.println("Drug not found!");
            return;
        }

        System.out.println("Current drug information: " + drug);
        System.out.println("\nEnter new information (press Enter to keep current value):");

        System.out.print("Name [" + drug.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = drug.getName();

        System.out.print("Quantity [" + drug.getQuantity() + "]: ");
        String quantityStr = scanner.nextLine().trim();
        int quantity = quantityStr.isEmpty() ? drug.getQuantity() : getValidInteger(quantityStr);

        System.out.print("Price [" + drug.getPrice() + "]: ");
        String priceStr = scanner.nextLine().trim();
        double price = priceStr.isEmpty() ? drug.getPrice() : getValidDouble(priceStr);

        System.out.print("Description [" + drug.getDescription() + "]: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) description = drug.getDescription();

        System.out.print("Category [" + drug.getCategory() + "]: ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) category = drug.getCategory();

        System.out.print("Expiry Date [" + drug.getExpiryDate() + "]: ");
        String expiryDate = scanner.nextLine().trim();
        if (expiryDate.isEmpty()) expiryDate = drug.getExpiryDate();

        System.out.print("Minimum Stock Level [" + drug.getMinStockLevel() + "]: ");
        String minStockStr = scanner.nextLine().trim();
        int minStockLevel = minStockStr.isEmpty() ? drug.getMinStockLevel() : getValidInteger(minStockStr);

        boolean success = pharmacyControl.updateDrug(drugId, name, quantity, price, 
                                                   description, category, expiryDate, minStockLevel);
        
        if (success) {
            System.out.println("Drug updated successfully!");
        } else {
            System.out.println("Failed to update drug.");
        }
    }

    private void searchDrugMenu() {
        System.out.println("\n=== SEARCH DRUG ===");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.print("Enter your choice (1-2): ");
        
        int choice = getValidChoice(1, 2);
        
        Drug foundDrug = null;
        
        switch (choice) {
            case 1:
                System.out.print("Enter drug ID: ");
                String drugId = scanner.nextLine().trim().toUpperCase();
                foundDrug = pharmacyControl.searchDrugById(drugId);
                break;
            case 2:
                System.out.print("Enter drug name: ");
                String drugName = scanner.nextLine().trim();
                foundDrug = pharmacyControl.searchDrugByName(drugName);
                break;
        }
        
        if (foundDrug != null) {
            System.out.println("\nDrug found:");
            System.out.println(foundDrug);
            System.out.println("Description: " + foundDrug.getDescription());
            System.out.println("Low Stock: " + (foundDrug.isLowStock() ? "Yes" : "No"));
            System.out.println("Total Value: $" + foundDrug.getTotalValue());
        } else {
            System.out.println("Drug not found!");
        }
    }

    private void summaryReportsMenu() {
        System.out.println("\n=== SUMMARY REPORTS ===");
        System.out.println("1. Patient-Drug Purchase Report");
        System.out.println("2. Doctor-Prescription-Drug Report");
        System.out.print("Enter your choice (1-2): ");
        
        int choice = getValidChoice(1, 2);
        
        switch (choice) {
            case 1:
                pharmacyControl.generatePatientDrugPurchaseReport();
                break;
            case 2:
                pharmacyControl.generateDoctorPrescriptionDrugReport();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void displayAllDrugs() {
        System.out.println("\n=== ALL DRUGS IN INVENTORY ===");
        if (pharmacyControl.isDrugListEmpty()) {
            System.out.println("No drugs in inventory.");
        } else {
            // Display drugs in a simple format since we removed the inventory report
            System.out.println("=".repeat(80));
            System.out.printf("%-8s %-20s %-10s %-10s %-15s %-12s\n", 
                            "ID", "Name", "Quantity", "Price", "Category", "Expiry Date");
            System.out.println("=".repeat(80));
            
            for (int i = 1; i <= pharmacyControl.getDrugListSize(); i++) {
                Drug drug = pharmacyControl.searchDrugById("M" + String.format("%03d", i));
                if (drug != null) {
                    System.out.printf("%-8s %-20s %-10d $%-9.2f %-15s %-12s\n",
                                    drug.getId(), drug.getName(), drug.getQuantity(), 
                                    drug.getPrice(), drug.getCategory(), drug.getExpiryDate());
                }
            }
            System.out.println("=".repeat(80));
        }
    }

    // Utility methods for input validation
    private int getValidChoice(int min, int max) {
        int choice = 0;
        boolean valid = false;
        
        while (!valid) {
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    valid = true;
                } else {
                    System.out.printf("Please enter a number between %d and %d: ", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.printf("Please enter a valid number between %d and %d: ", min, max);
            }
        }
        return choice;
    }

    private int getValidInteger() {
        int value = 0;
        boolean valid = false;
        
        while (!valid) {
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= 0) {
                    valid = true;
                } else {
                    System.out.print("Please enter a non-negative number: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
        return value;
    }

    private int getValidInteger(String input) {
        try {
            int value = Integer.parseInt(input.trim());
            return value >= 0 ? value : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double getValidDouble() {
        double value = 0.0;
        boolean valid = false;
        
        while (!valid) {
            try {
                value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= 0) {
                    valid = true;
                } else {
                    System.out.print("Please enter a non-negative number: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
        return value;
    }

    private double getValidDouble(String input) {
        try {
            double value = Double.parseDouble(input.trim());
            return value >= 0 ? value : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}