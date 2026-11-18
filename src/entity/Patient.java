/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

public class Patient {
    static int idCounter = 1;
            
    private String patientName;
    private String schoolId;
    private String patientId;
    private String patientPhNo;
    private String regDate;
    private int patientAge;
    private String gender;

    public Patient() {
        this.patientId = String.format("P%03d", idCounter++);
        this.patientName = patientName;
        this.schoolId = schoolId;
        this.patientId = patientId;
        this.patientPhNo = patientPhNo;
        this.regDate = regDate;
        this.patientAge = patientAge;
        this.gender = gender;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Patient.idCounter = idCounter;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientPhNo() {
        return patientPhNo;
    }

    public void setPatientPhNo(String patientPhNo) {
        this.patientPhNo = patientPhNo;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }


    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }
    
    public String getGender(){ 
        return gender; 
    }        
    public void setGender(String gender) { 
         this.gender = gender;
    }  

    @Override
     public String toString() {
        return String.format("%-10s %-20s %-5d %-5s %-15s %-15s %-10s",
                patientId, patientName, patientAge, gender, schoolId, patientPhNo, regDate);
    }
    
     
}
