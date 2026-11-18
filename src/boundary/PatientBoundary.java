package boundary;

import java.util.Scanner;
import adt.LinkedListInterface;
import entity.Patient;
import control.PatientControl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PatientBoundary {

    private final PatientControl control;
    private final Scanner sc = new Scanner(System.in);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PatientBoundary(PatientControl control) {
        this.control = control;
    }

    public void menu() {
        int choice;
        do {
            System.out.println("\n===== Patient Management System =====");
            System.out.println("1. Register New Patient");
            System.out.println("2. Show Queue");
            System.out.println("3. View Patient Info");
            System.out.println("4. Delete Patient Info");
            System.out.println("5. Update Patient Info");
            System.out.println("6. Report Summaries");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addNewPatient();
                case 2 -> queueMenu();
                case 3 -> viewPatient();
                case 4 -> deletePatient();
                case 5 -> updatePatientInfo();
                case 6 -> {
                   
                }
                case 7 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }

    public void viewPatient() {
        control.displayPatients();
        int choice;
        do {
            System.out.println("1. Search Patient by ID");
            System.out.println("2. Search Patient by Name");
            System.out.println("3. Back to Patient Menu");
            System.out.print("Enter your Choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> searchPatientbySchoolId();
                case 2 -> searchPatientbyName();
                case 3 -> System.out.println("Returning to Patient Menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    public void queueMenu() {
        int choice;
        do {
            control.displayQueue();
            System.out.println("1. Add Patient to Queue");
            System.out.println("2. Back to Patient Menu");
            System.out.print("Enter your Choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addExistingPatient();
                case 2 -> System.out.println("Returning to Patient Menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 2);
    }

    private void addNewPatient() {
    Patient patient = new Patient();

    System.out.print("Enter Patient Name: ");
    patient.setPatientName(sc.nextLine());

    // Age validation
    int age;
    do {
        System.out.print("Enter Patient Age: ");
        age = sc.nextInt();
        sc.nextLine();
        if (!control.isValidAge(age)) {
            System.out.println("Invalid age. Please enter a value between 1 and 119.");
        }
    } while (!control.isValidAge(age));
    patient.setPatientAge(age);

   
    String gender;
    do {
        System.out.print("Enter Gender (M/F): ");
        gender = sc.nextLine().trim().toUpperCase();
        if (!control.isValidGender(gender)) {
            System.out.println("Invalid input. Please enter only M or F.");
        }
    } while (!control.isValidGender(gender));
    patient.setGender(gender);

    
    String id;
    do {
        System.out.print("Enter School ID (7 digits): ");
        id = sc.nextLine().trim();
        if (!control.isValidSchoolId(id)) {
            System.out.println("Invalid School ID. Must be exactly 7 digits.");
        }
    } while (!control.isValidSchoolId(id));
    patient.setSchoolId(id);

   
    String phone;
    do {
        System.out.print("Enter Patient Phone No.: ");
        phone = sc.nextLine().trim();
        if (!control.isValidPhone(phone)) {
            System.out.println("Invalid phone number. Digits only.");
        }
    } while (!control.isValidPhone(phone));
    patient.setPatientPhNo(phone);

    
    LocalDateTime now = LocalDateTime.now();
    patient.setRegDate(dtf.format(now));

    control.addPatient(patient);

    System.out.println("\nPatient Registered Successfully:");
    System.out.printf("%-10s %-20s %-5s %-5s %-15s %-15s %-20s\n",
            "ID", "Name", "Age", "G", "School ID", "Phone", "Reg Date");

    System.out.printf("%-10s %-20s %-5d %-5s %-15s %-15s %-20s\n",
            patient.getPatientId(),
            patient.getPatientName(),
            patient.getPatientAge(),
            patient.getGender(),
            patient.getSchoolId(),
            patient.getPatientPhNo(),
            patient.getRegDate());

        System.out.print("\nEnter Reason for this visit: ");
        String issue = sc.nextLine();

        System.out.print("Add this patient to queue? [Y/N]: ");
        String confirm;
        do {
            confirm = sc.nextLine().trim().toUpperCase();
            if (!control.isValidYesNo(confirm)) {
                System.out.print("Invalid input. Please enter Y or N: ");
            }
        } while (!control.isValidYesNo(confirm));

        if (confirm.equals("Y")) {
            String confirm2;
            System.out.print("Is this Patient a priority? [Y/N]: ");
            do {
                confirm2 = sc.nextLine().trim().toUpperCase();
                if (!control.isValidYesNo(confirm2)) {
                    System.out.print("Invalid input. Please enter Y or N: ");
                }
            } while (!control.isValidYesNo(confirm2));

            if (confirm2.equals("Y")) {
                control.enqueuePatientPriority(patient.getPatientId(), issue);
            } else {
                control.enqueuePatient(patient.getPatientId(), issue);
            }
        }
    }

    private void addExistingPatient() {
        if (control.isEmpty()) {
            System.out.println("No patients in the system.");
            return;
        }

        boolean proceed = false;
        while (!proceed) {
            searchPatientbyName();
            System.out.print("\nDo you want to search again? (Y/N): ");
            String option = sc.nextLine().trim().toUpperCase();

            if (option.equals("Y")) {
                continue;
            } else if (option.equals("N")) {
                proceed = true;
            } else {
                System.out.println("Invalid choice. Please enter Y or N.");
            }
        }

        System.out.print("Enter Patient ID: ");
        String id = sc.nextLine().toUpperCase();
        Patient found = control.searchById(id);

        if (found != null) {
            System.out.println("\nSearch Result:");
            System.out.printf("%-10s %-20s %-5s %-5s %-15s %-15s %-20s\n",
                    "ID", "Name", "Age", "G", "School ID", "Phone", "Reg Date");
            System.out.printf("%-10s %-20s %-5d %-5s %-15s %-15s %-20s\n",
                    found.getPatientId(),
                    found.getPatientName(),
                    found.getPatientAge(),
                    found.getGender(),
                    found.getSchoolId(),
                    found.getPatientPhNo(),
                    found.getRegDate());

            System.out.print("\nEnter Reason for this visit: ");
            String issue = sc.nextLine();

            System.out.print("Add this patient to queue? [Y/N]: ");
            String confirm;
            do {
                confirm = sc.nextLine().trim().toUpperCase();
                if (!control.isValidYesNo(confirm)) {
                    System.out.print("Invalid input. Please enter Y or N: ");
                }
            } while (!control.isValidYesNo(confirm));

            if (confirm.equals("Y")) {
                String confirm2;
                System.out.print("Is this Patient a priority? [Y/N]: ");
                do {
                    confirm2 = sc.nextLine().trim().toUpperCase();
                    if (!control.isValidYesNo(confirm2)) {
                        System.out.print("Invalid input. Please enter Y or N: ");
                    }
                } while (!control.isValidYesNo(confirm2));

                if (confirm2.equals("Y")) {
                    control.enqueuePatientPriority(found.getPatientId(), issue);
                } else {
                    control.enqueuePatient(found.getPatientId(), issue);
                }
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    private void deletePatient() {
        if (control.isEmpty()) {
            System.out.println("No patients in the system.");
            return;
        }

        control.displayPatients();
        System.out.print("Enter ID to delete: ");
        String id = sc.nextLine().toUpperCase();
        Patient found = control.searchById(id);

        if (found != null) {
            control.removePatientById(id);
        } else {
            System.out.println("Patient not found.");
        }
    }

    private void searchPatientbySchoolId() {
    if (control.isEmpty()) {
        System.out.println("No patients in the system.");
        return;
    }

    String id;
    do {
        System.out.print("Enter School ID (7 digits): ");
        id = sc.nextLine().trim();
        if (!control.isValidSchoolId(id)) {
            System.out.println("Invalid School ID. Must be exactly 7 digits.");
        }
    } while (!control.isValidSchoolId(id));

    Patient found = control.searchBySchoolId(id);
    if (found != null) {
        System.out.println("\nSearch Result:");
        System.out.printf("%-10s %-20s %-5s %-5s %-15s %-15s %-20s\n",
                "ID", "Name", "Age", "G", "School ID", "Phone", "Reg Date");
        System.out.printf("%-10s %-20s %-5d %-5s %-15s %-15s %-20s\n",
                found.getPatientId(),
                found.getPatientName(),
                found.getPatientAge(),
                found.getGender(),
                found.getSchoolId(),
                found.getPatientPhNo(),
                found.getRegDate());
    } else {
        System.out.println("Patient not found.");
    }
}

    private void searchPatientbyName() {
        if (control.isEmpty()) {
            System.out.println("No patients in the system.");
            return;
        }

        System.out.print("Enter Name (full or partial): ");
        String name = sc.nextLine();
        LinkedListInterface<Patient> results = control.searchByName(name);

        if (!results.isEmpty()) {
            System.out.println("\nSearch Results:");
            System.out.printf("%-10s %-20s %-5s %-5s %-15s %-15s %-20s\n",
                    "ID", "Name", "Age", "G", "School ID", "Phone", "Reg Date");
            for (int i = 1; i <= results.size(); i++) {
                Patient found = results.getEntry(i);
                System.out.printf("%-10s %-20s %-5d %-5s %-15s %-15s %-20s\n",
                        found.getPatientId(),
                        found.getPatientName(),
                        found.getPatientAge(),
                        found.getGender(),
                        found.getSchoolId(),
                        found.getPatientPhNo(),
                        found.getRegDate());
            }
        } else {
            System.out.println("No matching patients found.");
        }
    }

    private void updatePatientInfo() {
        if (control.isEmpty()) {
            System.out.println("No patients in the system.");
            return;
        }

        boolean proceed = false;
        while (!proceed) {
            searchPatientbyName();
            System.out.print("\nDo you want to search again? (Y/N): ");
            String option = sc.nextLine().trim().toUpperCase();

            if (option.equals("Y")) {
                continue;
            } else if (option.equals("N")) {
                proceed = true;
            } else {
                System.out.println("Invalid choice. Please enter Y or N.");
            }
        }

        System.out.print("Enter Patient ID to update: ");
        String id = sc.nextLine().toUpperCase();
        Patient patient = control.searchById(id);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        boolean updating = true;
        while (updating) {
            System.out.println("\nCurrent Info:");
            System.out.printf("%-10s %-20s %-5s %-8s %-10s %-15s %-20s\n",
                    "ID", "Name", "Age", "Gender", "SchoolID", "Phone", "Reg Date");
            System.out.printf("%-10s %-20s %-5d %-8s %-10s %-15s %-20s\n",
                    patient.getPatientId(),
                    patient.getPatientName(),
                    patient.getPatientAge(),
                    patient.getGender(),
                    patient.getSchoolId(),
                    patient.getPatientPhNo(),
                    patient.getRegDate());

            System.out.println("\nWhich field do you want to update?");
            System.out.println("1. Name");
            System.out.println("2. Age");
            System.out.println("3. Gender");
            System.out.println("4. School ID");
            System.out.println("5. Phone Number");
            System.out.println("6. Exit Update");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new Name: ");
                    String name = sc.nextLine();
                    patient.setPatientName(name);
                }
                case 2 -> {
                    System.out.print("Enter new Age: ");
                    int age = sc.nextInt();
                    sc.nextLine();
                    patient.setPatientAge(age);
                }
                case 3 -> {
                    String gender;
                    do {
                        System.out.print("Enter Gender (M/F): ");
                        gender = sc.nextLine().trim().toUpperCase();
                        if (!gender.equals("M") && !gender.equals("F")) {
                            System.out.println("Invalid input. Please enter only M or F.");
                        }
                    } while (!gender.equals("M") && !gender.equals("F"));
                    patient.setGender(gender);
                }
                case 4 -> {
                    String schoolId;
                    do {
                        System.out.print("Enter new School ID (7 digits, e.g. 2401234): ");
                        schoolId = sc.nextLine().trim();
                        if (!schoolId.matches("\\d{7}")) {
                            System.out.println("Invalid School ID. Must be exactly 7 digits.");
                        }
                    } while (!schoolId.matches("\\d{7}"));
                    patient.setSchoolId(schoolId);
                }
                case 5 -> {
                    String phone;
                    do {
                        System.out.print("Enter new Phone Number (digits only): ");
                        phone = sc.nextLine().trim();
                        if (!phone.matches("\\d+")) {
                            System.out.println("Invalid phone number. Digits only.");
                        }
                    } while (!phone.matches("\\d+"));
                    patient.setPatientPhNo(phone);
                }
                case 6 -> {
                    updating = false;
                    System.out.println("Exiting update menu.");
                }
                default -> System.out.println("Invalid choice.");
            }

            if (choice >= 1 && choice <= 5) {
                System.out.println("Patient information updated successfully.");
            }
        }
    }
}