package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import com.cg.service.withdraw.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @Autowired
    private IWithdrawService withdrawService;

    @GetMapping
    public String showListPage(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);

        return "customer/list";
    }
//-----------------------------------------------------------------Get Mapping -----------------------------------------------------------------
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

    @GetMapping("/update/{id}")
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
    @GetMapping("/transfer/{senderId}")
    public String showTransferPage(@PathVariable Long senderId , Model model){
        Optional<Customer> senderOptional  = customerService.findById(senderId);
        // isPresent() Phương thức này dùng để kiểm tra một đối tượng Optional có không rỗng hay không? Nếu đối tượng này bị rỗng thì nó sẽ trả về false.
        if(!senderOptional.isPresent()){
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender not found");
        }
        else{
            Customer sender = senderOptional.get();

            Transfer transfer = new Transfer();

            transfer.setSender(sender);

            model.addAttribute("transfer",transfer);

            List<Customer> recipients = customerService.findAllByIdNotAndDeletedIsFalse(senderId);

            model.addAttribute("recipients", recipients);

        }
        return "customer/transfer";
    }

//----------------------------------------PostMapping----------------------------------------
    @PostMapping("/create")
    public String doCreate(@ModelAttribute Customer customer, Model model) {

        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);

        model.addAttribute("success", true);
        model.addAttribute("messages", "Create customer successful");

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

            deposit.setCustomer(customer);
            deposit = customerService.deposit(deposit);

            deposit.setTransactionAmount(BigDecimal.ZERO);

            model.addAttribute("deposit", deposit);
        }
        model.addAttribute("success", true);
        model.addAttribute("messages", "Deposit successful");


        return "customer/deposit";
    }
    @PostMapping("/update/{id}")
    public String toUpdate(@PathVariable Long id, Model model, @ModelAttribute Customer customer) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
        }
        else{
            Customer customer1 = customerOptional.get();
            customer.setId(id);
            customer.setBalance(customer1.getBalance());
            customerService.save(customer);
            model.addAttribute("customer", customer);

            model.addAttribute("success", true);
            model.addAttribute("messages", "Update success");
        }

        return "customer/edit";
    }
    @PostMapping("/withdraw/{idCustomer}")
    public String doWithdraw(@PathVariable Long idCustomer, @ModelAttribute Withdraw withdraw, Model model){
        Optional<Customer> customerOptional = customerService.findById(idCustomer);
        if (customerOptional.isEmpty()){
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không tồn tại");
        } else {
            Customer customer = customerOptional.get();
            BigDecimal currentBalance = customer.getBalance();
            if (currentBalance.compareTo(withdraw.getTransactionAmount()) >= 0) {
                BigDecimal newBalance = currentBalance.subtract(withdraw.getTransactionAmount());
                customer.setBalance(newBalance);
                customer.setId(idCustomer);
                customerService.save(customer);

                withdraw.setId(null);
                withdraw.setCustomer(customer);
                withdrawService.save(withdraw);
                model.addAttribute("success", true);
                model.addAttribute("message", "Rút thành công: " + withdraw.getTransactionAmount() + " $");
            } else {
                model.addAttribute("error", true);
                model.addAttribute("message", "Số dư không đủ");
                withdraw.setCustomer(customer);
            }
            model.addAttribute("withdraw", withdraw);
        }
        return "customer/withdraw";
    }

    @PostMapping("/transfer/{senderId}")
    public String doTransfer(@PathVariable Long senderId ,@ModelAttribute Transfer transfer, Model model){
        Optional<Customer> senderOptional = customerService.findById(senderId);
        List<Customer> recipients = customerService.findAllByIdNotAndDeletedIsFalse(senderId);

        model.addAttribute("recipients", recipients);
        model.addAttribute("transfer", transfer);

        if (!senderOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender not valid");

            return "customer/transfer";
        }

        Long recipientId = transfer.getRecipient().getId();
        Optional<Customer> recipientOptional = customerService.findById(recipientId);

        if (!recipientOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Recipient not valid");

            return "customer/transfer";
        }
        if (senderId.equals(recipientId)) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender ID must be different from Recipient ID");

            return "customer/transfer";
        }

        BigDecimal senderCurrentBalance = senderOptional.get().getBalance();

        String transferAmountStr = String.valueOf(transfer.getTransferAmount());
        BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(transferAmountStr));
        long fees = 10L;
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (senderCurrentBalance.compareTo(transactionAmount) < 0) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender balance not enough to transfer");

            return "customer/transfer";
        }

        transfer.setSender(senderOptional.get());
        transfer.setFees(fees);
        transfer.setFeesAmount(feesAmount);
        transfer.setTransactionAmount(transactionAmount);

        customerService.transfer(transfer);

        transfer.setTransferAmount(BigDecimal.ZERO);
        transfer.setTransactionAmount(BigDecimal.ZERO);

        model.addAttribute("transfer", transfer);

        model.addAttribute("success", true);
        model.addAttribute("messages", "Transfer success");

        return "customer/transfer";
    }

}
