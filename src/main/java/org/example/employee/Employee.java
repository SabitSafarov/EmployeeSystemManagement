package org.example.employee;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Employee implements Serializable {

    private Integer id;
    private String fullName;
    private Date dateBirth;
    private String gender;
    private String phoneNumber;
    private String jobTitle;
    private String department;
    private String supervisor;
    private Date hireDate;
    private double salary;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public Employee(Integer id, String fullName, Date dateBirth, String gender, String phoneNumber, String jobTitle,
                    String department, String supervisor, Date hireDate, double salary) {
        this.id = id;
        this.fullName = fullName;
        this.dateBirth = dateBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.jobTitle = jobTitle;
        this.department = department;
        this.supervisor = supervisor;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nФИО: " + fullName + "\nДата рождения: " + formatter.format(dateBirth) + "\nПол: " + gender +
                "\nНомер телефона: " + phoneNumber + "\nДолжность: " + jobTitle + "\nОтдел: " + department +
                "\nНачальник: " + supervisor + "\nДата приема на работу: " + formatter.format(hireDate) + "\nЗарплата: " + salary + " рублей\n";
    }
}
