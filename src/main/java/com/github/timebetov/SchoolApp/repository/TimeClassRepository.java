package com.github.timebetov.SchoolApp.repository;

import com.github.timebetov.SchoolApp.model.TimeClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeClassRepository extends JpaRepository<TimeClass, Integer> {
}
