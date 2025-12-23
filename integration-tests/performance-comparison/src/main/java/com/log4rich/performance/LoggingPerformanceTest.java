package com.log4rich.performance;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Core performance testing implementation that works with any LoggingConfiguration.
 * Provides comprehensive benchmarking including throughput, latency, and memory usage.
 */
public class LoggingPerformanceTest {
    
    private final LoggingConfiguration config;
    private final MemoryMXBean memoryBean;
    private final List<GarbageCollectorMXBean> gcBeans;
    
    public LoggingPerformanceTest(LoggingConfiguration config) {
        this.config = config;
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
    }
    
    /**
     * Measure single-threaded logging throughput.
     */
    public PerformanceComparisonRunner.ThroughputResult measureSingleThreadedThroughput(
            int messageCount, int warmupIterations, int testIterations) {
        
        try {
            config.initialize();
            Object logger = config.createLogger("performance.single");
            
            // Warmup
            for (int i = 0; i < warmupIterations; i++) {
                runSingleThreadedTest(logger, messageCount / 10);
            }
            
            // Actual measurements
            List<Double> results = new ArrayList<>();
            for (int i = 0; i < testIterations; i++) {
                double messagesPerSecond = runSingleThreadedTest(logger, messageCount);
                results.add(messagesPerSecond);
            }
            
            return calculateThroughputStatistics(results);
            
        } catch (Exception e) {
            throw new RuntimeException("Single-threaded throughput test failed", e);
        } finally {
            try {
                config.cleanup();
            } catch (Exception e) {
                // Log cleanup error but don't fail the test
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Measure multi-threaded logging throughput.
     */
    public PerformanceComparisonRunner.ThroughputResult measureMultiThreadedThroughput(
            int totalMessages, int threadCount, int warmupIterations, int testIterations) {
        
        try {
            config.initialize();
            
            // Warmup
            for (int i = 0; i < warmupIterations; i++) {
                runMultiThreadedTest(totalMessages / 10, threadCount);
            }
            
            // Actual measurements
            List<Double> results = new ArrayList<>();
            for (int i = 0; i < testIterations; i++) {
                double messagesPerSecond = runMultiThreadedTest(totalMessages, threadCount);
                results.add(messagesPerSecond);
            }
            
            return calculateThroughputStatistics(results);
            
        } catch (Exception e) {
            throw new RuntimeException("Multi-threaded throughput test failed", e);
        } finally {
            try {
                config.cleanup();
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Measure logging latency.
     */
    public PerformanceComparisonRunner.LatencyResult measureLatency(int sampleCount, int warmupIterations) {
        try {
            config.initialize();
            Object logger = config.createLogger("performance.latency");
            
            // Warmup
            for (int i = 0; i < warmupIterations; i++) {
                runLatencyTest(logger, sampleCount / 10);
            }
            
            // Actual measurement
            long[] latencies = runLatencyTest(logger, sampleCount);
            return calculateLatencyStatistics(latencies);
            
        } catch (Exception e) {
            throw new RuntimeException("Latency test failed", e);
        } finally {
            try {
                config.cleanup();
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Measure memory usage during logging.
     */
    public PerformanceComparisonRunner.MemoryUsageResult measureMemoryUsage(int messageCount) {
        try {
            config.initialize();
            Object logger = config.createLogger("performance.memory");
            
            // Record initial state
            System.gc();
            Thread.sleep(100);
            
            long initialMemory = memoryBean.getHeapMemoryUsage().getUsed();
            long initialGcCount = getTotalGcCount();
            long initialGcTime = getTotalGcTime();
            
            // Generate load
            long startTime = System.nanoTime();
            for (int i = 0; i < messageCount; i++) {
                config.logParameterizedMessage(logger, "INFO", 
                    "Memory test message {} with timestamp {} and data {}", 
                    i, System.nanoTime(), "sample-data-" + i);
                
                // Force some GC pressure periodically
                if (i % 10000 == 0) {
                    Object[] temp = new Object[1000];
                    Arrays.fill(temp, "temporary-" + i);
                }
            }
            long endTime = System.nanoTime();
            
            // Measure final state
            System.gc();
            Thread.sleep(100);
            
            long finalMemory = memoryBean.getHeapMemoryUsage().getUsed();
            long peakMemory = memoryBean.getHeapMemoryUsage().getMax();
            long finalGcCount = getTotalGcCount();
            long finalGcTime = getTotalGcTime();
            
            PerformanceComparisonRunner.MemoryUsageResult result = 
                new PerformanceComparisonRunner.MemoryUsageResult();
            result.peakMemoryUsageMB = Math.max(finalMemory, peakMemory) / (1024.0 * 1024.0);
            result.memoryGrowthMB = (finalMemory - initialMemory) / (1024.0 * 1024.0);
            result.gcEvents = finalGcCount - initialGcCount;
            result.gcTimeMicros = (finalGcTime - initialGcTime) * 1000.0; // Convert ms to μs
            
            return result;
            
        } catch (Exception e) {
            throw new RuntimeException("Memory usage test failed", e);
        } finally {
            try {
                config.cleanup();
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Measure performance of parameterized logging.
     */
    public PerformanceComparisonRunner.ThroughputResult measureParameterFormattingPerformance(
            int messageCount, int iterations) {
        
        try {
            config.initialize();
            Object logger = config.createLogger("performance.parameters");
            
            List<Double> results = new ArrayList<>();
            for (int iter = 0; iter < iterations; iter++) {
                long startTime = System.nanoTime();
                
                for (int i = 0; i < messageCount; i++) {
                    // Test various parameter patterns
                    switch (i % 4) {
                        case 0:
                            config.logParameterizedMessage(logger, "INFO", 
                                "Single parameter: {}", "value-" + i);
                            break;
                        case 1:
                            config.logParameterizedMessage(logger, "INFO", 
                                "Two parameters: {} and {}", "first-" + i, "second-" + i);
                            break;
                        case 2:
                            config.logParameterizedMessage(logger, "INFO", 
                                "Multiple parameters: {}, {}, {}, {}", 
                                i, System.nanoTime(), "data-" + i, i * 2.5);
                            break;
                        case 3:
                            config.logParameterizedMessage(logger, "INFO", 
                                "Complex parameters: user={}, session={}, duration={}ms, success={}", 
                                "user-" + i, "session-" + (i % 100), i * 10, i % 2 == 0);
                            break;
                    }
                }
                
                long endTime = System.nanoTime();
                double durationSeconds = (endTime - startTime) / 1_000_000_000.0;
                double messagesPerSecond = messageCount / durationSeconds;
                results.add(messagesPerSecond);
            }
            
            return calculateThroughputStatistics(results);
            
        } catch (Exception e) {
            throw new RuntimeException("Parameter formatting test failed", e);
        } finally {
            try {
                config.cleanup();
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Measure performance with large messages.
     */
    public PerformanceComparisonRunner.ThroughputResult measureLargeMessagePerformance(
            int messageCount, int messageSize, int iterations) {
        
        try {
            config.initialize();
            Object logger = config.createLogger("performance.large");
            
            // Create large message template
            StringBuilder template = new StringBuilder();
            for (int i = 0; i < messageSize; i++) {
                template.append((char) ('A' + (i % 26)));
            }
            String largeMessage = template.toString();
            
            List<Double> results = new ArrayList<>();
            for (int iter = 0; iter < iterations; iter++) {
                long startTime = System.nanoTime();
                
                for (int i = 0; i < messageCount; i++) {
                    config.logMessage(logger, "INFO", 
                        "Large message " + i + ": " + largeMessage);
                }
                
                long endTime = System.nanoTime();
                double durationSeconds = (endTime - startTime) / 1_000_000_000.0;
                double messagesPerSecond = messageCount / durationSeconds;
                results.add(messagesPerSecond);
            }
            
            return calculateThroughputStatistics(results);
            
        } catch (Exception e) {
            throw new RuntimeException("Large message test failed", e);
        } finally {
            try {
                config.cleanup();
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }
    
    private double runSingleThreadedTest(Object logger, int messageCount) {
        long startTime = System.nanoTime();
        
        for (int i = 0; i < messageCount; i++) {
            config.logParameterizedMessage(logger, "INFO", 
                "Performance test message {} with timestamp {}", i, System.nanoTime());
        }
        
        long endTime = System.nanoTime();
        double durationSeconds = (endTime - startTime) / 1_000_000_000.0;
        return messageCount / durationSeconds;
    }
    
    private double runMultiThreadedTest(int totalMessages, int threadCount) throws InterruptedException {
        int messagesPerThread = totalMessages / threadCount;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        
        // Start all threads
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    Object logger = config.createLogger("performance.multi." + threadId);
                    startLatch.await(); // Wait for all threads to be ready
                    
                    for (int i = 0; i < messagesPerThread; i++) {
                        config.logParameterizedMessage(logger, "INFO", 
                            "Multi-threaded message from thread {} message {} timestamp {}", 
                            threadId, i, System.nanoTime());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        // Start timing
        long startTime = System.nanoTime();
        startLatch.countDown(); // Release all threads
        
        // Wait for completion
        endLatch.await();
        long endTime = System.nanoTime();
        
        executor.shutdown();
        
        double durationSeconds = (endTime - startTime) / 1_000_000_000.0;
        return totalMessages / durationSeconds;
    }
    
    private long[] runLatencyTest(Object logger, int sampleCount) {
        long[] latencies = new long[sampleCount];
        
        for (int i = 0; i < sampleCount; i++) {
            long startTime = System.nanoTime();
            config.logParameterizedMessage(logger, "INFO", 
                "Latency test message {} at {}", i, System.nanoTime());
            long endTime = System.nanoTime();
            
            latencies[i] = endTime - startTime;
        }
        
        return latencies;
    }
    
    private PerformanceComparisonRunner.ThroughputResult calculateThroughputStatistics(List<Double> results) {
        PerformanceComparisonRunner.ThroughputResult result = 
            new PerformanceComparisonRunner.ThroughputResult();
        
        double sum = results.stream().mapToDouble(d -> d).sum();
        result.averageMessagesPerSecond = sum / results.size();
        result.minMessagesPerSecond = results.stream().mapToDouble(d -> d).min().orElse(0);
        result.maxMessagesPerSecond = results.stream().mapToDouble(d -> d).max().orElse(0);
        result.allResults = new ArrayList<>(results);
        
        // Calculate standard deviation
        double variance = results.stream()
            .mapToDouble(d -> Math.pow(d - result.averageMessagesPerSecond, 2))
            .average()
            .orElse(0);
        result.standardDeviation = Math.sqrt(variance);
        
        return result;
    }
    
    private PerformanceComparisonRunner.LatencyResult calculateLatencyStatistics(long[] latencies) {
        PerformanceComparisonRunner.LatencyResult result = 
            new PerformanceComparisonRunner.LatencyResult();
        
        // Convert to microseconds and sort
        double[] latenciesMicros = Arrays.stream(latencies)
            .mapToDouble(l -> l / 1000.0)
            .sorted()
            .toArray();
        
        result.averageLatencyMicros = Arrays.stream(latenciesMicros).average().orElse(0);
        result.minLatencyMicros = latenciesMicros[0];
        result.maxLatencyMicros = latenciesMicros[latenciesMicros.length - 1];
        result.medianLatencyMicros = latenciesMicros[latenciesMicros.length / 2];
        result.p95LatencyMicros = latenciesMicros[(int) (latenciesMicros.length * 0.95)];
        result.p99LatencyMicros = latenciesMicros[(int) (latenciesMicros.length * 0.99)];
        
        return result;
    }
    
    private long getTotalGcCount() {
        return gcBeans.stream().mapToLong(GarbageCollectorMXBean::getCollectionCount).sum();
    }
    
    private long getTotalGcTime() {
        return gcBeans.stream().mapToLong(GarbageCollectorMXBean::getCollectionTime).sum();
    }
}