package com.cg.model;

import com.cg.utils.ValidateUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="customers")
public class Customer extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;



    @Column(name="full_name" ,nullable = false)
    private String fullName;

    @Column(nullable = false,unique = true)
    private String email;

    private String phone;

    private String address;

    @Column(precision = 10, scale = 0, nullable = false,updatable = false)
    private BigDecimal balance;

    public Customer(Long id, String fullName, String email, String phone, String address, BigDecimal balance) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
    }

    public Customer() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {


        Customer customer =(Customer) target;

        String fullName = customer.fullName;
        if(fullName.length() == 0){
            errors.rejectValue("fullName","fullName.empty");
        }
        else{
            if(fullName.length() < 5 || fullName.length() > 55) {
                errors.rejectValue("fullName", "Họ tên nằm trong khoảng từ 5 đến 20 ký tự","Họ tên nằm trong khoảng từ 5 đến 20 ký tự");
            }
        }

        String email = customer.email;
        if(email.length() == 0){
            errors.rejectValue("email","Email là bắt buộc ","Email là bắt buộc ");
        }else{
            if(!ValidateUtils.isEmail(email)){
                errors.rejectValue("email","Email không đúng định dạng vui lòng nhập (example : longg2504@gmail.com)",
                        "Email không đúng định dạng vui lòng nhập (example : longg2504@gmail.com)");
            }
        }

        String phone = customer.phone;
        if(phone.length() == 0){
            errors.rejectValue("phone","Số điện thoại là bắt buộc","Số điện thoại là bắt buộc");
        }
        else {
            if(!ValidateUtils.isPhoneNumber(phone)){
                errors.rejectValue("phone","Số điện thoại phải nhập đúng định dạng(example : 0784689119)",
                        "Số điện thoại phải nhập đúng định dạng(example : 0784689119)");
            }
        }
    }


}
