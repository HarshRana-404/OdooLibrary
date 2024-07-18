package com.threebrains.odoolibrary.models;

public class RequestedModel {
    String  isbn;
    String title;
    String uid;
    String userName;
    String requestDate;
    String issueDate;
    String dueDate;
    String returnDate;
    String status;
    String docId;
    public RequestedModel(String isbn, String title, String uid, String userName, String requestDate, String issueDate, String dueDate, String returnDate, String status){
        this.isbn = isbn;
        this.title = title;
        this.uid = uid;
        this.userName = userName;
        this.requestDate = requestDate;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }
    public void setDocId(String docId){
        this.docId = docId;
    }
    public String getDocId() {
        return docId;
    }
}
