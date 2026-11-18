package dao;

import adt.LinkedList;
import adt.LinkedListInterface;
import entity.Patient;


public class PatientDao {

    private final LinkedListInterface<Patient> patients;

    public PatientDao() {
        patients = new LinkedList<>();
        initializeData();
    }

    private void initializeData() {
        Patient p1 = new Patient();
        p1.setPatientName("Alice Tan");
        p1.setPatientAge(22);
        p1.setSchoolId("2504821");
        p1.setPatientPhNo("0123456789");
        p1.setRegDate("01/08/2025");
        patients.addLast(p1);

        Patient p2 = new Patient();
        p2.setPatientName("John Lim");
        p2.setPatientAge(35);
        p2.setSchoolId("2519276");
        p2.setPatientPhNo("0171234567");
        p2.setRegDate("02/08/2025");
        patients.addLast(p2);

        Patient p3 = new Patient();
        p3.setPatientName("Siti Aminah");
        p3.setPatientAge(28);
        p3.setSchoolId("2501534");
        p3.setPatientPhNo("0138765432");
        p3.setRegDate("03/08/2025");
        patients.addLast(p3);

        Patient p4 = new Patient();
        p4.setPatientName("Michael Wong");
        p4.setPatientAge(41);
        p4.setSchoolId("2598762");
        p4.setPatientPhNo("0192345678");
        p4.setRegDate("04/08/2025");
        patients.addLast(p4);

        Patient p5 = new Patient();
        p5.setPatientName("Nur Aisyah");
        p5.setPatientAge(19);
        p5.setSchoolId("2510459");
        p5.setPatientPhNo("0119988776");
        p5.setRegDate("05/08/2025");
        patients.addLast(p5);

        Patient p6 = new Patient();
        p6.setPatientName("Raj Kumar");
        p6.setPatientAge(50);
        p6.setSchoolId("2506723");
        p6.setPatientPhNo("0146677889");
        p6.setRegDate("06/08/2025");
        patients.addLast(p6);

        Patient p7 = new Patient();
        p7.setPatientName("Lim Mei Ling");
        p7.setPatientAge(32);
        p7.setSchoolId("2512388");
        p7.setPatientPhNo("0163344556");
        p7.setRegDate("07/08/2025");
        patients.addLast(p7);

        Patient p8 = new Patient();
        p8.setPatientName("David Tan");
        p8.setPatientAge(27);
        p8.setSchoolId("2504987");
        p8.setPatientPhNo("0182233445");
        p8.setRegDate("08/08/2025");
        patients.addLast(p8);

        Patient p9 = new Patient();
        p9.setPatientName("Fatimah Binti Ali");
        p9.setPatientAge(44);
        p9.setSchoolId("2517650");
        p9.setPatientPhNo("0121122334");
        p9.setRegDate("09/08/2025");
        patients.addLast(p9);

        Patient p10 = new Patient();
        p10.setPatientName("Andrew Lee");
        p10.setPatientAge(30);
        p10.setSchoolId("2502194");
        p10.setPatientPhNo("0134455667");
        p10.setRegDate("10/08/2025");
        patients.addLast(p10);

        Patient p11 = new Patient();
        p11.setPatientName("Chong Wei");
        p11.setPatientAge(26);
        p11.setSchoolId("2518395");
        p11.setPatientPhNo("0159988776");
        p11.setRegDate("11/08/2025");
        patients.addLast(p11);

        Patient p12 = new Patient();
        p12.setPatientName("Sophia Lim");
        p12.setPatientAge(24);
        p12.setSchoolId("2509342");
        p12.setPatientPhNo("0175566778");
        p12.setRegDate("12/08/2025");
        patients.addLast(p12);

        Patient p13 = new Patient();
        p13.setPatientName("Hafiz Rahman");
        p13.setPatientAge(38);
        p13.setSchoolId("2505671");
        p13.setPatientPhNo("0199988221");
        p13.setRegDate("13/08/2025");
        patients.addLast(p13);

        Patient p14 = new Patient();
        p14.setPatientName("Jessica Wong");
        p14.setPatientAge(29);
        p14.setSchoolId("2510927");
        p14.setPatientPhNo("0187766554");
        p14.setRegDate("14/08/2025");
        patients.addLast(p14);

        Patient p15 = new Patient();
        p15.setPatientName("Mohd Faiz");
        p15.setPatientAge(33);
        p15.setSchoolId("2503846");
        p15.setPatientPhNo("0123344556");
        p15.setRegDate("15/08/2025");
        patients.addLast(p15);
    }

    public LinkedListInterface<Patient> getAllPatients() {
        return patients;
    }
}