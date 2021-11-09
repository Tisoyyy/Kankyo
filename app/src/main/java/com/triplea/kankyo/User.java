package com.triplea.kankyo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

    String reportDescription, reportLocation, reportName;
    Date reportDate;

    public User(){}

    public User(String reportDescription, String reportLocation, Date reportDate, String reportName) {
        this.reportDescription = reportDescription;
        this.reportLocation = reportLocation;
        this.reportDate = reportDate;
        this.reportName = reportName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public String getReportLocation() {
        return reportLocation;
    }

    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
}
