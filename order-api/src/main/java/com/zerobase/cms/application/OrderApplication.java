package com.zerobase.cms.application;

import static com.zerobase.cms.exception.ErrorCode.ORDER_FAIL_CHECK_CART;
import static com.zerobase.cms.exception.ErrorCode.ORDER_FAIL_NO_MONEY;

import com.zerobase.cms.client.MailgunClient;
import com.zerobase.cms.client.UserClient;
import com.zerobase.cms.client.mailgun.SendMailForm;
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
	private final MailgunClient mailgunClient;



	@Transactional
	public String order(String token, Cart cart) {

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
		//주문 사항 메일 전송
		SendMailForm sendMailForm = SendMailForm.builder()
			.from("test@naver.com")
			.to(customerDto.getEmail())
			.subject("주문내역 메일")
			.text(getOrderEmailBody(customerDto.getName(), orderCart))
			.build();

		mailgunClient.sendEmail(sendMailForm);

		return "주문이 성공하였습니다.";

	}

	public Integer getTotalPrice(Cart cart) {
		return cart.getProducts().stream().flatMapToInt(product ->
				product.getItems().stream().flatMapToInt(
					productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
			.sum();
	}

	private String getOrderEmailBody(String name, Cart orderCart) {
		StringBuilder builder = new StringBuilder();
		return builder.append("안녕하세요 ")
			.append(name)
			.append("주문내역입니다.\n\n ")
			.append(orderCart.getProducts().toString())
			.append("상품명 :" + orderCart.getProducts())
			.append("\n\n 곧 배송 될 예정입니다..\n 이용해주셔서 감사합니다.\n\n")
			.toString();
	}

}