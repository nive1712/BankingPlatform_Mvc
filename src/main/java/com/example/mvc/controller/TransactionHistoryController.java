package com.example.mvc.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mvc.model.TransactionHistory;
import com.example.mvc.model.User;
import com.example.mvc.service.PlatformService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Controller
@RequestMapping("/transactionHistory")
public class TransactionHistoryController {


	 private final PlatformService platformService;

	 @Autowired
	 public TransactionHistoryController(PlatformService platformService) {
	        this.platformService = platformService;
	    }
  
    @GetMapping
    public String showTransactionHistory(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = platformService.getUserByEmail(userDetails.getUsername());
        String accountNumber = platformService.getAccountNumberByUserId(user.getUserId());
        
        boolean cardBlocked = platformService.isCardBlocked(accountNumber);
        if (cardBlocked) {
            model.addAttribute("error", "Your card is blocked. Transaction History not visible.");
            return "transactionHistory"; 
        }
        
        List<TransactionHistory> transactions = platformService.getTransactionHistory(user.getUserId());
        model.addAttribute("transactions", transactions);
        
        return "transactionHistory";
    }
}
