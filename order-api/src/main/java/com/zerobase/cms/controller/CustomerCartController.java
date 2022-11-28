package com.zerobase.cms.controller;

import com.zerobase.cms.application.CartApplication;
import com.zerobase.cms.config.JwtAuthenticationProvider;
import com.zerobase.cms.model.product.AddProductCartForm;
import com.zerobase.cms.model.redis.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
public class CustomerCartController {

	//임시코드
	private final CartApplication cartApplication;
	private final JwtAuthenticationProvider provider;

	@PostMapping
	public ResponseEntity<Cart> addCart(
		@RequestHeader(name = "X-AUTH-TOKEN") String token,
		@RequestBody AddProductCartForm form) {
		return ResponseEntity.ok(cartApplication.addCart(provider.getUserVo(token).getId(), form));
	}

}