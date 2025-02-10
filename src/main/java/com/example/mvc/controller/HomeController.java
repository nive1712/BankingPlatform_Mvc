package com.example.mvc.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

		String email = userDetails.getUsername();
		model.addAttribute("email", email);
		return "dashboard";
	}

	@GetMapping("/netbanking")
	public String netBanking() {
		return "netbanking";
	}

	@GetMapping("/atm")
	public String atm() {
		return "atm";
	}

	@PostMapping("/toNetBanking")
	public String redirectToNetBanking() {
		return "redirect:/netbanking";
	}

	@PostMapping("/toAtm")
	public String redirectToAtm() {
		return "redirect:/atm";
	}
}
