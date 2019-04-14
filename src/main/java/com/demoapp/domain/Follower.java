package com.demoapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Follower {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="follower_id")
    private Employee follower;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    private Follower() {
    }

    public Follower(Employee employee, Employee follower) {
        this.employee = employee;
        this.follower = follower;
    }

    public Long getId() {
        return id;
    }

    public Employee getFollower() {
        return follower;
    }

    public Employee getEmployee() {
        return employee;
    }

}
