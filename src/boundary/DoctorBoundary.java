package boundary;

import control.DoctorControl;
import control.ReportControl;
import control.Shift;
import entity.Doctor;
import java.util.Scanner;

public class DoctorBoundary {
	private final DoctorControl control;
	private final ReportControl reportControl;
	private final Scanner sc = new Scanner(System.in);

	public DoctorBoundary(DoctorControl control, ReportControl reportControl) { this.control = control; this.reportControl = reportControl; }

	public void menu() {
		int choice;
		do {
			System.out.println("\n===== Doctor Management System =====");
			System.out.println("1. Register New Doctor");
			System.out.println("2. View All Doctor");
			System.out.println("3. Search Doctor by ID");
			System.out.println("4. Remove Doctor");
			System.out.println("5. Update Doctor Details");
			System.out.println("6. Apply Leave");
			System.out.println("7. View Doctor Leaves");
			System.out.println("8. Report: Doctor-Patient Engagement");
                        System.out.println("9. Report: Doctor Prescription");
			System.out.println("10. Exit");
			System.out.print("Enter your choice: ");
			choice = sc.nextInt();
			sc.nextLine();

			switch(choice) {
				case 1 -> registerNewDoctor();
				case 2 -> viewAllDoctor();
				case 3 -> searchDoctor();
				case 4 -> removeDoctor();
				case 5 -> updateDoctor();
				case 6 -> applyLeaveUI();
				case 7 -> viewLeavesUI();
				case 8 -> reportControl.printDoctorPatientEngagementReport();
                case 9 -> reportControl.printDoctorPrescriptionReport();
				case 10 -> System.out.println("Returning to Main Menu...");
				default -> System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 10);
	}

	private void registerNewDoctor() {
		System.out.println("\n===== Register New Doctor =====");

		System.out.print("Enter Doctor ID: ");
		String docID = sc.nextLine();

		if (control.searchDoctor(docID) != null) {
			System.out.println("Doctor with ID " + docID + " already exists!");
			return;
		}

		System.out.print("Enter Doctor Name: ");
		String docName = sc.nextLine();

		System.out.print("Enter Phone Number: ");
		int phoneNumber = sc.nextInt();
		sc.nextLine();

		System.out.print("Enter Department: ");
		String dept = sc.nextLine();

		System.out.print("Enter Shift Name (optional): ");
		String shiftName = sc.nextLine();
		System.out.print("Enter Start Time (HH:MM): ");
		String startTime = sc.nextLine();
		System.out.print("Enter End Time (HH:MM): ");
		String endTime = sc.nextLine();
		if (!Shift.isValidTimeFormat(startTime) || !Shift.isValidTimeFormat(endTime)) {
			System.out.println("Invalid time format! Please use HH:MM.");
			return;
		}

		System.out.print("Enter Shift Status (AVAILABLE/BUSY/BREAK/OFF): ");
		String status = sc.nextLine();

		System.out.print("Enter Break Time (HH:MM-HH:MM) or empty: ");
		String breakTime = sc.nextLine();

		Shift.ShiftStatus st;
		try { st = status == null ? Shift.ShiftStatus.AVAILABLE : Shift.ShiftStatus.valueOf(status.toUpperCase()); }
		catch (Exception e) { st = Shift.ShiftStatus.AVAILABLE; }
		Shift shift = new Shift(startTime, endTime, shiftName, breakTime, st);
		Doctor newDoctor = new Doctor(docName, docID, phoneNumber, dept, shift);
		control.addDoctor(newDoctor);

		System.out.println("Doctor registered successfully!");
	}

	private void viewAllDoctor() { control.displayDoctor(); }

	private void searchDoctor() {
		System.out.print("Enter Doctor ID to search: ");
		String docID = sc.nextLine();
		Doctor found = control.searchDoctor(docID);
		System.out.println(found != null ? found : "Doctor not found!");
	}

	private void removeDoctor() {
		System.out.print("Enter Doctor ID to remove: ");
		String docID = sc.nextLine();
		System.out.println(control.deleteDoctor(docID) ? "Doctor removed successfully!" : "Doctor not found!");
	}

	private void updateDoctor() {
		System.out.println("\n===== Update Doctor Details =====");
		System.out.print("Enter Doctor ID to update: ");
		String docID = sc.nextLine();
		
		// Check if doctor exists
		Doctor existingDoctor = control.searchDoctor(docID);
		if (existingDoctor == null) {
			System.out.println("Doctor with ID " + docID + " not found!");
			return;
		}
		
		System.out.println("Current Doctor Details:");
		System.out.println(existingDoctor.toString());
		
		System.out.println("\nEnter new details (press Enter to keep current value):");
		
		System.out.print("Enter new Doctor Name [" + existingDoctor.getDocName() + "]: ");
		String newName = sc.nextLine();
		if (newName.isEmpty()) {
			newName = existingDoctor.getDocName();
		}
		
		System.out.print("Enter new Phone Number [" + existingDoctor.getPhoneNumber() + "]: ");
		String phoneInput = sc.nextLine();
		int newPhone;
		if (phoneInput.isEmpty()) {
			newPhone = existingDoctor.getPhoneNumber();
		} else {
			newPhone = Integer.parseInt(phoneInput);
		}
		
		System.out.print("Enter new Department [" + existingDoctor.getDept() + "]: ");
		String newDept = sc.nextLine();
		if (newDept.isEmpty()) {
			newDept = existingDoctor.getDept();
		}
		
		System.out.print("Enter new Shift Name [" + existingDoctor.getShiftName() + "]: ");
		String newShiftName = sc.nextLine();
		if (newShiftName.isEmpty()) newShiftName = existingDoctor.getShiftName();

		System.out.print("Enter new Start Time [" + existingDoctor.getStartTime() + "]: ");
		String newStartTime = sc.nextLine();
		if (newStartTime.isEmpty()) newStartTime = existingDoctor.getStartTime();

		System.out.print("Enter new End Time [" + existingDoctor.getEndTime() + "]: ");
		String newEndTime = sc.nextLine();
		if (newEndTime.isEmpty()) newEndTime = existingDoctor.getEndTime();

		if (!Shift.isValidTimeFormat(newStartTime) || !Shift.isValidTimeFormat(newEndTime)) {
			System.out.println("Invalid time format! Please use HH:MM (e.g., 09:00)");
			return;
		}
		
		System.out.print("Enter new Shift Status [" + existingDoctor.getShiftStatus() + "]: ");
		String newStatus = sc.nextLine();
		if (newStatus.isEmpty()) newStatus = existingDoctor.getShiftStatus();
		
		System.out.print("Enter new Break Time [" + (existingDoctor.getBreakTime() == null ? "" : existingDoctor.getBreakTime()) + "]: ");
		String newBreakTime = sc.nextLine();
		if (newBreakTime.isEmpty()) newBreakTime = existingDoctor.getBreakTime();
		
		// Update the doctor
		boolean updated = control.updateDoctor(docID, newName, newPhone, newDept, newStartTime, newEndTime, newShiftName, newStatus, newBreakTime);
		if (updated) {
			System.out.println("Doctor details updated successfully!");
		} else {
			System.out.println("Failed to update doctor details!");
		}
	}

	
	private void applyLeaveUI() {
		System.out.println("\n===== Apply Leave =====");
		System.out.print("Enter Doctor ID: ");
		String docID = sc.nextLine().trim();

		if (control.searchDoctor(docID) == null) {
			System.out.println("Doctor not found.");
			return;
		}

		System.out.print("Enter FROM date (yyyy-MM-dd): ");
		String from = sc.nextLine().trim();
		System.out.print("Enter TO date (yyyy-MM-dd): ");
		String to = sc.nextLine().trim();
		System.out.print("Enter Reason: ");
		String reason = sc.nextLine();

		control.applyLeave(docID, from, to, reason);
	}

	private void viewLeavesUI() {
		System.out.print("Enter Doctor ID to view leaves: ");
		String docID = sc.nextLine().trim();
		control.displayLeavesByDoctor(docID);
	}
}
