package com.myappcompany.rob.employeemanagementapplication20.Entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.myappcompany.rob.employeemanagementapplication20.Adapter.DateConverter;

import java.util.Date;

@Entity(tableName = "timeentries")
public class TimeEntries {

    @PrimaryKey(autoGenerate = true)
    private int recordID;

    private Long clockInTime;
    private Long clockOutTime;
    private Double totalHours;

    // Add a Date field to store the date
    @TypeConverters(DateConverter.class)
    private Date date;

    private int employeeID;

    public TimeEntries() {
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public Long getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(Long clockInTime) {
        this.clockInTime = clockInTime;
    }

    public Long getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(Long clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
}
