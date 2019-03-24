package com.product.pricereduction.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.product.pricereduction.enums.LabelType;
import com.product.pricereduction.model.Product;
import com.product.pricereduction.service.ProductPriceReductionService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1")
public class ProductPriceReductionController {

	private static final Logger LOGGER = LogManager.getLogger(ProductPriceReductionController.class.getName());

	@Autowired
	private ProductPriceReductionService productPriceReductionService;

	@GetMapping("catalogs/{catalogId}/products")
	@ApiOperation("Get the list of products that have price reduction")
	public ResponseEntity<List<Product>> getProductsWithPriceReduction(
			@RequestParam(value = "labelType", defaultValue="ShowWasNow", required=true) String labelType,
			@PathVariable(value="catalogId", required=true) Long catId) {
		LabelType labelTypeFormat = LabelType.labelTypeFormat(labelType);
		
		if (ObjectUtils.isEmpty(labelTypeFormat)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<Product> products = productPriceReductionService.getProductsForCatalog(catId, labelTypeFormat);
		if (!ObjectUtils.isEmpty(products)) {
			return new ResponseEntity<>(products, HttpStatus.OK);
		}else {
			LOGGER.info("No Product is available for catelogId "+catId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
}
