package control;

import adt.LinkedList;
import adt.LinkedListInterface;
import dao.PatientDao;
import dao.QueueEntryDao;
import entity.Patient;
import entity.QueueEntry;
import java.time.LocalDateTime;

public class PatientControl {
    private final LinkedListInterface<Patient> patientList;
    private final LinkedListInterface<QueueEntry> patientQueue;

    public PatientControl() {
       
        PatientDao dao = new PatientDao();
        patientList = dao.getAllPatients();

        
        QueueEntryDao queueDao = new QueueEntryDao(patientList);
        patientQueue = queueDao.getAllQueueEntries();
    }

   

    public void addPatient(Patient p) {
        patientList.addLast(p);
    }

    public void displayPatients() {
        if (patientList.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        System.out.println("\n================================= Patient List =================================");
        System.out.printf("%-10s %-20s %-5s %-15s %-15s %-15s\n",
                "ID", "Name", "Age", "IC", "Phone", "Reg Date");
        System.out.println("---------------------------------------------------------------------------------");

        for (int i = 1; i <= patientList.size(); i++) {
            Patient p = patientList.getEntry(i);
            System.out.printf("%-10s %-20s %-5d %-15s %-15s %-15s\n",
                    p.getPatientId(),
                    p.getPatientName(),
                    p.getPatientAge(),
                    p.getSchoolId(),
                    p.getPatientPhNo(),
                    p.getRegDate());
        }
        System.out.println("=================================================================================");
    }

    public Patient searchBySchoolId(String id) {
        for (int i = 1; i <= patientList.size(); i++) {
            Patient p = patientList.getEntry(i);
            if (p.getSchoolId().equals(id)) {
                return p;
            }
        }
        return null;
    }
    
    public Patient searchById(String id) {
        for (int i = 1; i <= patientList.size(); i++) {
            Patient p = patientList.getEntry(i);
            if (p.getPatientId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public LinkedListInterface<Patient> searchByName(String name) {
        LinkedListInterface<Patient> results = new LinkedList<>();
        for (int i = 1; i <= patientList.size(); i++) {
            Patient p = patientList.getEntry(i);
            if (p.getPatientName().toLowerCase().contains(name.toLowerCase())) {
                results.addLast(p);
            }
        }
        return results;
    }

    public void removePatientById(String id) {
        for (int i = 1; i <= patientList.size(); i++) {
            Patient p = patientList.getEntry(i);
            if (p.getPatientId().equals(id)) {
                patientList.deleteAt(i);
                System.out.println("Patient " + id + " removed.");
                return;
            }
        }
        System.out.println("Patient " + id + " not found.");
    }

    public boolean isEmpty() {
        return patientList.isEmpty();
    }

    
    public void enqueuePatient(String id, String issue) {
        Patient p = searchById(id);
        if (p != null) {
            LocalDateTime now = LocalDateTime.now();
            QueueEntry entry = new QueueEntry(p, issue, now);
            patientQueue.addLast(entry);
            System.out.println("Patient " + p.getPatientId() +
                    " checked in at " + entry.getVisitTime() +
                    " with issue: " + issue);
        } else {
            System.out.println("Patient not found.");
        }
    }
    
     public void enqueuePatientPriority(String id, String issue) {
        Patient p = searchById(id);
        if (p != null) {
            LocalDateTime now = LocalDateTime.now();
            QueueEntry entry = new QueueEntry(p, issue, now);
            patientQueue.addFirst(entry);
            System.out.println("Priority Patient " + p.getPatientId() +
                    " checked in at " + entry.getVisitTime() +
                    " with issue: " + issue);
        } else {
            System.out.println("Patient not found.");
        }
    }

    
    public void enqueuePatient(String id, String issue, LocalDateTime checkInDateTime) {
        Patient p = searchById(id);
        if (p != null) {
            QueueEntry entry = new QueueEntry(p, issue, checkInDateTime);
            patientQueue.addLast(entry);
            System.out.println("Patient " + p.getPatientId() +
                    " checked in at " + entry.getVisitDate() + " " + entry.getVisitTime() +
                    " with issue: " + issue);
        } else {
            System.out.println("Patient not found.");
        }
    }

    public QueueEntry dequeuePatient() {
        if (patientQueue.isEmpty()) {
            System.out.println("No patients in queue.");
            return null;
        }
        QueueEntry entry = patientQueue.deleteFirst();
        System.out.println("Now serving: " + entry.getPatient().getPatientId() +
                " - " + entry.getPatient().getPatientName() +
                " (Issue: " + entry.getIssue() + ")");
        return entry;
    }

    public void displayQueue() {
        if (patientQueue.isEmpty()) {
            System.out.println("No patients in queue.");
            return;
        }

        System.out.println("\n=============================== Patient Queue ===============================");
        System.out.printf("%-10s %-20s %-5s %-15s %-15s %-10s\n",
                "ID", "Name", "Age", "Reason", "Date", "Time");
        System.out.println("-----------------------------------------------------------------------------");

        for (int i = 1; i <= patientQueue.size(); i++) {
            QueueEntry entry = patientQueue.getEntry(i);
            Patient p = entry.getPatient();
            System.out.printf("%-10s %-20s %-5d %-15s %-15s %-10s\n",
                    p.getPatientId(),
                    p.getPatientName(),
                    p.getPatientAge(),
                    entry.getIssue(),
                    entry.getVisitDate(),
                    entry.getVisitTime());
        }
        System.out.println("=============================================================================");
    }

    public QueueEntry peekQueue() {
        if (patientQueue.isEmpty()) {
            System.out.println("No patients in queue.");
            return null;
        }
        return patientQueue.getFirst();
    }

    public Patient getFirstPatient() {
        if (patientList.isEmpty()) return null;
        return patientList.getFirst();
    }
    
    
    public boolean isValidGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F"));
    }

    public boolean isValidSchoolId(String schoolId) {
        return schoolId != null && schoolId.matches("\\d{7}"); 
    }

    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d+"); 
    }

    public boolean isValidAge(int age) {
        return age > 0 && age < 120; 
    }
    
    public boolean isValidYesNo(String input) {
    return input != null && (input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"));
}

  
    public LinkedListInterface<Patient> getAllPatients() { return patientList; }
    public LinkedListInterface<QueueEntry> getQueueSnapshot() { return patientQueue; }
}
