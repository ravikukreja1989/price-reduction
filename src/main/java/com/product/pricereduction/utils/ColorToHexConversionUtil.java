package com.product.pricereduction.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:color.properties")
public class ColorToHexConversionUtil {

	@Autowired
	private Environment environment;
	
	private ColorToHexConversionUtil() {}
	
	public String convertColorNameToHexCode(String colorName) {
		return environment.getProperty(colorName);
	}
}
