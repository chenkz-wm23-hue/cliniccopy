/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueueEntry {
    private Patient patient;          
    private String issue;            
    private String visitDate;         
    private String visitTime;        

    
    public QueueEntry(Patient patient, String issue, LocalDateTime visitDateTime) {
        this.patient = patient;
        this.issue = issue;
        this.visitDate = visitDateTime.toLocalDate().toString();
        this.visitTime = visitDateTime.toLocalTime().withSecond(0).withNano(0).toString();
    }

    
    public Patient getPatient() { return patient; }
    public String getIssue() { return issue; }
    public String getVisitDate() { return visitDate; }
    public String getVisitTime() { return visitTime; }

    @Override
    public String toString() {
        return String.format("%s - %s | Issue: %s | Check-in: %s %s",
                patient.getPatientId(),
                patient.getPatientName(),
                issue,
                visitDate,
                visitTime);
    }
}
