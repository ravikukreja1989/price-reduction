package com.product.pricereduction.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.product.pricereduction.enums.LabelType;
import com.product.pricereduction.model.ColorSwatchesResponse;
import com.product.pricereduction.model.PriceResponse;
import com.product.pricereduction.model.Product;
import com.product.pricereduction.model.ProductResponse;
import com.product.pricereduction.repository.ProductPriceReductionRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductPriceReductionServiceTest {

	@Autowired
	private ProductPriceReductionService productPriceReductionService;

	@MockBean
	private ProductPriceReductionRepository productPriceReductionRepository;

	private PriceResponse price;

	@Before
	public void setUp() {
		price = new PriceResponse();
		price.setCurrency("GBP");
		price.setWas("150.00");
		price.setThen1("130.00");
		price.setThen2("110.00");
		price.setNow("50.00");
	}

	@Test
	public void testGetProductsForCatalogWithShowWasThenNow() {
		Long catId = 600001506L;
		LabelType labelType = LabelType.SHOW_WAS_THEN_NOW;
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, labelType);
		assertList(products);
		assertEquals("Was £150, then £110, now £50", products.get(0).getPriceLabel());
	}

	@Test
	public void testGetProductsForCatalogWithShowWasThenNowEmptyThen() {
		price.setThen2(null);
		Long catId = 600001506L;
		LabelType labelType = LabelType.SHOW_WAS_THEN_NOW;
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, labelType);
		assertList(products);
		assertEquals("Was £150, then £130, now £50", products.get(0).getPriceLabel());
		assertTrue(products.get(0).getColorSwatches().isEmpty());
	}

	@Test
	public void testGetProductsForCatalogWithEmptyThen1AndThen2() {
		price.setThen1(null);
		price.setThen2(null);
		Long catId = 600001506L;
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, LabelType.SHOW_WAS_THEN_NOW);
		assertList(products);
		assertEquals("Was £150, now £50", products.get(0).getPriceLabel());
	}

	@Test
	public void testGetProductsForCatalogWithShowPercDiscount() {
		Long catId = 600001506L;
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, LabelType.SHOW_PERC_DISCOUNT);
		assertList(products);
		assertEquals("67.00% off - now £50", products.get(0).getPriceLabel());
	}

	@Test
	public void testGetProductsForCatalogWithDefaultLabel() {
		Long catId = 600001506L;
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, LabelType.SHOW_WAS_NOW);
		assertList(products);
		assertEquals("Was £150, now £50", products.get(0).getPriceLabel());
	}

	@Test
	public void testGetProductsForCatalogWithEmptyLabel() {
		Long catId = 600001506L;
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, null);
		assertList(products);
		assertTrue(products.get(0).getPriceLabel().isEmpty());
	}

	@Test
	public void testGetProductsForCatalogWithEmptyPrice() {
		Long catId = 600001506L;
		price = null;
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, LabelType.SHOW_WAS_NOW);
		assertNotNull(products);
		assertEquals(0, products.size());
	}

	@Test
	public void testGetProductsForCatalogWithWasLessThenNow() {
		Long catId = 600001506L;
		price.setWas("100.00");
		price.setNow("200.00");
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, LabelType.SHOW_WAS_NOW);
		assertNotNull(products);
		assertEquals(0, products.size());
	}
	
	@Test
	public void testGetProductsForCatalogWithNowPriceTo() {
		Long catId = 600001506L;
		Map<String, String> nowMap = new LinkedHashMap<>();
		nowMap.put("from", "6.00");
		nowMap.put("to", "10.00");
		price.setNow(nowMap);
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, LabelType.SHOW_WAS_NOW);
		assertList(products);
		assertEquals("Was £150, now £10", products.get(0).getPriceLabel());
	}

	@Test
	public void testGetProductsForCatalogWithEmptyNowPrice() {
		Long catId = 600001506L;
		price.setNow("");
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, LabelType.SHOW_WAS_NOW);
		assertList(products);
		assertEquals("Was £150, now £0.00", products.get(0).getPriceLabel());
	}


	@Test
	public void testGetProductsForCatalogWithEmptyProductList() {
		Long catId = 600001506L;
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(null);
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, LabelType.SHOW_WAS_NOW);
		assertNotNull(products);
		assertEquals(0, products.size());
	}

	private List<ProductResponse> buildProductList(PriceResponse price){
		ProductResponse prod1 = new ProductResponse();
		prod1.setProductId("3525081");
		prod1.setTitle("hush Marble Panel Maxi Dress");
		prod1.setPrice(price);
		if(!ObjectUtils.isEmpty(price) && StringUtils.isEmpty(price.getThen2())) {
			prod1.setColorSwatches(null);
		}
		else {
			prod1.setColorSwatches(buildColorSwatches());
		}

		return Arrays.asList(prod1);
	}

	private List<ColorSwatchesResponse> buildColorSwatches() {
		ColorSwatchesResponse colorSwatchesBlue = new ColorSwatchesResponse();
		colorSwatchesBlue.setBasicColor("Blue");
		colorSwatchesBlue.setColor("Navy");
		colorSwatchesBlue.setSkuId("237299103");
		return Arrays.asList(colorSwatchesBlue);
	}
	
	private void assertList(List<Product>products) {
		assertNotNull(products);
		assertTrue(!products.isEmpty());
	}
	
	@Test
	public void testGetProductsForCatalogWith_BasicColor_To_RGB_Translate() {
		Long catId = 600001506L;
		LabelType labelType = LabelType.SHOW_WAS_THEN_NOW;
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, labelType);
		assertList(products);
		assertEquals("0000FF",products.get(0).getColorSwatches().get(0).getRgbColor());
	}
	
	@Test
	public void testGetProductsForCatalogWith_Price_With_Decimal_Value() {
		Long catId = 600001506L;
		price.setNow("5.00");
		Mockito.when(productPriceReductionRepository.getProductByCatalogId(catId)).thenReturn(buildProductList(price));
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, LabelType.SHOW_WAS_NOW);
		assertList(products);
		assertEquals("Was £150, now £5.00", products.get(0).getPriceLabel());
	}
}

