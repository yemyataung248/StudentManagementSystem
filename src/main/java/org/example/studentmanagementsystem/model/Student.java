package org.example.studentmanagementsystem.model;


import javafx.beans.property.*;

import java.time.Instant;

public class Student {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty major;
    private final IntegerProperty age;

    public Student() {
        this(0, "", "", 0, "");
    }

    public Student(String name, String email, int age, String major) {
        this(0, name, email, age, major);
    }

    public Student(int id, String name, String email, int age, String major) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.age = new SimpleIntegerProperty(age);
        this.major = new SimpleStringProperty(major);
    }
    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty majorProperty() { return major; }

    // Value getters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getEmail() { return email.get(); }
    public int getAge() { return age.get(); }
    public String getMajor() { return major.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setEmail(String email) { this.email.set(email); }
    public void setAge(int age) { this.age.set(age); }
    public void setMajor(String major) { this.major.set(major); }

    @Override
    public String toString() {
        return "Student{id=" + getId() + ", name='" + getName() + "', email='" + getEmail() +
                "', age=" + getAge() + ", major='" + getMajor() + "'}";
    }
}