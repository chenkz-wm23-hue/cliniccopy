package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LeaveRequest {
    public enum Status { PENDING, APPROVED, REJECTED }

    private static int seq = 1;

    private final String leaveId;   
    private final String doctorId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final String reason;
    private Status status;

    public LeaveRequest(String doctorId, LocalDate dateFrom, LocalDate dateTo, String reason, Status status) {
        this.leaveId = String.format("L%03d", seq++);
        this.doctorId = doctorId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.reason = reason;
        this.status = status == null ? Status.PENDING : status;
    }

    public String getLeaveId() { return leaveId; }
    public String getDoctorId() { return doctorId; }
    public LocalDate getDateFrom() { return dateFrom; }
    public LocalDate getDateTo() { return dateTo; }
    public String getReason() { return reason; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public boolean covers(LocalDate date) {
        return (date.isEqual(dateFrom) || date.isAfter(dateFrom)) &&
               (date.isEqual(dateTo)   || date.isBefore(dateTo));
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("%-6s | %-6s | %-10s -> %-10s | %-10s | %s",
                leaveId, doctorId, dateFrom.format(fmt), dateTo.format(fmt),
                status.name(), reason);
    }
}
