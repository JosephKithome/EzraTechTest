package com.example.ezralendingapi.repository;

import com.example.ezralendingapi.entities.LoanPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanPeriodRepository extends JpaRepository<LoanPeriod, Integer> {

    Optional<LoanPeriod> findByPeriod(Integer loanPeriod);
}
