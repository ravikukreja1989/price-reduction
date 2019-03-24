## Price - Reduction

 - This application is developed in Spring Boot with Spring rest API as backend.
 
 - It returns all the products comes under selected category that have price reduction.
 
 - It can be access by hitting below URL either from Postman/soapUI or from browser :    	`http://localhost:8088/price-reduction/v1/catalogs/{catalogId}/products?labelType={labelType}` 

  - Here, 
  		catalogId : It's having long data type and value can be say `600001506`.  
  		labelType : It's having string data type and value can be any one of them `ShowWasNow`, `ShowWasThenNow`, 					`ShowPercDiscount`. Default value for label type is `ShowWasNow` if none of them is selected.
  		
  - Sample requested formed URL :
	 `http://localhost:8088/price-reduction/v1/catalogs/600001506/products?labelType=ShowWasNow`
  
## Build

  - Run `maven install` command to build the project and download all the required dependency.

## How to run application

 When you need to Run this application on your local :

 - From `src/main/java/com/product/pricereduction/`, Run `PriceReductionApplication.java` as Spring boot application.

 - Once application is started, message "Started PriceReductionApplication in 10.703 seconds" will be logged. Then go to browser and hit above mentioned URL.

