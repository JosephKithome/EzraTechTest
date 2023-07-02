package com.example.ezralendingapi.repository;

import com.example.ezralendingapi.entities.LoanRepayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {
}
