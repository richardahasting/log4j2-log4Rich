package com.log4rich.integration.kafka;

/**
 * Configuration class for Kafka integration test settings.
 */
public class KafkaConfig {
    
    private final String brokerUrl;
    private final int messageCount;
    private final int batchSize;
    
    public KafkaConfig(String brokerUrl, int messageCount, int batchSize) {
        this.brokerUrl = brokerUrl;
        this.messageCount = messageCount;
        this.batchSize = batchSize;
    }
    
    public String getBrokerUrl() {
        return brokerUrl;
    }
    
    public int getMessageCount() {
        return messageCount;
    }
    
    public int getBatchSize() {
        return batchSize;
    }
    
    @Override
    public String toString() {
        return String.format("KafkaConfig{brokerUrl='%s', messageCount=%d, batchSize=%d}", 
                           brokerUrl, messageCount, batchSize);
    }
}