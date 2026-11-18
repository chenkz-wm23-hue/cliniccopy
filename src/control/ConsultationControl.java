package control;

import adt.LinkedList;
import adt.LinkedListInterface;
import dao.ConsultationDao;
import entity.Consultation;
import entity.QueueEntry;
import java.time.LocalDateTime;

public class ConsultationControl {
    private LinkedListInterface<Consultation> consultationList = new LinkedList<>();
    private final ConsultationDao dao;

    public ConsultationControl(ConsultationDao dao) {
        this.dao = dao;
        this.consultationList = dao.getAllConsultations();
    }

    public String getLastConsultationId() {
        if (consultationList.isEmpty()) return null;
        return consultationList.getEntry(consultationList.size()).getConsultationId();
    }

   
    public void addConsultation(QueueEntry patientEntry, String doctorId, LocalDateTime scheduledDT) {
        Consultation c = dao.createFromQueue(patientEntry, doctorId, scheduledDT);
        System.out.println("Consultation created: " + c.getConsultationId());
    }

    public void displayConsultations() {
        if (consultationList.isEmpty()) {
            System.out.println("No consultations recorded.");
            return;
        }
        System.out.println("\n==================== Consultation Records ====================");
        System.out.printf("%-6s | %-10s | %-10s | %-10s | %-10s | %-10s | %s%n",
                "CID", "Date", "Check-in", "Consult", "DoctorID", "PatientID", "Issue");
        System.out.println("--------------------------------------------------------------");
        for (int i = 1; i <= consultationList.size(); i++) {
            Consultation c = consultationList.getEntry(i);
            System.out.printf("%-6s | %-10s | %-10s | %-10s | %-10s | %-10s | %s%n",
                    c.getConsultationId(),
                    c.getDate(),
                    c.getCheckInTime(),
                    c.getConsultationTime(),
                    c.getDoctorId(),
                    c.getPatientId(),
                    c.getIssue());
        }
        System.out.println("==============================================================");
    }

    public void searchByPatientId(String patientId) {
        boolean found = false;
        for (int i = 1; i <= consultationList.size(); i++) {
            Consultation c = consultationList.getEntry(i);
            if (c.getPatientId().equals(patientId)) {
                if (!found) {
                    System.out.printf("%-6s | %-10s | %-5s | %-10s | %-10s | %-15s%n",
                            "ID", "Date", "Time", "DoctorID", "PatientID", "Issue");
                    found = true;
                }
                System.out.println(c);
            }
        }
        if (!found) System.out.println("No consultations found for patient " + patientId);
    }

    public void searchByDoctorId(String doctorId) {
        boolean found = false;
        for (int i = 1; i <= consultationList.size(); i++) {
            Consultation c = consultationList.getEntry(i);
            if (c.getDoctorId().equals(doctorId)) {
                if (!found) {
                    System.out.printf("%-6s | %-10s | %-5s | %-10s | %-10s | %-15s%n",
                            "ID", "Date", "Time", "DoctorID", "PatientID", "Issue");
                    found = true;
                }
                System.out.println(c);
            }
        }
        if (!found) System.out.println("No consultations found for doctor " + doctorId);
    }

    public void searchByConsultationId(String consultationId) {
        boolean found = false;
        for (int i = 1; i <= consultationList.size(); i++) {
            Consultation c = consultationList.getEntry(i);
            if (c.getConsultationId().equalsIgnoreCase(consultationId)) {
                if (!found) {
                    System.out.printf("%-6s | %-10s | %-8s | %-8s | %-10s | %-10s | %-15s%n",
                            "CID", "Date", "CheckIn", "Consult", "DocID", "PatID", "Issue");
                    found = true;
                }
                System.out.println(c);
                break;
            }
        }
        if (!found) System.out.println("No consultation found with ID " + consultationId);
    }

    public void arrangeFollowUp(String consultationId, String followUpDate) {
        for (int i = 1; i <= consultationList.size(); i++) {
            Consultation c = consultationList.getEntry(i);
            if (c.getConsultationId().equals(consultationId)) {
                c.setFollowUpNeeded(true);
                c.setFollowUpDate(followUpDate);
                System.out.println("Follow-up arranged on " + followUpDate + " for consultation " + consultationId);
                return;
            }
        }
        System.out.println("Consultation not found.");
    }

    public void reportByDoctor() {
        if (consultationList.isEmpty()) {
            System.out.println("No consultations available.");
            return;
        }
        System.out.println("\n===== Consultation Count by Doctor =====");
        for (int i = 1; i <= consultationList.size(); i++) {
            String docId = consultationList.getEntry(i).getDoctorId();
            int count = 0;
            for (int j = 1; j <= consultationList.size(); j++) {
                if (consultationList.getEntry(j).getDoctorId().equals(docId)) count++;
            }
            boolean printed = false;
            for (int k = 1; k < i; k++) {
                if (consultationList.getEntry(k).getDoctorId().equals(docId)) { printed = true; break; }
            }
            if (!printed) System.out.println("Doctor " + docId + " : " + count + " consultations");
        }
    }

    public void reportByDate() {
        if (consultationList.isEmpty()) {
            System.out.println("No consultations available.");
            return;
        }
        System.out.println("\n===== Consultation Count by Date =====");
        for (int i = 1; i <= consultationList.size(); i++) {
            String date = consultationList.getEntry(i).getDate();
            int count = 0;
            for (int j = 1; j <= consultationList.size(); j++) {
                if (consultationList.getEntry(j).getDate().equals(date)) count++;
            }
            boolean printed = false;
            for (int k = 1; k < i; k++) {
                if (consultationList.getEntry(k).getDate().equals(date)) { printed = true; break; }
            }
            if (!printed) System.out.println(date + " : " + count + " consultations");
        }
    }

    public void reportFollowUps() {
        System.out.println("\n===== Scheduled Follow-ups =====");
        boolean found = false;
        for (int i = 1; i <= consultationList.size(); i++) {
            Consultation c = consultationList.getEntry(i);
            if (c.isFollowUpNeeded()) {
                found = true;
                System.out.println("Consultation " + c.getConsultationId() +
                        " follow-up on " + c.getFollowUpDate());
            }
        }
        if (!found) System.out.println("No follow-ups scheduled.");
    }

    public Consultation getConsultationById(String consultationId) {
        if (consultationId == null || consultationList.isEmpty()) return null;
        for (int i = 1; i <= consultationList.size(); i++) {
            Consultation c = consultationList.getEntry(i);
            if (c.getConsultationId().equalsIgnoreCase(consultationId)) return c;
        }
        return null;
    }

    public adt.LinkedListInterface<entity.Consultation> getAllConsultations() { return consultationList; }
    
}
