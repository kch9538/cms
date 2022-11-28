package com.zerobase.cms.order.application;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zerobase.cms.application.CartApplication;
import com.zerobase.cms.model.Product;
import com.zerobase.cms.model.product.AddProductCartForm;
import com.zerobase.cms.model.product.AddProductForm;
import com.zerobase.cms.model.product.AddProductItemForm;
import com.zerobase.cms.model.redis.Cart;
import com.zerobase.cms.model.repository.ProductRepository;
import com.zerobase.cms.sevice.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CartApplicationTest {

	@Autowired
	private CartApplication cartApplication;
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void ADD_TEST() {
		Product p = add_product();

		Product result = productRepository.findWithProductItemsById(p.getId()).get();

		assertNotNull(result);

		assertEquals(result.getName(), "게이밍마우스");
		assertEquals(result.getDescription(), "마우스");

		assertEquals(result.getProductItems().size(), 3);
		assertEquals(result.getProductItems().get(0).getName(), "게이밍마우스0");
		assertEquals(result.getProductItems().get(0).getPrice(), 15000);

		Long customerId = 100L;

		Cart cart = cartApplication.addCart(customerId, makeAddForm(result));

		assertEquals(cart.getMessages().size(), 0);

	}

	@Test
	void ADD_TEST_MODIFY() {

		Long customerId = 100L;

		cartApplication.clearCart(customerId);


		Product p = add_product();
		Product result = productRepository.findWithProductItemsById(p.getId()).get();

		assertNotNull(result);

		assertEquals(result.getName(), "게이밍마우스");
		assertEquals(result.getDescription(), "마우스");

		assertEquals(result.getProductItems().size(), 3);
		assertEquals(result.getProductItems().get(0).getName(), "게이밍마우스0");
		assertEquals(result.getProductItems().get(0).getPrice(), 15000);

		Cart cart = cartApplication.addCart(customerId, makeAddForm(result));

		assertEquals(cart.getMessages().size(), 0);

		cart = cartApplication.getCart(customerId);
		assertEquals(cart.getMessages().size(), 1);

	}

	AddProductCartForm makeAddForm(Product p) {
		AddProductCartForm.ProductItem productItem =
			AddProductCartForm.ProductItem.builder()
				.id(p.getProductItems().get(0).getId())
				.name(p.getProductItems().get(0).getName())
				.count(5)
				.price(20000)
				.build();
		return AddProductCartForm.builder()
			.id(p.getId())
			.sellerId(p.getSellerId())
			.name(p.getName())
			.description(p.getDescription())
			.items(List.of(productItem))
			.build();
	}

	Product add_product() {
		Long sellerId = 1L;

		AddProductForm form = makeProductForm("게이밍마우", "LED마우스", 3);
		return productService.addProduct(sellerId, form);
	}

	private static AddProductForm makeProductForm(String name, String description, int itemCount) {
		List<AddProductItemForm> itemForms = new ArrayList<>();
		for (int i = 0; i < itemCount; i++) {
			itemForms.add(makeProductItemForm(null, name + i));
		}
		return AddProductForm.builder()
			.name(name)
			.description(description)
			.items(itemForms)
			.build();

	}

	private static AddProductItemForm makeProductItemForm(Long productId, String name) {
		return AddProductItemForm.builder()
			.productId(productId)
			.name(name)
			.price(15000)
			.count(10)
			.build();
	}

}