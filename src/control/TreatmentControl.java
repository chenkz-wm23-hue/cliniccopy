package control;

import adt.LinkedListInterface;
import dao.TreatmentDao;
import entity.TreatmentRecord;

public class TreatmentControl {
    private final LinkedListInterface<TreatmentRecord> records;

    public TreatmentControl(ConsultationControl consultationControl) {
        TreatmentDao dao = new TreatmentDao();
        this.records = dao.seed(consultationControl); // only seed here
    }

    // ------------------- CRUD -------------------
    public void addRecord(TreatmentRecord record) {
        records.addLast(record);
    }

    public LinkedListInterface<TreatmentRecord> getAllRecords() {
        return records;
    }

    public TreatmentRecord searchById(String consultationId) {
        for (int i = 1; i <= records.size(); i++) {
            TreatmentRecord r = records.getEntry(i);
            if (r.getConsultation().getConsultationId().equalsIgnoreCase(consultationId)) {
                return r;
            }
        }
        return null;
    }

    public boolean deleteRecord(String consultationId) {
        for (int i = 1; i <= records.size(); i++) {
            TreatmentRecord r = records.getEntry(i);
            if (r.getConsultation().getConsultationId().equalsIgnoreCase(consultationId)) {
                records.deleteAt(i);
                return true;
            }
        }
        return false;
    }

    // ------------------- Utility -------------------
    public boolean hasRecords() {
        return !records.isEmpty();
    }

    public int getRecordCount() {
        return records.size();
    }
}
