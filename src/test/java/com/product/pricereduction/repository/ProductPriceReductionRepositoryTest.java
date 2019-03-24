package com.product.pricereduction.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.product.pricereduction.exception.ApiException;
import com.product.pricereduction.model.ClientResponse;
import com.product.pricereduction.model.ProductResponse;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductPriceReductionRepositoryTest {

	@Autowired
	private ProductPriceReductionRepository productPriceReductionRepository;

	@MockBean
	private RestTemplate restTemplate;

	@Test
	public void testGetAllProductWithEndPoints() {
		Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn(reponse());
		List<ProductResponse> response = productPriceReductionRepository.getProductByCatalogId(600001506L);
		assertNotNull(response);
		assertTrue(response.size() > 0);	
	}

	@Test
	public void testGetAllProductWithEmptyResponse() {
		Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn(null);
		List<ProductResponse> response = productPriceReductionRepository.getProductByCatalogId(600001506L);
		assertNotNull(response);
		assertTrue(response.isEmpty());
		assertEquals(0, response.size());
	}
	
	@Test(expected = ApiException.class)
	public void testGetAllProductWithException() {
		Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenThrow(RestClientResponseException.class);
		productPriceReductionRepository.getProductByCatalogId(600001506L);
	}

	private ResponseEntity<Object> reponse() {
		List<ProductResponse> list = new ArrayList<>();
		ProductResponse product = new ProductResponse();
		product.setProductId("1231");
		product.setTitle("test");
		list.add(product);

		ClientResponse clientResponse = new ClientResponse();
		clientResponse.setProducts(list);

		return ResponseEntity.status(HttpStatus.OK).body(clientResponse);
	}
}
