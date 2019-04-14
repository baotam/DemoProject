package com.demoapp.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.demoapp.domain.Employee;
import com.demoapp.domain.Follower;
import com.demoapp.domain.Tweet;
import com.demoapp.repository.EmployeeRepository;
import com.demoapp.repository.FollowerRepository;
import com.demoapp.repository.TweetRepository;
import com.demoapp.resources.EmployeeResource;
import com.demoapp.resources.FollowerResource;
import com.demoapp.resources.TweetResource;
import com.demoapp.service.EmployeeService;
import com.demoapp.service.TweetService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/employees")
@Transactional
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<EmployeeResource>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResource> employeeResources = employees.stream().map(e -> new EmployeeResource(e)).collect(Collectors.toList());
        return new ResponseEntity<>(employeeResources, HttpStatus.OK);
    }

    @RequestMapping(value = "/{employeeId}", method = RequestMethod.GET)
    public ResponseEntity<EmployeeResource> getEmployee(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        return new ResponseEntity<>(new EmployeeResource(employee), HttpStatus.OK);
    }

    @RequestMapping(value = "/{employeeId}/tweets", method = RequestMethod.GET)
    public ResponseEntity<List<TweetResource>> getTweets(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        List<Tweet> tweetList = employee.getTweetList();
        List<TweetResource> tweets = tweetList.stream().map(tweet -> new TweetResource(tweet)).collect(Collectors.toList());
        return new ResponseEntity<>(tweets, HttpStatus.OK);
    }

    @RequestMapping(value = "/{employeeId}/followers", method = RequestMethod.GET)
    public ResponseEntity<Set<FollowerResource>> getFollowers(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        Preconditions.checkNotNull(employee, "Employee can not be null");
        Set<Follower> followers = employee.getFollowers();
        Set<FollowerResource> followerResourceSet = followers.stream().map(f -> new FollowerResource(f)).collect(Collectors.toSet());
        return new ResponseEntity<>(followerResourceSet, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createEmployee(@RequestBody Employee newEmployee) {
        if (employeeService.isAdmin()) {
            Employee employee = new Employee(newEmployee.getFirstName(), newEmployee.getLastName());
            employeeRepository.save(employee);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity("Only an admin can create users.", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{employeeId}/tweet", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> addTweet(@PathVariable Long employeeId, @RequestBody Tweet newTweet) {
        Tweet tweet = new Tweet(newTweet.getEntry(), newTweet.getHashtag());
        Employee employee = employeeRepository.findById(employeeId).get();
        Preconditions.checkNotNull(employee, "Employee can not be null");
        tweet.setEmployee(employee);
        employee.addTweet(tweet);
        tweet.setLastModifiedAt(Timestamp.from(Instant.now()));
        tweetRepository.save(tweet);
        return new ResponseEntity("Added tweet.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{employeeId}/tweet/{tweetId}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<Void> updateTweet(@PathVariable Long employeeId, @PathVariable Long tweetId, @RequestBody Tweet modifiedTweet) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
        Preconditions.checkNotNull(optionalTweet.isPresent(), "Tweet is not present");
        Tweet tweet = optionalTweet.get();
        if (employeeService.hasAccess(tweet.getEmployee())) {
            tweet.setEntry(modifiedTweet.getEntry());
            tweet.setHashtag(modifiedTweet.getHashtag());
            tweetRepository.save(tweet);
            return new ResponseEntity("Successful.", HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity("User can only update his own tweets.", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{employeeId}/tweet/{tweetId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> deleteTweet(@PathVariable Long employeeId, @PathVariable Long tweetId) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
        Preconditions.checkArgument(optionalTweet.isPresent(), "The given tweet is not present.");
        Tweet tweet = optionalTweet.get();
        if (employeeService.hasAccess(tweet.getEmployee())) {
            tweetRepository.delete(tweet);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity("User can only delete his own tweets.", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{employeeId}/follow", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addFollowers(@PathVariable Long employeeId, @RequestBody Long followerId) {
        Optional<Follower> following = followerRepository.findOneByEmployeeIdAndFollowerId(employeeId, followerId);
        if (!following.isPresent()) {
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            Optional<Employee> follower = employeeRepository.findById(followerId);
            Preconditions.checkArgument(employee.isPresent(), "Employee can not be null");
            Preconditions.checkNotNull(follower.isPresent(), "Follower can not be null");
            Follower fl = new Follower(employee.get(), follower.get());
            followerRepository.save(fl);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity("You have to follow someone only once.", HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "{employeeId}/unfollow", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> deleteFollower(@PathVariable Long employeeId, @RequestBody Long followerId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Preconditions.checkArgument(employee.isPresent(), "Employee is not present.");
        if (employeeService.hasAccess(employee.get())) {
            Optional<Follower> follower = followerRepository.findOneByEmployeeIdAndFollowerId(employeeId, followerId);
            if (follower.isPresent()) {
                followerRepository.delete(follower.get());
                return new ResponseEntity(HttpStatus.OK);
            }
            else {
                return new ResponseEntity("Nothing to Delete", HttpStatus.NO_CONTENT);
            }
        }
        else {
            return new ResponseEntity("User can unfollow only his own followers", HttpStatus.FORBIDDEN);
        }
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{employeeId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteEmployee(@PathVariable Long employeeId) {
        if (employeeService.isAdmin()) {
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            Preconditions.checkArgument(employee.isPresent(), "Employee is not present.");
            List<Follower> followers = followerRepository.findByEmployeeId(employeeId);
            followers.forEach(f -> followerRepository.delete(f));
            employeeRepository.delete(employee.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity("Only an admin can create users.", HttpStatus.FORBIDDEN);
        }
    }

}
