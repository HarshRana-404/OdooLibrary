package com.threebrains.odoolibrary.models;

public class LastSixMonthIssueModel {
    String month;
    int issuecount;

    public LastSixMonthIssueModel(String month, int issuecount){
        this.month = month;
        this.issuecount = issuecount;
    }

    public String getMonth() {
        return month;
    }

    public int getIssuecount() {
        return issuecount;
    }
}
