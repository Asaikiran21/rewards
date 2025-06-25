package com.app.rewards.repository;


import com.app.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.date >= :startDate")
    List<Transaction> findTransactionsByDate(@Param("startDate") LocalDate startDate);

    @Query("SELECT t FROM Transaction t WHERE t.date >= :startDate and t.customerId=:customerId")
    List<Transaction> findTransactionByCustomerIdAndDate(@Param("startDate")LocalDate startDate,@Param("customerId")String customerId);
}
