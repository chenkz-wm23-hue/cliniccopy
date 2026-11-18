package dao;
import adt.LinkedList;
import adt.LinkedListInterface;
import control.Shift;
import entity.Doctor;


public class DoctorDAO {
    private final LinkedListInterface<Doctor> doctors;

    public DoctorDAO() {
        doctors = new LinkedList<>();

        
        doctors.addLast(new Doctor(
                "Dr. Alice Tan",
                "D001",
                123456789,
                "Cardiology",
                new Shift("09:00", "17:00", "Morning", "12:00-13:00", Shift.ShiftStatus.AVAILABLE)
        ));

        doctors.addLast(new Doctor(
                "Dr. Ben Ong",
                "D002",
                987654321,
                "Orthopedics",
                new Shift("14:00", "22:00", "Evening", "", Shift.ShiftStatus.BUSY)
        ));

        doctors.addLast(new Doctor(
                "Dr. Chloe Lee",
                "D003",
                555123456,
                "Emergency",
                new Shift("22:00", "06:00", "Night", "02:00-02:30", Shift.ShiftStatus.BREAK)
        ));
    }

    public LinkedListInterface<Doctor> getAllDoctors() {
        return doctors;
    }
}
 
