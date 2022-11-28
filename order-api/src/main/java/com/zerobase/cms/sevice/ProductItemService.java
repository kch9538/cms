package com.zerobase.cms.sevice;
import static com.zerobase.cms.exception.ErrorCode.NOT_FOUND_PRODUCT;
import static com.zerobase.cms.exception.ErrorCode.SAME_ITEM_NAME;

import com.zerobase.cms.exception.CustomException;
import com.zerobase.cms.model.Product;
import com.zerobase.cms.model.ProductItem;
import com.zerobase.cms.model.product.AddProductItemForm;
import com.zerobase.cms.model.repository.ProductItemRepository;
import com.zerobase.cms.model.repository.ProductRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductItemService {

	private final ProductRepository productRepository;
	private final ProductItemRepository productItemRepository;

	@Transactional
	public Product addProductItem(Long sellerId, AddProductItemForm form) {

		Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

		if (product.getProductItems().stream()
			.anyMatch(item -> item.getName().equals(form.getName()))) {
			throw new CustomException(SAME_ITEM_NAME);
		}

		ProductItem productItem = ProductItem.of(sellerId, form);
		product.getProductItems().add(productItem);
		return product;
	}

}