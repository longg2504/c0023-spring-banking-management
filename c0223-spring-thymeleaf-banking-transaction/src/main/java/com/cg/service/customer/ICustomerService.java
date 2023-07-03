package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGeneralService<Customer, Long> {
    List<Customer> findAllByDeletedIsFalse();

    List<Customer> findAllByIdNot(Long id);

    List<Customer> findAllByIdNotAndDeletedIsFalse(Long id);

    void incrementBalance(Long customerId, BigDecimal transactionAmount);

    Deposit deposit(Deposit deposit);

    Transfer transfer(Transfer transfer);
}
