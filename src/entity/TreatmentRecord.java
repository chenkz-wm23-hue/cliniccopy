package entity;

import adt.LinkedList;
import adt.LinkedListInterface;
import entity.Consultation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TreatmentRecord {

    private Consultation consultation;
    private String diagnosis;
    private String treatment;
    private String notes;

    public TreatmentRecord(Consultation consultation, String diagnosis, String treatment, String notes) {

        this.consultation = consultation;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {

        String date = consultation.getDate();
        String time = consultation.getConsultationTime();

        return String.format("%-10s %-12s %-8s %-10s %-10s %-20s %-20s %-30s",
                consultation.getConsultationId(),
                date,
                time,
                consultation.getDoctorId(),
                consultation.getPatientId(),
                diagnosis == null ? "-" : diagnosis,
                treatment == null ? "-" : treatment,
                notes == null ? "-" : notes);
    }

}
