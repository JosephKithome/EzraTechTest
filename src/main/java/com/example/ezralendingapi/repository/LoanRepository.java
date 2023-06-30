package com.example.ezralendingapi.repository;

import com.example.ezralendingapi.entities.Loan;
import com.example.ezralendingapi.entities.LoanStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findBySubscriberId(Long subscriberId);
    List<Loan> findByStatusAndCreatedAtBefore(LoanStatus status, LocalDateTime cutoffDateTime);
    @Query("SELECT l FROM Loan l WHERE l.status = 'active' AND l.createdAt < :cutoffDateTime")
    List<Loan> findDefaultedLoans(@Param("cutoffDateTime") LocalDateTime cutoffDateTime);

    Loan findSubscriberLoanById(@Param("id") Long id);
}
