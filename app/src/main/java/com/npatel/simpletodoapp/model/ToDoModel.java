package com.npatel.simpletodoapp.model;

public class ToDoModel {
    private int id;
    private String name;
    private String dueDate;
    private String priority;

    public ToDoModel() {
    }

    public ToDoModel(int id, String name, String duedate, String pname) {
        this.id = id;
        this.name = name;
        this.dueDate = duedate;
        this.priority = pname;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

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
}
