package com.zerobase.cms.application;

import static com.zerobase.cms.exception.ErrorCode.ORDER_FAIL_CHECK_CART;
import static com.zerobase.cms.exception.ErrorCode.ORDER_FAIL_NO_MONEY;

import com.zerobase.cms.client.UserClient;
import com.zerobase.cms.client.user.ChangeBalanceForm;
import com.zerobase.cms.client.user.CustomerDto;
import com.zerobase.cms.exception.CustomException;
import com.zerobase.cms.model.ProductItem;
import com.zerobase.cms.model.redis.Cart;
import com.zerobase.cms.sevice.ProductItemService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApplication {

	private final CartApplication cartApplication;
	private final UserClient userClient;

	private final ProductItemService productItemService;


	@Transactional
	public void order(String token, Cart cart) {

		// 주문 시 기존 카트 버림
		// 선택주문 : 내가 사지 않은 아이템을 살려야 함 -> ?

		Cart orderCart = cartApplication.refreshCart(cart);
		if (orderCart.getMessages().size() > 0) {
			throw new CustomException(ORDER_FAIL_CHECK_CART);
		}
		CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

		int totalPrice = getTotalPrice(cart);

		if (customerDto.getBalance() < getTotalPrice(cart)) {
			throw new CustomException(ORDER_FAIL_NO_MONEY);
		}

		userClient.changeBalance(token, ChangeBalanceForm.builder()
			.from("USER")
			.message("Order")
			.money(-totalPrice)
			.build());

		for (Cart.Product product : orderCart.getProducts()) {
			for (Cart.ProductItem cartItem : product.getItems()) {
				ProductItem productItem = productItemService.getProductItem(cartItem.getId());
				productItem.setCount(productItem.getCount() - cartItem.getCount());
			}
		}


	}

	public Integer getTotalPrice(Cart cart) {
		return cart.getProducts().stream().flatMapToInt(product ->
				product.getItems().stream().flatMapToInt(
					productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
			.sum();
	}



}