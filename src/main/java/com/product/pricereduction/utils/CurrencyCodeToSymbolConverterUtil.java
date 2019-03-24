package com.product.pricereduction.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:currency-symbol.properties")
public class CurrencyCodeToSymbolConverterUtil {

	@Autowired
	private Environment environment;

	private CurrencyCodeToSymbolConverterUtil() {}

	public String convertCurrencyToSymbol(String currency) {
		return environment.getProperty(currency);
	}
}
