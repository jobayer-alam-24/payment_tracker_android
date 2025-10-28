package com.testapp.paymenttracker;

public class Batch {
    private int id;
    private String name;
    private String startDate;
    private double fee;

    public Batch(int id, String name, String startDate, double fee) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.fee = fee;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getStartDate() { return startDate; }
    public double getFee() { return fee; }
}
