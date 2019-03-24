package com.product.pricereduction.model;

public class ColorSwatchesResponse {
	private String color;
	private String basicColor;
	private String skuId;
	
	public String getColor() {
		return color;
	}

	public String getBasicColor() {
		return basicColor;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setBasicColor(String basicColor) {
		this.basicColor = basicColor;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
}
