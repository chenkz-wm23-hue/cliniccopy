package main;

import boundary.ConsultationBoundary;
import boundary.DoctorBoundary;
import boundary.PatientBoundary;
import boundary.PharmacyBoundary;
import boundary.TreatmentBoundary;
import control.ConsultationControl;
import control.DoctorControl;
import control.PatientControl;
import control.PharmacyControl;
import control.ReportControl;
import control.TreatmentControl;
import dao.ConsultationDao;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

  
        PatientControl patientControl = new PatientControl();
        DoctorControl doctorControl   = new DoctorControl();

        
        ConsultationDao consultationDao = new ConsultationDao(patientControl, doctorControl);
        ConsultationControl consultationControl = new ConsultationControl(consultationDao);

      
        TreatmentControl treatmentControl = new TreatmentControl(consultationControl);
        PharmacyControl  pharmacyControl  = new PharmacyControl();

        
        ReportControl reportControl = new ReportControl(
                consultationControl, doctorControl, patientControl, treatmentControl
        );

      
        PatientBoundary       patientBoundary       = new PatientBoundary(patientControl);
        DoctorBoundary        doctorBoundary        = new DoctorBoundary(doctorControl, reportControl);
        ConsultationBoundary  consultationBoundary  = new ConsultationBoundary(
                consultationControl, patientControl, doctorControl, reportControl
        );
        
        PharmacyBoundary      pharmacyBoundary      = new PharmacyBoundary();
        
        TreatmentBoundary treatmentBoundary =
        new TreatmentBoundary(treatmentControl, consultationControl, pharmacyControl, reportControl);

        

        int choice;
        do {
            System.out.println("\n===== Clinic Management System =====");
            System.out.println("1. Patient Management");
            System.out.println("2. Doctor Management");
            System.out.println("3. Consultation Management");
            System.out.println("4. Medical Treatment");
            System.out.println("5. Pharmacy");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            while (!sc.hasNextInt()) { System.out.print("Please enter 1-9: "); sc.next(); }
            choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> patientBoundary.menu();
                case 2 -> doctorBoundary.menu();
                case 3 -> consultationBoundary.menu();
                case 4 -> treatmentBoundary.menu();
                case 5 -> pharmacyBoundary.start();

                
               
              

                case 6 -> System.out.println("Exiting system. Goodbye!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);

        sc.close();
    }
}
