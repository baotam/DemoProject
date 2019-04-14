package com.demoapp.repository;

import java.util.List;

import com.demoapp.domain.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    Page<Tweet> findByOrderByLastModifiedAtDesc(Pageable pageable);

    List<Tweet> findByHashtag(String hashtag);

    List<Tweet> findByEmployeeId(Long employeeId);


}
