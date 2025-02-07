package com.github.timebetov.SchoolApp.repository;

import com.github.timebetov.SchoolApp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
