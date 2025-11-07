package edu.jpa.entity;

import edu.jpa.entity.embeddables.Project;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ManyToOne;
import java.util.List;

@Entity
public class Employee {
    @Id
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ElementCollection
    @CollectionTable(name = "employee_project", joinColumns = @JoinColumn(name = "employee_id"))
    private List<Project> projects;

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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
