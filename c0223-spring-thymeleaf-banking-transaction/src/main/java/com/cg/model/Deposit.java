package com.cg.model;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "deposits")
public class Deposit extends BaseEntity implements Validator{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    referencedColumnName: Tham chiếu tới cột ID của bảng customers
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Column(name = "transaction_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;

    public Deposit() {
    }

    public Deposit(Long id, Customer customer, BigDecimal transactionAmount) {
        this.id = id;
        this.customer = customer;
        this.transactionAmount = transactionAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Deposit deposit = (Deposit) target;

        BigDecimal transactionAmount = deposit.transactionAmount;
        BigDecimal maxTransactionAmount = BigDecimal.valueOf(10000000000L);

        if(transactionAmount == null){
            errors.rejectValue("transactionAmount","deposit.transactionAmount.notnull");
        }else if(transactionAmount.compareTo(BigDecimal.ZERO) <= 0 || transactionAmount.compareTo(maxTransactionAmount) > 0){
            errors.rejectValue("transactionAmount","deposit.transactionAmount.valid");
        }

    }
}
