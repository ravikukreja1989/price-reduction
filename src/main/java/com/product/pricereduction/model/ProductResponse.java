package com.product.pricereduction.model;

import java.util.ArrayList;
import java.util.List;

public class ProductResponse {
	private String productId;
	private String title;
	private PriceResponse price;
	private List<ColorSwatchesResponse> colorSwatches = new ArrayList<>();

	public List<ColorSwatchesResponse> getColorSwatches() {
		return colorSwatches;
	}

	public String getProductId() {
		return productId;
	}

	public String getTitle() {
		return title;
	}

	public PriceResponse getPrice() {
		return price;
	}

	public void setColorSwatches(List<ColorSwatchesResponse> colorSwatches) {
		this.colorSwatches = colorSwatches;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPrice(PriceResponse price) {
		this.price = price;
	}
}

