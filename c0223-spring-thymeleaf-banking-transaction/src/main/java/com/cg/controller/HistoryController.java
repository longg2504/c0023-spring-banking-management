package com.cg.controller;


import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.service.customer.ICustomerService;
import com.cg.service.transfer.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/histories")
public class HistoryController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ITransferService transferService;

    @GetMapping("/transfer")
    public String showTransferPage(Model model) {
        List<Transfer> transfers = transferService.findAll();

        model.addAttribute("transfers", transfers);
        BigDecimal totalFeesAmount = transferService.getAllFeesAmount();

        model.addAttribute("transfers", transfers);
        model.addAttribute("totalFeesAmount", totalFeesAmount);

        return "customer/history";
    }

//    @GetMapping("/transfer/{senderId}")
//    public String showTransferBySenderPage(Model model, @PathVariable Long senderId) {
//
//        Optional<Customer> senderOptional = customerService.findById(senderId);
//
//        if (!senderOptional.isPresent()) {
//            return "/errors/404";
//        }
//
//        Customer sender = senderOptional.get();
//
//        List<Transfer> transfers = transferService.findAllBySender(sender);
//
//        BigDecimal totalFeesAmount = transferService.getAllFeesAmount();
//
//        model.addAttribute("transfers", transfers);
//        model.addAttribute("totalFeesAmount", totalFeesAmount);
//
//        return "/customer/transfer_by_sender";
//    }

}
