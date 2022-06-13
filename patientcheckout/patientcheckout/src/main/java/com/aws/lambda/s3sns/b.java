package com.aws.lambda.s3sns;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class b {
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	public void handler(SNSEvent event) {
		event.getRecords().forEach(snsRecord -> {
			try {
				p p = objectMapper.readValue(snsRecord.getSNS().getMessage(), p.class);
				System.out.println(p);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}
}