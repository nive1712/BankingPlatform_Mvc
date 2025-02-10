package com.example.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.mvc.service.PlatformService;


@Controller

public class AdminController {
	
	 private final PlatformService cardBlockStatusService;

	    @Autowired
	    public AdminController(PlatformService cardBlockStatusService) {
	        this.cardBlockStatusService = cardBlockStatusService;
	    }

    @PostMapping("/approveCardBlockStatus")
    public String approveCardBlockStatus(@RequestParam("id") int id) {
        cardBlockStatusService.approveCardBlockStatus(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/rejectCardBlockStatus")
    public String rejectCardBlockStatus(@RequestParam("id") int id) {
        cardBlockStatusService.rejectCardBlockStatus(id);
        return "redirect:/admin/dashboard";
    }
}

