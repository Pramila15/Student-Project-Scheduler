package com.pramila.StudentProjectScheduler;

public class Task {
    private int id;
    private String name;
    private String date;

    // Constructor

    public Task(int id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    // Setter, Getter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
