package com.product.pricereduction.repository;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.product.pricereduction.constant.PriceReductionConstant;
import com.product.pricereduction.exception.ApiException;
import com.product.pricereduction.model.ClientResponse;
import com.product.pricereduction.model.ProductResponse;

@Repository
public class ProductPriceReductionRepository {

	private static final Logger LOGGER = LogManager.getLogger(ProductPriceReductionRepository.class.getName());

	@Value("${api.product.endpoint}")
	private String endPoint;

	@Value("${api.product.key}")
	private String apiKey;

	@Autowired
	private RestTemplate restTemplate;

	public List<ProductResponse> getProductByCatalogId(Long catId) {
		LOGGER.info(PriceReductionConstant.CLASS_AT + this.getClass().getName() + " Method @ getAllProducts");

		String productCategoryEndPoint = String.format(endPoint, catId, apiKey);
		try {
			ResponseEntity<ClientResponse>responseEntity = restTemplate.getForEntity(productCategoryEndPoint, ClientResponse.class);

			if(!ObjectUtils.isEmpty(responseEntity)) {
				return responseEntity.getBody().getProducts();
			}
			else {
				return Collections.emptyList();
			}
		}
		catch(RestClientResponseException exception ) {
			LOGGER.error(PriceReductionConstant.CLASS_AT + this.getClass().getName() + " Method @ getAllProducts - Response : ", exception);	
			throw new ApiException("Exception occur due to : "+exception.getMessage()); 
		}

	}
}
