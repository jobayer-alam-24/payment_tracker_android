package com.testapp.paymenttracker;

public class Student {
    private int id;
    private int batchId;
    private String name;

    public Student(int id, int batchId, String name) {
        this.id = id;
        this.batchId = batchId;
        this.name = name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() { return id; }
    public int getBatchId() { return batchId; }
    public String getName() { return name; }
}
