package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CustomerResponse;
import org.example.dto.ErrorResponse;
import org.example.dto.SearchRequest;
import org.example.model.Company;
import org.example.security.JwtAuthentication;
import org.example.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
	private final CustomerService customerService;

	@PostMapping
	public ResponseEntity<?> search(@RequestBody SearchRequest searchRequest) {
		String phone = searchRequest.getSearch();
		CustomerResponse customer = customerService.findByPhone(phone);

		if (customer == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse("Клиент не найден"));
		}

		return ResponseEntity.ok(customer);
	}
}
