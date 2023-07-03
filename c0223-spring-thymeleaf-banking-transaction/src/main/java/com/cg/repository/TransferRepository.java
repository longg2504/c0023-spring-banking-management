package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer,Long> {
    List<Transfer> findAllBySender(Customer sender);

    @Query("SELECT SUM(tr.feesAmount) FROM Transfer AS tr")
    BigDecimal getAllFeesAmount();
}
