package control;

import adt.LinkedList;
import adt.LinkedListInterface;
import dao.DoctorDAO;
import entity.Doctor;
import entity.LeaveRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DoctorControl {
  private LinkedListInterface<Doctor> doctorList;
  private LinkedList<LeaveRequest> leaveList = new LinkedList<>(); // NEW

  public DoctorControl(){
      doctorList = new LinkedList<>();
      try {
          DoctorDAO dao = new DoctorDAO();
          LinkedListInterface<Doctor> seeded = dao.getAllDoctors();
          for (int i = 1; i <= seeded.size(); i++) {
              doctorList.addLast(seeded.getEntry(i));
          }
      } catch (Exception ignored) {}
  }


  public void addDoctor(Doctor d){ doctorList.addLast(d); }

  public void displayDoctor(){
      if (doctorList.isEmpty()){
          System.out.println("There is no doctors available");
          return;
      }
      System.out.println("\n================================= Doctor List =================================");
      System.out.printf("%-10s %-20s %-15s %-15s %-25s %-12s %-12s %-10s %-15s\n",
               "ID", "Doctor Name", "Phone Number", "Department", "Shift Name", "Start", "End", "Status", "Break");
      System.out.println("-----------------------------------------------------------------------------------------------");
      for (int i = 1; i <= doctorList.size(); i++) {
          Doctor d = doctorList.getEntry(i);
          System.out.printf("%-10s %-20s %-15s %-15s %-25s %-12s %-12s %-10s %-15s\n",
                  d.getDocID(), d.getDocName(), d.getPhoneNumber(), d.getDept(),
                  d.getShiftName(), d.getStartTime(), d.getEndTime(),
                  d.getShiftStatus(), d.getBreakTime() == null ? "" : d.getBreakTime());
      }
      System.out.println("================================================================================");
  }

  public Doctor searchDoctor(String docID){
      for (int i = 1; i <= doctorList.size(); i++){
          Doctor d = doctorList.getEntry(i);
          if (d.getDocID().equals(docID)){ return d; }
      }
      return null;
  }

  public boolean updateDoctor(String docID, String newName, int newPhone, String newDept,
                              String newStartTime, String newEndTime, String newShiftName,
                              String newStatus, String newBreakTime) {
      Doctor d = searchDoctor(docID);
      if (d != null) {
          d.setDocName(newName);
          d.setPhoneNumber(newPhone);
          d.setDept(newDept);
          if (newStartTime != null && newEndTime != null) {
              if (d.getShift() == null) {
                  Shift.ShiftStatus st;
                  try { st = newStatus == null ? Shift.ShiftStatus.AVAILABLE : Shift.ShiftStatus.valueOf(newStatus.toUpperCase()); }
                  catch (Exception e) { st = Shift.ShiftStatus.AVAILABLE; }
                  d.setShift(new Shift(newStartTime, newEndTime, newShiftName, newBreakTime, st));
              } else {
                  d.getShift().setStartTime(newStartTime);
                  d.getShift().setEndTime(newEndTime);
                  if (newShiftName != null && !newShiftName.isEmpty()) d.getShift().setShiftName(newShiftName);
                  if (newBreakTime != null) d.getShift().setBreakTime(newBreakTime);
                  if (newStatus != null) {
                      try { d.getShift().setCurrentStatus(Shift.ShiftStatus.valueOf(newStatus.toUpperCase())); } catch (Exception ignored) {}
                  }
              }
          }
          return true;
      }
      return false;
  }

  public boolean deleteDoctor(String docID) {
      for (int i = 1; i <= doctorList.size(); i++) {
          Doctor d = doctorList.getEntry(i);
          if (d.getDocID().equals(docID)) {
              doctorList.deleteAt(i);
              return true;
          }
      }
      return false;
  }

  public boolean isEmpty(){ return doctorList.isEmpty(); }


  private LocalDate parseDate(String s) {
      try { return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")); }
      catch (DateTimeParseException e) { return null; }
  }

  public boolean applyLeave(String docID, String fromDateStr, String toDateStr, String reason) {
      Doctor d = searchDoctor(docID);
      if (d == null) {
          System.out.println("Doctor " + docID + " not found.");
          return false;
      }
      LocalDate from = parseDate(fromDateStr);
      LocalDate to   = parseDate(toDateStr);
      if (from == null || to == null || to.isBefore(from)) {
          System.out.println("Invalid dates. Use yyyy-MM-dd and ensure TO >= FROM.");
          return false;
      }
      
      for (int i = 1; i <= leaveList.size(); i++) {
          LeaveRequest lr = leaveList.getEntry(i);
          if (lr.getDoctorId().equals(docID)) {
              boolean overlap = !(to.isBefore(lr.getDateFrom()) || from.isAfter(lr.getDateTo()));
              if (overlap && lr.getStatus() != LeaveRequest.Status.REJECTED) {
                  System.out.println("Leave overlaps with " + lr.getLeaveId() + " (" + lr.getStatus() + ").");
                  return false;
              }
          }
      }
      LeaveRequest req = new LeaveRequest(docID, from, to, reason, LeaveRequest.Status.APPROVED); // auto-approve
      leaveList.addLast(req);
      System.out.println("Leave submitted: " + req.getLeaveId() + " (APPROVED)");
      return true;
  }

  public void displayLeavesByDoctor(String docID) {
      boolean any = false;
      System.out.printf("%-6s | %-6s | %-10s -> %-10s | %-10s | %s%n",
              "LID", "DocID", "From", "To", "Status", "Reason");
      for (int i = 1; i <= leaveList.size(); i++) {
          LeaveRequest lr = leaveList.getEntry(i);
          if (lr.getDoctorId().equals(docID)) {
              System.out.println(lr.toString());
              any = true;
          }
      }
      if (!any) System.out.println("(no leave records)");
  }

  public boolean isDoctorOnLeave(String docID, LocalDate date) {
      for (int i = 1; i <= leaveList.size(); i++) {
          LeaveRequest lr = leaveList.getEntry(i);
          if (lr.getDoctorId().equals(docID) &&
              lr.getStatus() == LeaveRequest.Status.APPROVED &&
              lr.covers(date)) {
              return true;
          }
      }
      return false;
  }


  public void displayDoctorShortList() {
      if (doctorList.isEmpty()) {
          System.out.println("No doctors available.");
          return;
      }
      System.out.println("\n#  | ID     | Name               | Dept         | Shift       | Start  | End    | Status  | Break");
      System.out.println("----+--------+--------------------+--------------+-------------+--------+--------+---------+----------------");
      for (int i = 1; i <= doctorList.size(); i++) {
          Doctor d = doctorList.getEntry(i);
          System.out.printf("%-3d| %-6s | %-18s | %-12s | %-11s | %-6s | %-6s | %-7s | %s%n",
                  i, d.getDocID(), d.getDocName(), d.getDept(), d.getShiftName(),
                  d.getStartTime(), d.getEndTime(), d.getShiftStatus(),
                  (d.getBreakTime() == null ? "" : d.getBreakTime()));
      }
  }

  public Doctor getDoctorByIndex(int index) {
      if (index < 1 || index > doctorList.size()) return null;
      return doctorList.getEntry(index);
  }

  public boolean isDoctorTimeAvailableNow(Doctor d, boolean respectStatus) {
      if (d == null || d.getShift() == null) return false;
      return d.getShift().isAvailableAt(LocalDateTime.now(), respectStatus);
  }

  public int getDoctorCount() { return doctorList.size(); }

  public adt.LinkedListInterface<entity.Doctor> getAllDoctors() { return doctorList; }
}
