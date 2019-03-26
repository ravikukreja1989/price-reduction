package com.product.pricereduction.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.product.pricereduction.enums.LabelType;
import com.product.pricereduction.model.Product;
import com.product.pricereduction.service.ProductPriceReductionService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(ProductPriceReductionController.class)
public class ProductPriceReductionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductPriceReductionService productPriceReductionService;

	@Test
	public void testGetProductsWithPriceReduction() throws Exception {

		Mockito.when(productPriceReductionService.getProductsForCatalog(600001506L, LabelType.SHOW_WAS_NOW)).thenReturn(buildProductList());

		mockMvc.perform(get("/v1/catalogs/{catalogId}/products?labelType=ShowWasNow",600001506L ))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(1)))
		.andExpect(jsonPath("$[0].productId", Matchers.is(buildProductList().get(0).getProductId())));
	}
	
	@Test
	public void testGetProductsWithPriceReductionWithUnmatchedLabel() throws Exception {

		Mockito.when(productPriceReductionService.getProductsForCatalog(600001506L, LabelType.SHOW_WAS_NOW)).thenReturn(buildProductList());

		mockMvc.perform(get("/v1/catalogs/{catalogId}/products?labelType=TestLabel",600001506L ))
		.andExpect(status().isBadRequest())
		.andExpect(status().is(400));
	}
	
	@Test
	public void testGetProductsWithPriceReductionWithEmptyProductList() throws Exception {

		Mockito.when(productPriceReductionService.getProductsForCatalog(600001506L, LabelType.SHOW_WAS_NOW)).thenReturn(null);

		mockMvc.perform(get("/v1/catalogs/{catalogId}/products?labelType=ShowWasNow",600001506L ))
		.andExpect(status().isNoContent())
		.andExpect(status().is(204));
	}

	private List<Product> buildProductList(){
		Product prod1 = new Product();
		prod1.setProductId("3525081");
		prod1.setTitle("hush Marble Panel Maxi Dress");
		return Arrays.asList(prod1);
	}

}
