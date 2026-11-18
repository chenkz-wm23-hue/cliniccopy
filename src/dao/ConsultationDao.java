package dao;

import adt.LinkedList;
import adt.LinkedListInterface;
import control.DoctorControl;
import control.PatientControl;
import entity.Consultation;
import entity.Doctor;
import entity.Patient;
import entity.QueueEntry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class ConsultationDao {
    private final LinkedListInterface<Consultation> consultations = new LinkedList<>();

    public ConsultationDao(PatientControl patientControl, DoctorControl doctorControl) {
        seed(patientControl, doctorControl);
    }

    public LinkedListInterface<Consultation> getAllConsultations() {
        return consultations;
    }

   
    public Consultation createFromQueue(QueueEntry entry, String doctorId, LocalDateTime dt) {
        Consultation c = new Consultation(entry, doctorId, dt);
        consultations.addLast(c);
        return c;
    }

   
    private void seed(PatientControl patientControl, DoctorControl doctorControl) {
        if (!consultations.isEmpty()) return;

        adt.LinkedListInterface<Patient> pats = patientControl.getAllPatients();
        adt.LinkedListInterface<Doctor> docs = doctorControl.getAllDoctors();
        if (pats == null || pats.size() == 0 || docs == null || docs.size() == 0) return;

        String[] issues = {
            "Fever and cough", "Back pain", "Headache", "Skin rash",
            "Follow-up check", "Allergy symptoms", "Chest discomfort", "Knee pain"
        };

        LocalDate startDay = LocalDate.now().minusDays(2);

        int patientIdx = 1; 
        int issueIdx   = 0; 

        
        for (int dIdx = 1; dIdx <= docs.size(); dIdx++) {
            Doctor doc = docs.getEntry(dIdx);

            for (int k = 0; k < 2; k++) {
              
                if (patientIdx > pats.size()) patientIdx = 1;
                Patient p = pats.getEntry(patientIdx++);

               
                LocalDate day = startDay.plusDays(dIdx + k);
                LocalTime checkInTime = LocalTime.of(9 + (k * 2), 0);     
                LocalDateTime checkIn = LocalDateTime.of(day, checkInTime);
                LocalDateTime consult = checkIn.plusMinutes(20);

               
                String issue = issues[issueIdx++ % issues.length];

                
                QueueEntry qe = new QueueEntry(p, issue, checkIn);

                
                Consultation c = new Consultation(qe, doc.getDocID(), consult);

               
                if (((dIdx + k) % 2) == 0) {
                    c.setFollowUpNeeded(true);
                    c.setFollowUpDate(day.plusDays(5 + (k * 2)).toString()); 
                }

                consultations.addLast(c);
            }
        }
    }
}
