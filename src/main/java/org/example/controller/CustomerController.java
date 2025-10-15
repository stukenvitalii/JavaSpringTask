package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.CustomerResponse;
import org.example.dto.ErrorResponse;
import org.example.dto.SearchRequest;
import org.example.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
	private final CustomerService customerService;

	@PostMapping
	public ResponseEntity<?> search(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
		try {
			Integer companyId = (Integer) request.getAttribute("companyId");

			String phone = searchRequest.getSearch();
			CustomerResponse customer = customerService.findByPhone(phone, companyId);

			if (customer == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorResponse("Клиент не найден"));
			}

			return ResponseEntity.ok(customer);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Внутренняя ошибка сервера"));
		}
	}
}
