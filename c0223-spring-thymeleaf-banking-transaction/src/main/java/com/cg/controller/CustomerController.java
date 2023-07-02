package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;

    @GetMapping
    public String showListPage(Model model) {
        List<Customer> customers = customerService.findAll();

        model.addAttribute("customers", customers);

        return "customer/list";
    }

    @GetMapping("/create")
    public String showCreatePage() {
        return "customer/create";
    }

    @GetMapping("/deposit/{customerId}")
    public String showDepositPage(@PathVariable Long customerId, Model model) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID khách hàng không tồn tại");
        }
        else {
            Customer customer = customerOptional.get();
            Deposit deposit = new Deposit();
            deposit.setCustomer(customer);

            model.addAttribute("deposit", deposit);
        }

        return "customer/deposit";
    }

    @GetMapping("/edit/{id}")
    public String showUpdate(@PathVariable String id, Model model) {
        try {
            Long customerId = Long.parseLong(id);
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (customerOptional.isEmpty()) {
                return "redirect:/errors/404";
            }

            Customer customer = customerOptional.get();

            model.addAttribute("customer", customer);

            return "/customer/edit";
        } catch (Exception e) {
            return "/errors/404";
        }
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {

        try {
            Long customerId = Long.parseLong(id);
            customerService.deleteById(customerId);

            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Xóa thành công");

            return "redirect:/customers";
        } catch (Exception e) {
            return "/errors/404";
        }
    }
    @GetMapping("/withdraw/{id}")
    public String showWithdraw(@PathVariable String id, Model model) {
        try {
            Long customerId = Long.parseLong(id);
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (customerOptional.isEmpty()) {
                return "redirect:/errors/404";
            }

            Customer customer = customerOptional.get();

            Withdraw withdraw = new Withdraw();
            withdraw.setCustomer(customer);

            model.addAttribute("withdraw", withdraw);

            return "/customer/withdraw";
        } catch (Exception e) {
            return "/errors/404";
        }

    }


    @PostMapping("/create")
    public String doCreate(@ModelAttribute Customer customer) {

        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);

        return "customer/create";
    }

    @PostMapping("/deposit/{customerId}")
    public String doDeposit(@ModelAttribute Deposit deposit, @PathVariable Long customerId, Model model) {
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID khách hàng không tồn tại");
        }
        else {
            Customer customer = customerOptional.get();

            deposit.setId(customerId);
            depositService.save(deposit);

            BigDecimal currentBalance = customer.getBalance();
            BigDecimal newBalance = currentBalance.add(deposit.getTransactionAmount());
            customer.setBalance(newBalance);
            customerService.save(customer);

            deposit.setCustomer(customer);

            model.addAttribute("deposit", deposit);
        }

        return "customer/deposit";
    }
    @PostMapping("/edit/{id}")
    public String toUpdate(@PathVariable Long id, Model model, @ModelAttribute Customer customer) {

        customer.setId(id);
        customerService.save(customer);
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "redirect:/customers";
    }
    @PostMapping("/withdraw/{id}")
    public String toWithdraw(@PathVariable String id,Model model,@RequestParam("withdraw") String withdraw){
        try{
            Long customerId = Long.parseLong(id);
            Long withdrawAmount = Long.parseLong(withdraw);
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (customerOptional.isEmpty()) {
                return "redirect:/errors/404";
            }

            Customer customer = customerOptional.get();

            BigDecimal balance = customer.getBalance().subtract(BigDecimal.valueOf(withdrawAmount));

            customer.setBalance(balance);
            customerService.save(customer);
            Withdraw withdraw1 = new Withdraw();
            withdraw1.setCustomer(customer);
            model.addAttribute("withdraw",withdraw1);
            return "/customer/withdraw";


        }catch (Exception e){
            return "/errors/404";
        }
    }
}
