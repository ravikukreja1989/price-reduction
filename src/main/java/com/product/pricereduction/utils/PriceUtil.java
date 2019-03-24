package com.product.pricereduction.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class PriceUtil {

	private static final String PRICE_VALUE_TO_COMPARE = "10";

	private PriceUtil() {}

	public static String priceFormatWithCurrency(String price, String currency) {
		Double formattedValue = stringToDoubleConversion(price);
		if(stringToDoubleConversion(price) >= stringToDoubleConversion(PRICE_VALUE_TO_COMPARE)) {
			return String.format("%s%d", currency, formattedValue.intValue());
		}else {
			return String.format("%s%.2f", currency, formattedValue);
		}
	}

	public static Double stringToDoubleConversion(String stringValue) {
		Double doubleValue = 0.0;
		if(!StringUtils.isEmpty(stringValue)) {
			doubleValue = Double.valueOf(stringValue);
		}
		return doubleValue;
	}

	public static String formattedNowPrice(Object now) {
		String nowPrice = "";
		if (!ObjectUtils.isEmpty(now)) {
			if (now instanceof LinkedHashMap<?, ?>) {
				Map<String, String> nowValue = (LinkedHashMap<String, String>) now;
				nowPrice = nowValue.get("to");
			} else {
				nowPrice = now.toString();
			}
		}
		return nowPrice;
	}
}
