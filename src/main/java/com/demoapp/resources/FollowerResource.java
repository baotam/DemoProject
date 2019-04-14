package com.demoapp.resources;

import com.demoapp.controller.FollowerController;
import com.demoapp.domain.Employee;
import com.demoapp.domain.Follower;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class FollowerResource extends ResourceSupport {

    public static final String TWEETS = "tweets";

    private String firstName;

    private final String lastName;

    private final String following;

    public FollowerResource(Follower follower) {
        Employee followerEmployeeObject = follower.getFollower();
        Employee followingEmployeeObject = follower.getEmployee();
        this.firstName = followerEmployeeObject.getFirstName();
        this.lastName= followerEmployeeObject.getLastName();
        this.following = followingEmployeeObject.getFirstName();
        this.add(linkTo(methodOn(FollowerController.class).getFollower(follower.getId())).withSelfRel());
        this.add(linkTo(methodOn(FollowerController.class).getTweets(follower.getId())).withRel(TWEETS));
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFollowing() {
        return following;
    }
}
