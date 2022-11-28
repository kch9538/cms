package com.zerobase.cms.sevice;
import static com.zerobase.cms.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.zerobase.cms.exception.CustomException;
import com.zerobase.cms.model.Product;
import com.zerobase.cms.model.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

	private final ProductRepository productRepository;

	public List<Product> searchByName(String name) {
		return productRepository.searchByName(name);
	}

	public Product getByProductId(Long productId) {
		return productRepository.findWithProductItemsById(productId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
	}

	public List<Product> getListByProductIds(List<Long> productIds) {
		return productRepository.findAllByIdIn(productIds);
	}
}