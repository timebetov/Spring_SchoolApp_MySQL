package com.github.timebetov.SchoolApp.repository;

import com.github.timebetov.SchoolApp.model.Holiday;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidaysRepository extends CrudRepository<Holiday, String> {
}
