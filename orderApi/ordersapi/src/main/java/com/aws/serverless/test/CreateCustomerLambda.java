package com.aws.serverless.test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateCustomerLambda {
	
    public APIGatewayProxyResponseEvent createCustomer(APIGatewayProxyRequestEvent request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = objectMapper.readValue(request.getBody(), Customer.class);
        DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBAsyncClientBuilder.defaultClient());
        Table table = dynamoDB.getTable(System.getenv("CUSTOMER_TABLE"));
        Item item = new Item().withPrimaryKey("id", customer.id)
                .withString("firstName", customer.firstName)
                .withString("lastName", customer.lastName)
                .withInt("rewardPoints", customer.rewardPoints);
        table.putItem(item);
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Customer Id: " + customer.id);
    }
}