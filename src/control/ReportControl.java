package control;

import adt.LinkedList;
import adt.LinkedListInterface;
import entity.Consultation;
import entity.Doctor;
import entity.Patient;
import entity.TreatmentRecord;

public class ReportControl {

    private final ConsultationControl consultations;
    private final DoctorControl doctors;
    private final PatientControl patients;
    private final TreatmentControl treatments;

    public ReportControl(ConsultationControl c, DoctorControl d, PatientControl p, TreatmentControl t) {
        this.consultations = c;
        this.doctors = d;
        this.patients = p;
        this.treatments = t;
    }

    
    public void printClinicOperationsSummary() {
        LinkedListInterface<Consultation> cons = consultations.getAllConsultations();
        LinkedListInterface<Doctor> docs = doctors.getAllDoctors();
        adt.LinkedListInterface<Patient> pats = patients.getAllPatients();
        adt.LinkedListInterface<entity.QueueEntry> queue = patients.getQueueSnapshot();

        System.out.println("\nGenerated on: " + java.time.LocalDate.now() + " " + java.time.LocalTime.now());
        System.out.println("TUNKU ABDUL RAHMAN UNIVERSITY OF MANAGEMENT AND TECHNOLOGY");
        System.out.println("CLINIC OPERATIONS SUMMARY REPORT");
        line(100);

        System.out.printf("%-8s | %-18s | %-12s | %-10s%n", "DoctorID", "Doctor Name", "Department", "#Consults");
        line(100);

        int n = cons.size();
        String[] docIds = new String[n];
        int[] counts = new int[n];
        int u = 0;

        for (int i = 1; i <= n; i++) {
            String did = cons.getEntry(i).getDoctorId();
            u = addOrInc(docIds, counts, u, did);
        }

        for (int i = 1; i <= docs.size(); i++) {
            Doctor d = docs.getEntry(i);
            int c = findCount(docIds, counts, u, d.getDocID());
            System.out.printf("%-8s | %-18s | %-12s | %-10d%n",
                    d.getDocID(), d.getDocName(), d.getDept(), c);
        }
        line(100);

        System.out.println("Total Doctors          : " + docs.size());
        System.out.println("Total Patients         : " + pats.size());
        System.out.println("Total Consultations    : " + cons.size());
        System.out.println("Patients in Queue      : " + (queue == null ? 0 : queue.size()));

        System.out.println();
        line(100);
        System.out.println(center("GRAPHICAL REPRESENTATION", 100));
        line(100);

        
        String[] depts = new String[docs.size()];
        int[] deptCounts = new int[docs.size()];
        int du = 0;
        for (int i = 1; i <= cons.size(); i++) {
            String dept = lookupDept((LinkedList<Doctor>)docs, cons.getEntry(i).getDoctorId());
            if (dept == null) {
                dept = "(Unknown)";
            }
            du = addOrInc(depts, deptCounts, du, dept);
        }
        System.out.println("\nConsultations by Department");
        printVerticalBarChart(depts, deptCounts, du, "Departments");

       
        System.out.println("\nConsultations by Doctor");
        printVerticalBarChart(docIds, counts, u, "Doctors");

        line(100);
    }

    private String lookupDept(LinkedList<Doctor> docs, String doctorId) {
        for (int i = 1; i <= docs.size(); i++) {
            Doctor d = docs.getEntry(i);
            if (d.getDocID().equals(doctorId)) {
                return d.getDept();
            }
        }
        return null;
    }

    
    public void printClinicalInsightsSummary() {
        LinkedListInterface<Consultation> cons = consultations.getAllConsultations();
        LinkedListInterface<TreatmentRecord> trs = treatments.getAllRecords();

        System.out.println("\nGenerated on: " + java.time.LocalDate.now() + " " + java.time.LocalTime.now());
        System.out.println("CLINICAL INSIGHTS SUMMARY REPORT");
        line(100);

        System.out.printf("%-6s | %-8s | %-8s | %-10s | %-5s | %-18s | %-18s%n",
                "CID", "Doctor", "Patient", "Date", "Time", "Diagnosis", "Treatment");
        line(100);

        for (int i = 1; i <= cons.size(); i++) {
            Consultation c = cons.getEntry(i);
            TreatmentRecord tr = findTRByConsultId((LinkedList<TreatmentRecord>) trs, c.getConsultationId());
            String diag = tr == null ? "-" : limit(tr.getDiagnosis(), 18);
            String med = tr == null ? "-" : limit(tr.getTreatment(), 18);

            System.out.printf("%-6s | %-8s | %-8s | %-10s | %-5s | %-18s | %-18s%n",
                    c.getConsultationId(), c.getDoctorId(), c.getPatientId(),
                    c.getDate(), c.getConsultationTime(), diag, med);
        }
        line(100);

        int tn = trs.size();
        String[] diagKeys = new String[tn];
        int[] diagCounts = new int[tn];
        int du = 0;

        String[] medKeys = new String[tn];
        int[] medCounts = new int[tn];
        int mu = 0;

        for (int i = 1; i <= tn; i++) {
            TreatmentRecord tr = trs.getEntry(i);
            if (tr == null) {
                continue;
            }
            String d = nz(tr.getDiagnosis());
            String m = nz(tr.getTreatment());
            du = addOrInc(diagKeys, diagCounts, du, d);
            mu = addOrInc(medKeys, medCounts, mu, m);
        }

        System.out.println();
        line(100);
        System.out.println(center("GRAPHICAL REPRESENTATION", 100));
        line(100);
        System.out.println("\nTop Diagnoses");
        printVerticalBarChart(diagKeys, diagCounts, du, "Diagnoses");

        System.out.println("\nTop Medicines / Treatments");
        printVerticalBarChart(medKeys, medCounts, mu, "Treatments");

        System.out.println();
        line(100);
        System.out.println("Scheduled Follow-ups:");
        boolean any = false;
        for (int i = 1; i <= cons.size(); i++) {
            Consultation c = cons.getEntry(i);
            if (c.isFollowUpNeeded()) {
                any = true;
                System.out.println("  • " + c.getConsultationId() + "  on  " + c.getFollowUpDate()
                        + "  (Doctor " + c.getDoctorId() + " / Patient " + c.getPatientId() + ")");
            }
        }
        if (!any) {
            System.out.println("  (none)");
        }
        line(100);
    }

    // ================== helpers ==================
    private int addOrInc(String[] keys, int[] counts, int u, String key) {
        if (key == null) {
            key = "(Unknown)";
        }
        for (int i = 0; i < u; i++) {
            if (keys[i].equals(key)) {
                counts[i]++;
                return u;
            }
        }
        keys[u] = key;
        counts[u] = 1;
        return u + 1;
    }

    private int findCount(String[] keys, int[] counts, int u, String key) {
        for (int i = 0; i < u; i++) {
            if (keys[i].equals(key)) {
                return counts[i];
            }
        }
        return 0;
    }

    private TreatmentRecord findTRByConsultId(LinkedList<TreatmentRecord> trs, String cid) {
        for (int i = 1; i <= trs.size(); i++) {
            TreatmentRecord tr = trs.getEntry(i);
            if (tr.getConsultation().getConsultationId().equals(cid)) {
                return tr;
            }
        }
        return null;
    }

    private String limit(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }

    private String nz(String s) {
        return (s == null || s.isBlank()) ? "(Unknown)" : s;
    }

    private String center(String s, int w) {
        int pad = (w - s.length()) / 2;
        if (pad < 0) {
            pad = 0;
        }
        return " ".repeat(pad) + s;
    }

    private void line(int n) {
        System.out.println("-".repeat(n));
    }

    
    private void printVerticalBarChart(String[] labels, int[] counts, int used, String xAxisName) {
        if (used == 0) {
            System.out.println("(no data)");
            return;
        }
        int max = 0;
        for (int i = 0; i < used; i++) {
            if (counts[i] > max) {
                max = counts[i];
            }
        }

       
        for (int level = max; level >= 1; level--) {
            System.out.printf("%2d | ", level);   // y axis label
            for (int i = 0; i < used; i++) {
                if (counts[i] >= level) {
                    System.out.print(" #   ");   // bar block
                } else {
                    System.out.print("     ");   // spacing
                }
            }
            System.out.println();
        }

        
        System.out.print("   +");
        for (int i = 0; i < used; i++) {
            System.out.print("-----");
        }
        System.out.println("> " + xAxisName);

        
        System.out.print("     ");
        for (int i = 0; i < used; i++) {
            System.out.printf("%-5s", labels[i]);
        }
        System.out.println();
    }

    public void printDiagnosisSummary() {
        LinkedListInterface<TreatmentRecord> trs = treatments.getAllRecords();

        System.out.println("\nGenerated on: " + java.time.LocalDate.now() + " " + java.time.LocalTime.now());
        System.out.println("DIAGNOSIS SUMMARY REPORT");
        line(100);

        System.out.printf("%-6s | %-18s%n", "CID", "Diagnosis");
        line(100);

        for (int i = 1; i <= trs.size(); i++) {
            TreatmentRecord tr = trs.getEntry(i);
            System.out.printf("%-6s | %-18s%n",
                    tr.getConsultation().getConsultationId(),
                    limit(nz(tr.getDiagnosis()), 18));
        }
        line(100);

       
        int tn = trs.size();
        String[] diagKeys = new String[tn];
        int[] diagCounts = new int[tn];
        int du = 0;

        for (int i = 1; i <= tn; i++) {
            TreatmentRecord tr = trs.getEntry(i);
            if (tr == null) {
                continue;
            }
            du = addOrInc(diagKeys, diagCounts, du, nz(tr.getDiagnosis()));
        }

        System.out.println();
        line(100);
        System.out.println(center("GRAPHICAL REPRESENTATION", 100));
        line(100);
        System.out.println("\nTop Diagnoses");
        printVerticalBarChart(diagKeys, diagCounts, du, "Diagnoses");
        line(100);
    }

    public void printMedicineUsageSummary() {
        LinkedListInterface<TreatmentRecord> trs = treatments.getAllRecords();

        System.out.println("\nGenerated on: " + java.time.LocalDate.now() + " " + java.time.LocalTime.now());
        System.out.println("MEDICINE USAGE SUMMARY REPORT");
        line(100);

        System.out.printf("%-6s | %-18s%n", "CID", "Treatment");
        line(100);

        for (int i = 1; i <= trs.size(); i++) {
            TreatmentRecord tr = trs.getEntry(i);
            System.out.printf("%-6s | %-18s%n",
                    tr.getConsultation().getConsultationId(),
                    limit(nz(tr.getTreatment()), 18));
        }
        line(100);

       
        int tn = trs.size();
        String[] medKeys = new String[tn];
        int[] medCounts = new int[tn];
        int mu = 0;

        for (int i = 1; i <= tn; i++) {
            TreatmentRecord tr = trs.getEntry(i);
            if (tr == null) {
                continue;
            }
            mu = addOrInc(medKeys, medCounts, mu, nz(tr.getTreatment()));
        }

        System.out.println();
        line(100);
        System.out.println(center("GRAPHICAL REPRESENTATION", 100));
        line(100);
        System.out.println("\nTop Medicines / Treatments");
        printVerticalBarChart(medKeys, medCounts, mu, "Treatments");
        line(100);
    }

    public void printDoctorPatientEngagementReport() {
        LinkedListInterface<Consultation> cons = consultations.getAllConsultations();
        LinkedListInterface<Doctor> docs = doctors.getAllDoctors();

        System.out.println("\nGenerated on: " + java.time.LocalDate.now() + " " + java.time.LocalTime.now());
        System.out.println("DOCTOR-PATIENT ENGAGEMENT REPORT");
        line(100);

        if (cons.size() == 0 || docs.size() == 0) {
            System.out.println("(no data)");
            line(100);
            return;
        }

        
        int maxDocs = docs.size();
        String[] docIds = new String[maxDocs];
        String[] docNames = new String[maxDocs];
        String[] docDepts = new String[maxDocs];
        int usedDocs = 0;

        
        for (int i = 1; i <= docs.size(); i++) {
            Doctor d = docs.getEntry(i);
            docIds[usedDocs] = d.getDocID();
            docNames[usedDocs] = d.getDocName();
            docDepts[usedDocs] = d.getDept();
            usedDocs++;
        }

        
        int[] totalConsults = new int[usedDocs];
        int[] uniquePatientCounts = new int[usedDocs];
        String[][] uniquePatients = new String[usedDocs][cons.size()];

        
        for (int i = 1; i <= cons.size(); i++) {
            Consultation c = cons.getEntry(i);
            String did = c.getDoctorId();
            String pid = c.getPatientId();

            int di = -1;
            for (int j = 0; j < usedDocs; j++) {
                if (docIds[j].equals(did)) { di = j; break; }
            }
            if (di < 0) continue; 

            totalConsults[di]++;

            
            boolean seen = false;
            for (int k = 0; k < uniquePatientCounts[di]; k++) {
                if (uniquePatients[di][k].equals(pid)) { seen = true; break; }
            }
            if (!seen) {
                uniquePatients[di][uniquePatientCounts[di]] = pid;
                uniquePatientCounts[di]++;
            }
        }

        System.out.printf("%-8s | %-18s | %-12s | %-14s | %-10s | %-10s%n",
                "DoctorID", "Doctor Name", "Department", "Unique Patients", "#Consults", "Avg/Patient");
        line(100);

        double[] averages = new double[usedDocs];
        for (int i = 0; i < usedDocs; i++) {
            int u = uniquePatientCounts[i];
            int t = totalConsults[i];
            double avg = (u == 0) ? 0.0 : ((double) t) / u;
            averages[i] = avg;
            System.out.printf("%-8s | %-18s | %-12s | %-14d | %-10d | %-10.2f%n",
                    docIds[i], limit(docNames[i], 18), limit(docDepts[i], 12), u, t, avg);
        }
        line(100);

       
        int widestIdx = -1; int widestVal = -1;
        int repeatIdx = -1; double repeatVal = -1.0;
        for (int i = 0; i < usedDocs; i++) {
            if (uniquePatientCounts[i] > widestVal) { widestVal = uniquePatientCounts[i]; widestIdx = i; }
            if (averages[i] > repeatVal) { repeatVal = averages[i]; repeatIdx = i; }
        }
        System.out.println("Highlights:");
        if (widestIdx >= 0)
            System.out.println(" Widest patient base: " + docIds[widestIdx] + " (" + docNames[widestIdx] + ") with " + widestVal + " unique patients");
        if (repeatIdx >= 0)
            System.out.println("  Most repeat patients: " + docIds[repeatIdx] + " (" + docNames[repeatIdx] + ") with avg " + String.format("%.2f", repeatVal) + " consults/patient");

       
        System.out.println();
        line(100);
        System.out.println(center("GRAPHICAL REPRESENTATION", 100));
        line(100);
        System.out.println("\nUnique Patients by Doctor");
        printVerticalBarChart(docIds, uniquePatientCounts, usedDocs, "Doctors");

        System.out.println("\nConsultations by Doctor");
        printVerticalBarChart(docIds, totalConsults, usedDocs, "Doctors");

        line(100);
    }

    public void printDoctorPrescriptionReport() {
        LinkedListInterface<Consultation> cons = consultations.getAllConsultations();
        LinkedListInterface<TreatmentRecord> trs = treatments.getAllRecords();
        LinkedListInterface<Doctor> docs = doctors.getAllDoctors();

        System.out.println("\nGenerated on: " + java.time.LocalDate.now() + " " + java.time.LocalTime.now());
        System.out.println("DOCTOR PRESCRIPTION & MEDICINE REPORT");
        line(100);

        if (trs.size() == 0 || docs.size() == 0) {
            System.out.println("(no data)");
            line(100);
            return;
        }

        
        int dCount = docs.size();
        String[] docIds = new String[dCount];
        String[] docNames = new String[dCount];
        for (int i = 1; i <= dCount; i++) {
            Doctor d = docs.getEntry(i);
            docIds[i - 1] = d.getDocID();
            docNames[i - 1] = d.getDocName();
        }

      
        String[][] medKeys = new String[dCount][trs.size()];
        int[][] medCounts = new int[dCount][trs.size()];
        int[] usedPerDoctor = new int[dCount];

        for (int i = 1; i <= trs.size(); i++) {
            TreatmentRecord tr = trs.getEntry(i);
            if (tr == null || tr.getConsultation() == null) continue;
            String did = tr.getConsultation().getDoctorId();
            String med = nz(tr.getTreatment());
            
            int di = -1;
            for (int j = 0; j < dCount; j++) {
                if (docIds[j].equals(did)) { di = j; break; }
            }
            if (di < 0) continue; 

           
            int u = usedPerDoctor[di];
            boolean found = false;
            for (int k = 0; k < u; k++) {
                if (medKeys[di][k].equals(med)) { medCounts[di][k]++; found = true; break; }
            }
            if (!found) {
                medKeys[di][u] = med;
                medCounts[di][u] = 1;
                usedPerDoctor[di] = u + 1;
            }
        }

        
        for (int i = 0; i < dCount; i++) {
            System.out.println();
            System.out.println(docIds[i] + " - " + docNames[i]);
            line(60);
            int u = usedPerDoctor[i];
            if (u == 0) {
                System.out.println("(no prescriptions)");
                continue;
            }
            System.out.printf("%-20s | %-6s%n", "Medicine/Treatment", "Count");
            System.out.println("---------------------+--------");
           
            for (int a = 0; a < u - 1; a++) {
                int maxIdx = a;
                for (int b = a + 1; b < u; b++) {
                    if (medCounts[i][b] > medCounts[i][maxIdx]) maxIdx = b;
                }
                if (maxIdx != a) {
                    int tc = medCounts[i][a]; medCounts[i][a] = medCounts[i][maxIdx]; medCounts[i][maxIdx] = tc;
                    String tk = medKeys[i][a]; medKeys[i][a] = medKeys[i][maxIdx]; medKeys[i][maxIdx] = tk;
                }
            }
            for (int k = 0; k < u; k++) {
                System.out.printf("%-20s | %-6d%n", limit(medKeys[i][k], 20), medCounts[i][k]);
            }
        }

       
        System.out.println();
        line(100);
        System.out.println("Highlights:");
        String bestDoc = null, bestMed = null; int bestCount = 0; String bestDocName = null;
        for (int i = 0; i < dCount; i++) {
            for (int k = 0; k < usedPerDoctor[i]; k++) {
                if (medCounts[i][k] > bestCount) {
                    bestCount = medCounts[i][k];
                    bestDoc = docIds[i];
                    bestDocName = docNames[i];
                    bestMed = medKeys[i][k];
                }
            }
        }
        if (bestCount > 0) {
            System.out.println(" Highest single-medicine usage: " + bestDoc + " (" + bestDocName + ") - " + bestMed + " x" + bestCount);
        } else {
            System.out.println(" (no prescribing activity found)");
        }
        line(100);
    }

}
