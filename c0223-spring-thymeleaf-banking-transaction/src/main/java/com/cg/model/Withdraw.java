package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;



@Entity
@Table(name = "withdraws")
public class Withdraw implements Validator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;
    @Column(name = "transaction_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;

    public Withdraw(Customer customer, BigDecimal transactionAmount) {
        this.customer = customer;
        this.transactionAmount = transactionAmount;
    }

    public Withdraw(){
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
        return  Withdraw.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Withdraw withdraw = (Withdraw ) o;

        BigDecimal transactionAmount = withdraw.transactionAmount;

        if(transactionAmount == null){
            errors.rejectValue("transactionAmount","withdraw.transactionAmount.isError");
//            errors.rejectValue("transactionAmount","withdraw.transactionAmount.nollnull");
        }else if(transactionAmount.compareTo(BigDecimal.ZERO)<=0){
            errors.rejectValue("transactionAmount","withdraw.transactionAmount.valid");
            }

        }
    }


