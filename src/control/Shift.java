package control;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Shift {
    public enum ShiftStatus { AVAILABLE, BUSY, BREAK, OFF }
    private LocalTime startTime;
    private LocalTime endTime;
    private String shiftName; 
    private String breakTime; 
    private ShiftStatus currentStatus = ShiftStatus.AVAILABLE;
    
    
    
    private LocalTime[] parseBreakRange() {
    if (breakTime == null || breakTime.trim().isEmpty() || !breakTime.contains("-")) return null;
    try {
        String[] parts = breakTime.trim().split("-");
        LocalTime bStart = LocalTime.parse(parts[0].trim(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime bEnd   = LocalTime.parse(parts[1].trim(), DateTimeFormatter.ofPattern("HH:mm"));
        return new LocalTime[]{ bStart, bEnd };
    } catch (Exception e) {
        return null;
    }
}


public boolean isOnBreak(LocalTime time) {
    LocalTime[] br = parseBreakRange();
    if (br == null) return false;
    LocalTime bStart = br[0], bEnd = br[1];

    if (bStart.isBefore(bEnd)) { 
        return !time.isBefore(bStart) && !time.isAfter(bEnd);
    } else { 
        return !time.isBefore(bStart) || !time.isAfter(bEnd);
    }
}


public boolean isTimeAvailable(LocalDateTime dt) {
    LocalTime t = dt.toLocalTime();
    return isWithinShift(t) && !isOnBreak(t);
}


public boolean isAvailableAt(LocalDateTime dt, boolean respectStatus) {
    if (!isTimeAvailable(dt)) return false;
    if (!respectStatus) return true;
    return currentStatus == ShiftStatus.AVAILABLE;
}
    
    
    public Shift(String startTime, String endTime) {
        this.startTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
        this.shiftName = generateShiftName();
    }
    
    
    public Shift(String startTime, String endTime, String shiftName) {
        this.startTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
        this.shiftName = shiftName;
    }
    
   
    public Shift(String startTime, String endTime, String shiftName, String breakTime, ShiftStatus status) {
        this.startTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
        this.shiftName = shiftName;
        this.breakTime = breakTime;
        this.currentStatus = status == null ? ShiftStatus.AVAILABLE : status;
    }
    
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public String getShiftName() {
        return shiftName;
    }
    
    public String getBreakTime() {
        return breakTime;
    }
    
    public ShiftStatus getCurrentStatus() {
        return currentStatus;
    }
    
    public String getStartTimeString() {
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    public String getEndTimeString() {
        return endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    
  
    public void setStartTime(String startTime) {
        this.startTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
        this.shiftName = generateShiftName();
    }
    
    public void setEndTime(String endTime) {
        this.endTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
        this.shiftName = generateShiftName();
    }
    
    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }
    
    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }
    
    public void setCurrentStatus(ShiftStatus status) {
        this.currentStatus = status;
    }
    
    
    public boolean isWithinShift(LocalTime time) {
        if (startTime.isBefore(endTime)) {
            
            return !time.isBefore(startTime) && !time.isAfter(endTime);
        } else {
           
            return !time.isBefore(startTime) || !time.isAfter(endTime);
        }
    }
    
   
    public double getDurationHours() {
        if (startTime.isBefore(endTime)) {
            return java.time.Duration.between(startTime, endTime).toHours();
        } else {
           
            return java.time.Duration.between(startTime, endTime.plusHours(24)).toHours();
        }
    }
    
    public ShiftStatus getEffectiveStatus(LocalTime time) {
        if (!isWithinShift(time)) {
            return ShiftStatus.OFF;
        }
        return currentStatus;
    }
    
    
    private String generateShiftName() {
        int startHour = startTime.getHour();
        
        if (startHour >= 6 && startHour < 12) {
            return "Morning";
        } else if (startHour >= 12 && startHour < 17) {
            return "Afternoon";
        } else if (startHour >= 17 && startHour < 22) {
            return "Evening";
        } else {
            return "Night";
        }
    }
    
  
    public static boolean isValidTimeFormat(String time) {
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
   
    public boolean isValidShift() {
        return startTime != null && endTime != null;
    }
    
    @Override
    public String toString() {
        return shiftName + " Shift: " + getStartTimeString() + " - " + getEndTimeString() +
               " (" + String.format("%.1f", getDurationHours()) + " hours)" +
               (breakTime != null && !breakTime.isEmpty() ? ", Break: " + breakTime : "") +
               ", Status: " + (currentStatus == null ? ShiftStatus.AVAILABLE : currentStatus);
    }
    
   
    
    public String getShift12HourFormat() {
        DateTimeFormatter formatter12 = DateTimeFormatter.ofPattern("hh:mm a");
        return startTime.format(formatter12) + " - " + endTime.format(formatter12);
    }
}
