package com.zerobase.cms.model.repository;
import com.zerobase.cms.model.Product;
import java.util.List;

public interface ProductRepositoryCustom {

	List<Product> searchByName(String name);

}