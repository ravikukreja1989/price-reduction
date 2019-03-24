package com.product.pricereduction.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.product.pricereduction.constant.PriceReductionConstant;
import com.product.pricereduction.enums.LabelType;
import com.product.pricereduction.model.ColorSwatch;
import com.product.pricereduction.model.ColorSwatchesResponse;
import com.product.pricereduction.model.PriceResponse;
import com.product.pricereduction.model.Product;
import com.product.pricereduction.model.ProductResponse;
import com.product.pricereduction.repository.ProductPriceReductionRepository;
import com.product.pricereduction.utils.ColorToHexConversionUtil;
import com.product.pricereduction.utils.CurrencyCodeToSymbolConverterUtil;
import com.product.pricereduction.utils.PriceUtil;

@Service
public class ProductPriceReductionService {

	private static final Logger LOGGER = LogManager.getLogger(ProductPriceReductionService.class.getName());

	@Autowired
	private ProductPriceReductionRepository productPriceReductionRepository;

	@Autowired
	private ColorToHexConversionUtil colorToHexConversionUtil;

	@Autowired
	private CurrencyCodeToSymbolConverterUtil currencyCodeToSymbolConverterUtil;

	
	public List<Product> getProductsForCatalog(Long catId, LabelType labelType) {
		LOGGER.info(PriceReductionConstant.CLASS_AT + this.getClass().getName() + " Method @ getProductsForCatalog");

		List<ProductResponse> products = productPriceReductionRepository.getProductByCatalogId(catId);
		if (!ObjectUtils.isEmpty(products)) {
			return products.stream()
					.filter(prod -> {
						if (!ObjectUtils.isEmpty(prod.getPrice())) {
							return (PriceUtil.stringToDoubleConversion(prod.getPrice().getWas()) > 
							PriceUtil.stringToDoubleConversion(PriceUtil.formattedNowPrice(prod.getPrice().getNow())));
						}else {
							return false;
						}
					})
					.map(productResponse -> {
						PriceResponse price = productResponse.getPrice();
						Product product = new Product();
						if (!ObjectUtils.isEmpty(price)) {
							Object now = price.getNow();
							String was = price.getWas();
							String currency = currencyCodeToSymbolConverterUtil.convertCurrencyToSymbol(price.getCurrency());
							product.setProductId(productResponse.getProductId());
							product.setTitle(productResponse.getTitle());
							product.setColorSwatches(getColorSwatches(productResponse.getColorSwatches()));
							product.setNowPrice(buildNowPrice(currency, now));
							product.setPriceLabel(buildPriceLabel(price, labelType, currency));
							product.setPriceReduction(buildPriceReduction(was, now));
						}
						return product;
					})
					.sorted(Comparator.comparing(Product :: getPriceReduction).reversed())
					.collect(Collectors.toList());
		} 
		else {
			return Collections.emptyList();
		}
	}

	private List<ColorSwatch> getColorSwatches(List<ColorSwatchesResponse> colorSwatchesList) {
		if (!ObjectUtils.isEmpty(colorSwatchesList)) {
			return colorSwatchesList.stream()
					.map(colorSwatchResponse -> {
						ColorSwatch colorSwatch = new ColorSwatch();
						colorSwatch.setColor(colorSwatchResponse.getColor());
						colorSwatch.setRgbColor(colorToHexConversionUtil.convertColorNameToHexCode(colorSwatchResponse.getBasicColor()));
						colorSwatch.setSkuid(colorSwatchResponse.getSkuId());
						return colorSwatch;
					})
					.collect(Collectors.toList());
		} 
		else {
			return Collections.emptyList();
		}
	}

	private String buildNowPrice(String currency, Object nowPrice) {
		return PriceUtil.priceFormatWithCurrency(PriceUtil.formattedNowPrice(nowPrice), currency);
	}

	private Double buildPriceReduction(String wasPrice, Object nowPrice) {
		return PriceUtil.stringToDoubleConversion(wasPrice) - PriceUtil.stringToDoubleConversion(PriceUtil.formattedNowPrice(nowPrice));
	}

	private String buildPriceLabel(PriceResponse price, LabelType labelType, String currency) {
		String nowPrice = PriceUtil.formattedNowPrice(price.getNow());
		String wasPrice = price.getWas();

		if(!ObjectUtils.isEmpty(labelType)) {
			switch(labelType) {
			case SHOW_WAS_THEN_NOW :
				return buildWasThenNowLabel(nowPrice, price, currency);
			case SHOW_PERC_DISCOUNT :
				Double was = PriceUtil.stringToDoubleConversion(wasPrice);
				Double now = PriceUtil.stringToDoubleConversion(nowPrice);
				double percOff = (double) (Math.round(((was - now) / was) * 100));
				return String.format("%.2f%s off - now %s", percOff, "%", PriceUtil.priceFormatWithCurrency(nowPrice, currency));
			default :
				return String.format("Was %s, now %s", PriceUtil.priceFormatWithCurrency(wasPrice, currency),PriceUtil.priceFormatWithCurrency(nowPrice, currency));
			}
		}
		else {
			return "";
		}
	}
	
	private String buildWasThenNowLabel(String now, PriceResponse price, String currency) {
		String was = price.getWas();
		String then = !StringUtils.isEmpty(price.getThen2()) ? price.getThen2() : price.getThen1();

		return (!StringUtils.isEmpty(then)) ? 
				String.format("Was %s, then %s, now %s", 
						PriceUtil.priceFormatWithCurrency(was, currency),
						PriceUtil.priceFormatWithCurrency(then, currency),
						PriceUtil.priceFormatWithCurrency(now, currency)
						) : 
				String.format("Was %s, now %s", 
						PriceUtil.priceFormatWithCurrency(was, currency),
						PriceUtil.priceFormatWithCurrency(now, currency)
						);
	}
}
