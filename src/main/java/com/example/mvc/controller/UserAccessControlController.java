package com.example.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mvc.service.PlatformService;

@Controller
@RequestMapping("/admin")
public class UserAccessControlController {

	 private final PlatformService cardBlockStatusService; 

	    @Autowired
	    public UserAccessControlController(PlatformService cardBlockStatusService) {
	        this.cardBlockStatusService = cardBlockStatusService; 
	    }

    @GetMapping("/userAccessControl")
    public String viewUserAccessControl(Model model) {
        model.addAttribute("cardBlockStatusList", cardBlockStatusService.getAllCardBlockStatuses());
        return "userAccessControl";
    }

    @PostMapping("/approveCardBlock")
    public String approveCardBlock(@RequestParam("id") int id) {
        cardBlockStatusService.approveCardBlockStatus(id);
        return "redirect:/admin/userAccessControl";
    }

    @PostMapping("/rejectCardBlock")
    public String rejectCardBlock(@RequestParam("id") int id) {
        cardBlockStatusService.rejectCardBlockStatus(id);
        return "redirect:/admin/userAccessControl";
    }
}
