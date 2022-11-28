package com.zerobase.cms.order.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zerobase.cms.model.Product;
import com.zerobase.cms.model.product.AddProductForm;
import com.zerobase.cms.model.product.AddProductItemForm;
import com.zerobase.cms.model.repository.ProductRepository;
import com.zerobase.cms.sevice.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void ADD_PRODUCT_TEST() {
		Long sellerId = 1L;

		AddProductForm form = makeProductForm("유메꼬까옷", "유메가을옷", 3);
		Product p = productService.addProduct(sellerId, form);

		Product result = productRepository.findWithProductItemsById(p.getId()).get();

		assertNotNull(result);

		//나머지 필드들에 대한 검증?
		assertEquals(result.getName(), "유메꼬까옷");
		assertEquals(result.getDescription(), "유메가을옷");
		assertEquals(result.getProductItems().size(), 3);
		assertEquals(result.getProductItems().get(0).getName(), "유메꼬까옷0");
		assertEquals(result.getProductItems().get(1).getPrice(), 10000);
		assertEquals(result.getProductItems().get(2).getCount(), 1);

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

	private static final AddProductItemForm makeProductItemForm(Long productId, String name) {
		return AddProductItemForm.builder()
			.productId(productId)
			.name(name)
			.price(10000)
			.count(1)
			.build();
	}
}
