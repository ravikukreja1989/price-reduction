package com.product.pricereduction.model;

import java.util.ArrayList;
import java.util.List;

public class ClientResponse {
	private List<ProductResponse> products = new ArrayList<>();

	public List<ProductResponse> getProducts() {
		return products;
	}

	public void setProducts(List<ProductResponse> products) {
		this.products = products;
	}
}

