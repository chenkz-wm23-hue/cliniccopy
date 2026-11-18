package boundary;

import control.ConsultationControl;
import control.PatientControl;
import control.DoctorControl;
import control.ReportControl;
import entity.Doctor;
import entity.QueueEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsultationBoundary {
    private final ConsultationControl control;
    private final PatientControl patientControl;
    private final DoctorControl doctorControl;
    private final ReportControl reportControl;
    private final Scanner sc = new Scanner(System.in);

    public ConsultationBoundary(ConsultationControl control,
                                PatientControl patientControl,
                                DoctorControl doctorControl,
                                ReportControl reportControl) {
        this.control = control;
        this.patientControl = patientControl;
        this.doctorControl = doctorControl;
        this.reportControl = reportControl;
    }

    public void menu() {
        int choice;
        do {
            System.out.println("\n===== Consultation Management =====");
            System.out.println("1. Start Consultation (assign patient to doctor)");
            System.out.println("2. View All Consultations");
            System.out.println("3. Summary Report: Clinic Operations");
            System.out.println("4. Summary Report: Clinical Insights");
            System.out.println("5. Arrange Follow-up Appointment");
            System.out.println("6. Report: Scheduled Follow-ups");
            System.out.println("7. Back");
            System.out.print("Choice: ");
            while (!sc.hasNextInt()) { System.out.print("Enter 1-7: "); sc.next(); }
            choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> startConsultation();
                case 2 -> control.displayConsultations();
                case 3 -> reportControl.printClinicOperationsSummary();
                case 4 -> reportControl.printClinicalInsightsSummary();
                case 5 -> arrangeFollowUp();
                case 6 -> control.reportFollowUps();
                case 7 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 7);
    }

    private void startConsultation() {
      
        QueueEntry next = patientControl.peekQueue();
        if (next == null) {
            System.out.println("No patients in queue. Returning to menu...");
            return;
        }
        System.out.println("Next patient in queue: " +
                next.getPatient().getPatientId() + " - " +
                next.getPatient().getPatientName() + " (Reason: " +
                next.getIssue() + ")");

       
        System.out.println("\nSelect a doctor from the list:");
        doctorControl.displayDoctor();

        System.out.print("Enter Doctor ID for consultation: ");
        String docId = sc.nextLine().trim();
        Doctor d = doctorControl.searchDoctor(docId);
        if (d == null) {
            System.out.println("Doctor not found. Consultation cancelled.");
            return;
        }

      
        System.out.print("Enter Consultation DATE (yyyy-MM-dd): ");
        String dateStr = sc.nextLine().trim();
        System.out.print("Enter Consultation TIME (HH:mm): ");
        String timeStr = sc.nextLine().trim();

        final LocalDateTime dt;
        try {
            LocalDate date = LocalDate.parse(dateStr);   
            LocalTime time = LocalTime.parse(timeStr);  
            dt = LocalDateTime.of(date, time);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date/time. Use formats: yyyy-MM-dd and HH:mm (e.g., 2025-08-28, 09:30).");
            return;
        }

        
        QueueEntry entry = patientControl.dequeuePatient(); 
        if (entry == null) {
            System.out.println("Queue changed unexpectedly. Try again.");
            return;
        }

        control.addConsultation(entry, docId, dt);
    }

    private void arrangeFollowUp() {
        System.out.print("Enter Consultation ID: ");
        String cid = sc.nextLine().trim();
        System.out.print("Enter Follow-up Date (yyyy-MM-dd): ");
        String dateStr = sc.nextLine().trim();
        control.arrangeFollowUp(cid, dateStr);
    }
}
