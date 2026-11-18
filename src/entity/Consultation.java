package entity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consultation {
    private static int idCounter = 1;
    private String consultationId;
    private String patientId;
    private String doctorId;
    private String issue;
    private String date;             
    private String checkInTime;      
    private String consultationTime; 

    private boolean followUpNeeded;
    private String followUpDate;

public Consultation(QueueEntry entry, String doctorId, LocalDateTime consultationDateTime) {
    this.consultationId = String.format("C%03d", idCounter++);
    this.patientId = entry.getPatient().getPatientId();
    this.doctorId = doctorId;
    this.issue = entry.getIssue();
    this.date = entry.getVisitDate();         
    this.checkInTime = entry.getVisitTime();   
    this.consultationTime = consultationDateTime.toLocalTime().withSecond(0).withNano(0).toString(); 
    this.followUpNeeded = false;
    this.followUpDate = null;
}

   public String getConsultationId() { return consultationId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getIssue() { return issue; }
    public String getDate() { return date; }
    public String getCheckInTime() { return checkInTime; }
    public String getConsultationTime() { return consultationTime; }


    public boolean isFollowUpNeeded() { return followUpNeeded; }
    public void setFollowUpNeeded(boolean followUpNeeded) { this.followUpNeeded = followUpNeeded; }

    public String getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(String followUpDate) { this.followUpDate = followUpDate; }

    @Override
    public String toString() {
        return String.format("%-6s | %-10s | Check-in: %-8s | Consult: %-8s | Doc: %-5s | Pat: %-5s | Issue: %s",
                consultationId, date, checkInTime, consultationTime, doctorId, patientId, issue);
    }
}