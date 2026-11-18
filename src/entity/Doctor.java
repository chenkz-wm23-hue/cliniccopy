package entity;
import control.Shift;
/**
 *
 * @author user
 */
public class Doctor<T> {
    private String docName;
    private String docID;
    private int phoneNumber;
    private String dept;
    private Shift shift; 
    
   
    public Doctor(String docName, String docID, int phoneNumber, String dept, Shift shift) {
        this.docName = docName;
        this.docID = docID;
        this.phoneNumber = phoneNumber;
        this.dept = dept;
        this.shift = shift;
    }
    
    
    public Doctor(String docName, String docID, int phoneNumber, String dept, String startTime, String endTime, String status, String breakTime) {
        this.docName = docName;
        this.docID = docID;
        this.phoneNumber = phoneNumber;
        this.dept = dept;
        Shift.ShiftStatus st;
        try { st = status == null ? Shift.ShiftStatus.AVAILABLE : Shift.ShiftStatus.valueOf(status.toUpperCase()); }
        catch (Exception e) { st = Shift.ShiftStatus.AVAILABLE; }
        this.shift = new Shift(startTime, endTime, null, breakTime, st);
    }
    
  
    public Doctor(String docName, String docID, int phoneNumber, String dept, String startTime, String endTime, String shiftName, String status, String breakTime) {
        this.docName = docName;
        this.docID = docID;
        this.phoneNumber = phoneNumber;
        this.dept = dept;
        Shift.ShiftStatus st;
        try { st = status == null ? Shift.ShiftStatus.AVAILABLE : Shift.ShiftStatus.valueOf(status.toUpperCase()); }
        catch (Exception e) { st = Shift.ShiftStatus.AVAILABLE; }
        this.shift = new Shift(startTime, endTime, shiftName, breakTime, st);
    }
    
    
    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    
    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }
    
    
    public String getStartTime() {
        return shift != null ? shift.getStartTimeString() : "Not set";
    }
    
    public String getEndTime() {
        return shift != null ? shift.getEndTimeString() : "Not set";
    }
    
    public String getShiftName() {
        return shift != null ? shift.getShiftName() : "Not set";
    }
    
    public double getShiftDuration() {
        return shift != null ? shift.getDurationHours() : 0.0;
    }
    
    public String getShiftStatus() {
        return shift != null && shift.getCurrentStatus() != null ? shift.getCurrentStatus().name() : "AVAILABLE";
    }
    
    public String getBreakTime() {
        return shift != null ? shift.getBreakTime() : null;
    }
    
    
    public void setShiftTime(String startTime, String endTime) {
        if (Shift.isValidTimeFormat(startTime) && Shift.isValidTimeFormat(endTime)) {
            if (this.shift == null) {
                this.shift = new Shift(startTime, endTime);
            } else {
                this.shift.setStartTime(startTime);
                this.shift.setEndTime(endTime);
            }
        }
    }
    
    
    public void setShiftTime(String startTime, String endTime, String shiftName) {
        if (Shift.isValidTimeFormat(startTime) && Shift.isValidTimeFormat(endTime)) {
            if (this.shift == null) {
                this.shift = new Shift(startTime, endTime, shiftName);
            } else {
                this.shift.setStartTime(startTime);
                this.shift.setEndTime(endTime);
                this.shift.setShiftName(shiftName);
            }
        }
    }
    
   
    @Override
    public String toString() {
        return 
                "Doctor Name :  "+ docName +
                "\nDoctor Id: " + docID +
                "\nPhone Number : " + phoneNumber + 
                "\nDepartment : " + dept + 
                "\nShift : " + (shift != null ? shift.toString() : "Not set");
    }
    
   
    public boolean isCurrentlyOnShift() {
        if (shift == null) return false;
        return shift.isWithinShift(java.time.LocalTime.now());
    }
    
    
    public String getShift12HourFormat() {
        return shift != null ? shift.getShift12HourFormat() : "Not set";
    }
}
