package dao;

import adt.LinkedList;
import adt.LinkedListInterface;
import control.ConsultationControl;
import entity.Consultation;
import entity.TreatmentRecord;

/**
 * DAO for Treatment Records.
 * - Seeds dummy treatment records from consultations.
 */
public class TreatmentDao {

    public LinkedListInterface<TreatmentRecord> seed(ConsultationControl consultationControl) {
        LinkedListInterface<TreatmentRecord> records = new LinkedList<>();

        LinkedListInterface<Consultation> cons = consultationControl.getAllConsultations();
        if (cons == null || cons.size() == 0) return records;

        // Add some sample treatments for first few consultations
        if (cons.size() >= 4) {
            records.addLast(new TreatmentRecord(
                    cons.getEntry(1),
                    "Flu symptoms",
                    "Paracetamol",
                    "Take 1 tablet every 8 hours for 3 days"
            ));

            records.addLast(new TreatmentRecord(
                    cons.getEntry(2),
                    "Migraine",
                    "Ibuprofen",
                    "200mg twice a day, after meals"
            ));

            records.addLast(new TreatmentRecord(
                    cons.getEntry(3),
                    "High blood pressure",
                    "Amlodipine",
                    "5mg once daily, review in 2 weeks"
            ));

            records.addLast(new TreatmentRecord(
                    cons.getEntry(4),
                    "Allergic rhinitis",
                    "Cetirizine",
                    "10mg at night, avoid allergens"
            ));
        }

        // For any other consultations beyond 4, just auto-fill
        for (int i = 5; i <= cons.size(); i++) {
            Consultation c = cons.getEntry(i);
            records.addLast(new TreatmentRecord(
                    c,
                    "General checkup",
                    "Vitamin supplements",
                    "Maintain healthy lifestyle"
            ));
        }

        return records;
    }
}
