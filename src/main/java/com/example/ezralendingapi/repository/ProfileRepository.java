package com.example.ezralendingapi.repository;

import com.example.ezralendingapi.entities.Subscriber;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ProfileRepository extends JpaRepository<Subscriber,Long> {
    Optional<Subscriber> findByMsisdn(String msisdn);
   Subscriber getByMsisdn(String msisdn);

   @Query(
           value = "SELECT loan_time from subscribers", nativeQuery = true
   )
    Integer getNumberOfLoanTimes();

   @Query(
           value = "SELECT credit_limit FROM  subscribers", nativeQuery = true
   )
    Double getCreditLimitByMsisdn(String msisdn);
}
