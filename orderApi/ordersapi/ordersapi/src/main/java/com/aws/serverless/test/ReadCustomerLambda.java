package com.aws.serverless.test;

import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadCustomerLambda {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	private AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
	
    public APIGatewayProxyResponseEvent getCustomers(APIGatewayProxyRequestEvent request) throws JsonProcessingException {
        ScanResult scanResult = dynamoDB.scan(new ScanRequest().withTableName(System.getenv("CUSTOMER_TABLE")));
        List<Customer> customers = scanResult.getItems().stream()
                .map(item -> new Customer(Integer.parseInt(item.get("id").getN()),item.get("firstName").getS(),item.get("lastName").getS(),Integer.parseInt(item.get("rewardPoints").getN())))
                .collect(Collectors.toList());
        String customerJson = objectMapper.writeValueAsString(customers);
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(customerJson);
    }
}