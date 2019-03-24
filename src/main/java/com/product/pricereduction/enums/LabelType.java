package com.product.pricereduction.enums;

public enum LabelType {

	SHOW_WAS_NOW, SHOW_WAS_THEN_NOW, SHOW_PERC_DISCOUNT;

	public static LabelType labelTypeFormat(String labelType) {
		switch(labelType.toUpperCase()) {
		case "SHOWWASNOW" :
			return SHOW_WAS_NOW;
		case "SHOWWASTHENNOW" :
			return SHOW_WAS_THEN_NOW;
		case "SHOWPERCDISCOUNT" :
			return SHOW_PERC_DISCOUNT;
		default :
			return null;
		}
	}
}
