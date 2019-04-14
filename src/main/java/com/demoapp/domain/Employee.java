package com.demoapp.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "employee")
    List<Tweet> tweetList = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    Set<Follower> followers = new HashSet<>();

    private Employee() {
    }

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void addTweet(Tweet tweet) {
        this.tweetList.add(tweet);
    }

    public void addFollower(Follower follower) {
        this.followers.add(follower);
    }

    public List<Tweet> getTweetList() {
        return tweetList;
    }

    public Set<Follower> getFollowers() {
        return followers;
    }
}
