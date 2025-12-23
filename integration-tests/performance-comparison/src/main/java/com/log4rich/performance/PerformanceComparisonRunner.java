package com.log4rich.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Main runner for performance comparison tests across different logging frameworks.
 * 
 * This class orchestrates comprehensive performance testing including:
 * - Single-threaded throughput tests
 * - Multi-threaded concurrency tests
 * - Memory usage analysis
 * - Latency measurements
 * - Different message patterns and sizes
 */
public class PerformanceComparisonRunner {
    
    private static final int DEFAULT_WARMUP_ITERATIONS = 5;
    private static final int DEFAULT_TEST_ITERATIONS = 10;
    private static final int DEFAULT_MESSAGES_PER_TEST = 100000;
    
    private final ObjectMapper objectMapper;
    private final Map<String, Object> systemInfo;
    
    public PerformanceComparisonRunner() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.systemInfo = collectSystemInfo();
    }
    
    public static void main(String[] args) {
        PerformanceComparisonRunner runner = new PerformanceComparisonRunner();
        
        try {
            Config config = parseArguments(args);
            PerformanceResults results = runner.runFullPerformanceComparison(config);
            
            // Output results
            runner.saveResults(results, config.outputFile);
            runner.printSummary(results);
            
        } catch (Exception e) {
            System.err.println("Performance comparison failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public PerformanceResults runFullPerformanceComparison(Config config) {
        System.out.println("=== Starting Comprehensive Performance Comparison ===");
        System.out.println("Configuration: " + config);
        
        PerformanceResults results = new PerformanceResults();
        results.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        results.systemInfo = systemInfo;
        results.config = config;
        
        // Initialize all logging configurations
        List<LoggingConfiguration> configurations = initializeLoggingConfigurations();
        
        for (LoggingConfiguration loggingConfig : configurations) {
            System.out.println("\\n--- Testing: " + loggingConfig.getName() + " ---");
            
            try {
                LoggingPerformanceTest test = new LoggingPerformanceTest(loggingConfig);
                FrameworkResults frameworkResults = runFrameworkTests(test, config);
                results.frameworkResults.put(loggingConfig.getName(), frameworkResults);
                
                System.out.println("✓ " + loggingConfig.getName() + " completed");
                
            } catch (Exception e) {
                System.err.println("✗ " + loggingConfig.getName() + " failed: " + e.getMessage());
                FrameworkResults errorResults = new FrameworkResults();
                errorResults.error = e.getMessage();
                results.frameworkResults.put(loggingConfig.getName(), errorResults);
            }
        }
        
        // Generate comparative analysis
        results.analysis = generateComparativeAnalysis(results.frameworkResults);
        
        System.out.println("\\n=== Performance Comparison Complete ===");
        return results;
    }
    
    private List<LoggingConfiguration> initializeLoggingConfigurations() {
        List<LoggingConfiguration> configs = new ArrayList<>();
        
        // SLF4J → log4j2 → log4Rich (our bridge chain)
        configs.add(new SLF4JLog4RichConfiguration());
        
        // Standard SLF4J → Logback
        configs.add(new SLF4JLogbackConfiguration());
        
        // Standard SLF4J → log4j2
        configs.add(new SLF4JLog4j2Configuration());
        
        // Direct log4Rich usage
        configs.add(new DirectLog4RichConfiguration());
        
        // Legacy log4j 1.x
        configs.add(new Log4j1Configuration());
        
        // Java Util Logging
        configs.add(new JULConfiguration());
        
        return configs;
    }
    
    private FrameworkResults runFrameworkTests(LoggingPerformanceTest test, Config config) {
        FrameworkResults results = new FrameworkResults();
        
        // Single-threaded throughput test
        System.out.print("  Single-threaded throughput... ");
        results.singleThreadedThroughput = test.measureSingleThreadedThroughput(
            config.messagesPerTest, config.warmupIterations, config.testIterations);
        System.out.println(String.format("%.0f msg/s", results.singleThreadedThroughput.averageMessagesPerSecond));
        
        // Multi-threaded throughput test
        System.out.print("  Multi-threaded throughput... ");
        results.multiThreadedThroughput = test.measureMultiThreadedThroughput(
            config.messagesPerTest, 4, config.warmupIterations, config.testIterations);
        System.out.println(String.format("%.0f msg/s", results.multiThreadedThroughput.averageMessagesPerSecond));
        
        // Latency test
        System.out.print("  Latency measurement... ");
        results.latency = test.measureLatency(10000, config.warmupIterations);
        System.out.println(String.format("%.2f μs avg", results.latency.averageLatencyMicros));
        
        // Memory usage test
        System.out.print("  Memory usage... ");
        results.memoryUsage = test.measureMemoryUsage(config.messagesPerTest);
        System.out.println(String.format("%.2f MB", results.memoryUsage.peakMemoryUsageMB));
        
        // Parameter formatting test
        System.out.print("  Parameter formatting... ");
        results.parameterFormatting = test.measureParameterFormattingPerformance(
            config.messagesPerTest / 10, config.testIterations);
        System.out.println(String.format("%.0f msg/s", results.parameterFormatting.averageMessagesPerSecond));
        
        // Large message test
        System.out.print("  Large messages... ");
        results.largeMessages = test.measureLargeMessagePerformance(
            config.messagesPerTest / 100, 1024, config.testIterations);
        System.out.println(String.format("%.0f msg/s", results.largeMessages.averageMessagesPerSecond));
        
        return results;
    }
    
    private ComparativeAnalysis generateComparativeAnalysis(Map<String, FrameworkResults> results) {
        ComparativeAnalysis analysis = new ComparativeAnalysis();
        
        // Find best performing configuration for each metric
        analysis.bestSingleThreaded = findBestPerforming(results, "singleThreadedThroughput");
        analysis.bestMultiThreaded = findBestPerforming(results, "multiThreadedThroughput");
        analysis.lowestLatency = findLowestLatency(results);
        analysis.lowestMemoryUsage = findLowestMemoryUsage(results);
        
        // Calculate relative performance
        analysis.relativePerformance = calculateRelativePerformance(results);
        
        // Generate recommendations
        analysis.recommendations = generateRecommendations(results);
        
        return analysis;
    }
    
    private String findBestPerforming(Map<String, FrameworkResults> results, String metric) {
        return results.entrySet().stream()
            .filter(entry -> entry.getValue().error == null)
            .max((e1, e2) -> {
                double val1 = getMetricValue(e1.getValue(), metric);
                double val2 = getMetricValue(e2.getValue(), metric);
                return Double.compare(val1, val2);
            })
            .map(Map.Entry::getKey)
            .orElse("None");
    }
    
    private String findLowestLatency(Map<String, FrameworkResults> results) {
        return results.entrySet().stream()
            .filter(entry -> entry.getValue().error == null && entry.getValue().latency != null)
            .min((e1, e2) -> Double.compare(
                e1.getValue().latency.averageLatencyMicros,
                e2.getValue().latency.averageLatencyMicros))
            .map(Map.Entry::getKey)
            .orElse("None");
    }
    
    private String findLowestMemoryUsage(Map<String, FrameworkResults> results) {
        return results.entrySet().stream()
            .filter(entry -> entry.getValue().error == null && entry.getValue().memoryUsage != null)
            .min((e1, e2) -> Double.compare(
                e1.getValue().memoryUsage.peakMemoryUsageMB,
                e2.getValue().memoryUsage.peakMemoryUsageMB))
            .map(Map.Entry::getKey)
            .orElse("None");
    }
    
    private double getMetricValue(FrameworkResults results, String metric) {
        switch (metric) {
            case "singleThreadedThroughput":
                return results.singleThreadedThroughput != null ? 
                    results.singleThreadedThroughput.averageMessagesPerSecond : 0;
            case "multiThreadedThroughput":
                return results.multiThreadedThroughput != null ? 
                    results.multiThreadedThroughput.averageMessagesPerSecond : 0;
            default:
                return 0;
        }
    }
    
    private Map<String, Map<String, Double>> calculateRelativePerformance(Map<String, FrameworkResults> results) {
        Map<String, Map<String, Double>> relative = new HashMap<>();
        
        // Find baseline (first successful result)
        String baseline = results.entrySet().stream()
            .filter(entry -> entry.getValue().error == null)
            .findFirst()
            .map(Map.Entry::getKey)
            .orElse(null);
        
        if (baseline == null) return relative;
        
        FrameworkResults baselineResults = results.get(baseline);
        
        for (Map.Entry<String, FrameworkResults> entry : results.entrySet()) {
            if (entry.getValue().error != null) continue;
            
            Map<String, Double> frameworkRelative = new HashMap<>();
            FrameworkResults current = entry.getValue();
            
            if (current.singleThreadedThroughput != null && baselineResults.singleThreadedThroughput != null) {
                frameworkRelative.put("singleThreadedThroughput", 
                    current.singleThreadedThroughput.averageMessagesPerSecond / 
                    baselineResults.singleThreadedThroughput.averageMessagesPerSecond);
            }
            
            if (current.multiThreadedThroughput != null && baselineResults.multiThreadedThroughput != null) {
                frameworkRelative.put("multiThreadedThroughput", 
                    current.multiThreadedThroughput.averageMessagesPerSecond / 
                    baselineResults.multiThreadedThroughput.averageMessagesPerSecond);
            }
            
            relative.put(entry.getKey(), frameworkRelative);
        }
        
        return relative;
    }
    
    private List<String> generateRecommendations(Map<String, FrameworkResults> results) {
        List<String> recommendations = new ArrayList<>();
        
        // Find SLF4J → log4j2 → log4Rich results
        FrameworkResults slf4jLog4Rich = results.get("SLF4J → log4j2 → log4Rich");
        FrameworkResults slf4jLogback = results.get("SLF4J → Logback");
        
        if (slf4jLog4Rich != null && slf4jLog4Rich.error == null) {
            if (slf4jLogback != null && slf4jLogback.error == null) {
                double improvement = slf4jLog4Rich.singleThreadedThroughput.averageMessagesPerSecond / 
                                   slf4jLogback.singleThreadedThroughput.averageMessagesPerSecond;
                
                if (improvement > 1.5) {
                    recommendations.add(String.format(
                        "SLF4J → log4j2 → log4Rich shows %.1fx performance improvement over standard Logback", 
                        improvement));
                }
            }
            
            recommendations.add("For SLF4J applications, use log4j2-log4Rich bridge for significant performance gains");
        }
        
        recommendations.add("For maximum performance, consider direct log4Rich usage when possible");
        recommendations.add("Enable async logging and batch processing for high-volume scenarios");
        
        return recommendations;
    }
    
    private void saveResults(PerformanceResults results, String outputFile) throws IOException {
        if (outputFile != null) {
            File file = new File(outputFile);
            file.getParentFile().mkdirs();
            objectMapper.writeValue(file, results);
            System.out.println("Results saved to: " + file.getAbsolutePath());
        }
    }
    
    private void printSummary(PerformanceResults results) {
        System.out.println("\\n=== PERFORMANCE COMPARISON SUMMARY ===");
        
        System.out.println("\\nBest Performers:");
        System.out.println("  Single-threaded: " + results.analysis.bestSingleThreaded);
        System.out.println("  Multi-threaded:  " + results.analysis.bestMultiThreaded);
        System.out.println("  Lowest latency:  " + results.analysis.lowestLatency);
        System.out.println("  Lowest memory:   " + results.analysis.lowestMemoryUsage);
        
        System.out.println("\\nThroughput Results (messages/second):");
        for (Map.Entry<String, FrameworkResults> entry : results.frameworkResults.entrySet()) {
            FrameworkResults result = entry.getValue();
            if (result.error != null) {
                System.out.println(String.format("  %-25s: ERROR - %s", entry.getKey(), result.error));
            } else {
                System.out.println(String.format("  %-25s: %8.0f (single) | %8.0f (multi)", 
                    entry.getKey(),
                    result.singleThreadedThroughput.averageMessagesPerSecond,
                    result.multiThreadedThroughput.averageMessagesPerSecond));
            }
        }
        
        System.out.println("\\nRecommendations:");
        for (String recommendation : results.analysis.recommendations) {
            System.out.println("  • " + recommendation);
        }
    }
    
    private static Config parseArguments(String[] args) {
        Config config = new Config();
        
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--output":
                    if (i + 1 < args.length) config.outputFile = args[++i];
                    break;
                case "--warmup":
                    if (i + 1 < args.length) config.warmupIterations = Integer.parseInt(args[++i]);
                    break;
                case "--iterations":
                    if (i + 1 < args.length) config.testIterations = Integer.parseInt(args[++i]);
                    break;
                case "--messages":
                    if (i + 1 < args.length) config.messagesPerTest = Integer.parseInt(args[++i]);
                    break;
                case "--help":
                    printUsage();
                    System.exit(0);
                    break;
            }
        }
        
        return config;
    }
    
    private static void printUsage() {
        System.out.println("Usage: PerformanceComparisonRunner [options]");
        System.out.println("Options:");
        System.out.println("  --output <file>      Output file for results (JSON)");
        System.out.println("  --warmup <n>         Number of warmup iterations (default: 5)");
        System.out.println("  --iterations <n>     Number of test iterations (default: 10)");
        System.out.println("  --messages <n>       Messages per test (default: 100000)");
        System.out.println("  --help               Show this help message");
    }
    
    private Map<String, Object> collectSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        
        Runtime runtime = Runtime.getRuntime();
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("javaVendor", System.getProperty("java.vendor"));
        info.put("osName", System.getProperty("os.name"));
        info.put("osVersion", System.getProperty("os.version"));
        info.put("osArch", System.getProperty("os.arch"));
        info.put("availableProcessors", runtime.availableProcessors());
        info.put("maxMemoryMB", runtime.maxMemory() / (1024 * 1024));
        info.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return info;
    }
    
    // Configuration and result classes
    public static class Config {
        public int warmupIterations = DEFAULT_WARMUP_ITERATIONS;
        public int testIterations = DEFAULT_TEST_ITERATIONS;
        public int messagesPerTest = DEFAULT_MESSAGES_PER_TEST;
        public String outputFile = null;
        
        @Override
        public String toString() {
            return String.format("Config{warmup=%d, iterations=%d, messages=%d, output=%s}", 
                warmupIterations, testIterations, messagesPerTest, outputFile);
        }
    }
    
    public static class PerformanceResults {
        public String timestamp;
        public Map<String, Object> systemInfo;
        public Config config;
        public Map<String, FrameworkResults> frameworkResults = new HashMap<>();
        public ComparativeAnalysis analysis;
    }
    
    public static class FrameworkResults {
        public ThroughputResult singleThreadedThroughput;
        public ThroughputResult multiThreadedThroughput;
        public LatencyResult latency;
        public MemoryUsageResult memoryUsage;
        public ThroughputResult parameterFormatting;
        public ThroughputResult largeMessages;
        public String error;
    }
    
    public static class ThroughputResult {
        public double averageMessagesPerSecond;
        public double standardDeviation;
        public double minMessagesPerSecond;
        public double maxMessagesPerSecond;
        public List<Double> allResults = new ArrayList<>();
    }
    
    public static class LatencyResult {
        public double averageLatencyMicros;
        public double medianLatencyMicros;
        public double p95LatencyMicros;
        public double p99LatencyMicros;
        public double minLatencyMicros;
        public double maxLatencyMicros;
    }
    
    public static class MemoryUsageResult {
        public double peakMemoryUsageMB;
        public double memoryGrowthMB;
        public long gcEvents;
        public double gcTimeMicros;
    }
    
    public static class ComparativeAnalysis {
        public String bestSingleThreaded;
        public String bestMultiThreaded;
        public String lowestLatency;
        public String lowestMemoryUsage;
        public Map<String, Map<String, Double>> relativePerformance;
        public List<String> recommendations;
    }
}