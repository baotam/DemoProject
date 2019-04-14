package com.demoapp.resources;

import java.util.ArrayList;
import java.util.List;

import com.demoapp.controller.EmployeeController;
import com.demoapp.domain.Employee;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class EmployeeResource extends ResourceSupport {

    public static final String FOLLOW = "follow";

    public static final String UNFOLLOW = "unfollow";

    public static final String TWEET = "addTweet";

    public static final String TWEETS = "viewTweets";

    private final int numberOfFollowers;

    private final int numberOfTweets;

    private String firstName;

    private final String lastName;

    private List<String> tweets = new ArrayList<>();

    public EmployeeResource(Employee employee) {
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.numberOfFollowers = employee.getFollowers().size();
        this.numberOfTweets = employee.getTweetList().size();
        this.add(linkTo(methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel());
        this.add(linkTo(methodOn(EmployeeController.class).addFollowers(employee.getId(), null)).withRel(FOLLOW));
        this.add(linkTo(methodOn(EmployeeController.class).deleteFollower(employee.getId(), null)).withRel(UNFOLLOW));
        this.add(linkTo(methodOn(EmployeeController.class).addTweet(employee.getId(), null)).withRel(TWEET));
        this.add(linkTo(methodOn(EmployeeController.class).getTweets(employee.getId())).withRel(TWEETS));
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public int getNumberOfTweets() {
        return numberOfTweets;
    }
}