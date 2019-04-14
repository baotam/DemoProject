package com.demoapp.repository;

import java.util.List;
import java.util.Optional;

import com.demoapp.domain.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower,Long> {

    Optional<Follower> findOneByEmployeeIdAndFollowerId(Long employeeId, Long followerId);

    @Override
    Page<Follower> findAll(Pageable pageable);

    List<Follower> findByFollowerId(Long followeId);

    List<Follower> findByEmployeeId(Long employeeId);
}
