# Performance Comparison Suite

This module provides comprehensive performance benchmarking across different logging configurations to validate the performance benefits of the log4j2-log4Rich bridge.

## Test Coverage

### Logging Configurations Tested

1. **SLF4J → log4j2 → log4Rich** - Our primary bridge chain
2. **SLF4J → Logback** - Standard SLF4J baseline
3. **SLF4J → log4j2** - Standard log4j2 baseline  
4. **Direct log4Rich** - Maximum performance baseline
5. **log4j 1.x** - Legacy logging baseline
6. **Java Util Logging** - Built-in Java logging baseline

### Performance Metrics

#### Throughput Tests
- **Single-threaded throughput**: Messages per second from single thread
- **Multi-threaded throughput**: Messages per second from multiple concurrent threads
- **Parameter formatting**: Performance with SLF4J-style {} placeholders
- **Large messages**: Performance with large message payloads

#### Latency Tests
- **Average latency**: Mean time per log operation
- **Percentile latency**: P95 and P99 latency measurements
- **Latency distribution**: Min, max, median latency

#### Resource Usage Tests
- **Memory usage**: Peak memory consumption during logging
- **Memory growth**: Memory allocation patterns
- **GC impact**: Garbage collection events and timing

## Running the Performance Tests

### Prerequisites
```bash
# Ensure log4Rich is available (install to local Maven repo)
cd ../../log4Rich
mvn clean install

# Ensure log4j2-log4Rich bridge is built
cd ../log4j2-log4Rich
mvn clean install
```

### Run Complete Performance Suite
```bash
# Run all performance tests with default settings
mvn exec:java

# Run with custom parameters
mvn exec:java -Dexec.args="--messages 50000 --iterations 5 --output results.json"

# Run specific configuration only (manual)
mvn compile exec:java -Dexec.mainClass="com.log4rich.performance.PerformanceComparisonRunner" \
  -Dexec.args="--warmup 3 --iterations 5"
```

### Run JMH Benchmarks (Optional)
```bash
# Build and run JMH benchmarks
mvn clean package -Pbenchmark
java -jar target/benchmarks.jar
```

### Command Line Options

```bash
--output <file>      # Output file for results (JSON format)
--warmup <n>         # Number of warmup iterations (default: 5)
--iterations <n>     # Number of test iterations (default: 10)  
--messages <n>       # Messages per test (default: 100000)
--help              # Show help message
```

## Expected Results

### Performance Comparison Matrix

| Configuration | Single-Thread | Multi-Thread | Avg Latency | Memory |
|---------------|---------------|--------------|-------------|---------|
| SLF4J → log4j2 → log4Rich | ~85K msg/s | ~300K msg/s | ~12 μs | Low |
| SLF4J → Logback | ~60K msg/s | ~180K msg/s | ~17 μs | Medium |
| SLF4J → log4j2 | ~70K msg/s | ~220K msg/s | ~14 μs | Medium |
| Direct log4Rich | ~120K msg/s | ~400K msg/s | ~8 μs | Lowest |
| log4j 1.x | ~45K msg/s | ~120K msg/s | ~22 μs | High |
| Java Util Logging | ~35K msg/s | ~90K msg/s | ~28 μs | Medium |

*Note: Actual results will vary based on system configuration*

### Key Validation Points

✅ **SLF4J → log4j2 → log4Rich should outperform standard Logback**  
✅ **Bridge overhead should be minimal compared to direct log4Rich usage**  
✅ **Multi-threaded performance should scale well**  
✅ **Memory usage should be competitive or better**  
✅ **Parameter formatting should be efficient**

## Test Configuration

### Benchmark Settings
- **Message Count**: 100,000 messages per test (configurable)
- **Warmup Iterations**: 5 iterations to JIT warm-up
- **Test Iterations**: 10 iterations for statistical accuracy
- **Thread Count**: 4 threads for multi-threaded tests
- **Message Patterns**: Various complexity levels

### Logging Configuration
All configurations use:
- File appenders (console disabled for consistent timing)
- Immediate flush disabled for performance
- 64KB buffer sizes
- Simple patterns for fair comparison
- DEBUG level enabled

### System Information Captured
- Java version and vendor
- Operating system details
- Available CPU cores
- Maximum heap memory
- Test timestamp

## Output Format

### JSON Results Structure
```json
{
  "timestamp": "2025-07-25T11:00:00",
  "systemInfo": {
    "javaVersion": "11.0.2",
    "osName": "Linux",
    "availableProcessors": 8,
    "maxMemoryMB": 4096
  },
  "frameworkResults": {
    "SLF4J → log4j2 → log4Rich": {
      "singleThreadedThroughput": {
        "averageMessagesPerSecond": 85432.1,
        "standardDeviation": 2341.5
      },
      "multiThreadedThroughput": {
        "averageMessagesPerSecond": 298765.4
      },
      "latency": {
        "averageLatencyMicros": 11.7,
        "p95LatencyMicros": 18.2,
        "p99LatencyMicros": 25.1
      },
      "memoryUsage": {
        "peakMemoryUsageMB": 125.3,
        "gcEvents": 2
      }
    }
  },
  "analysis": {
    "bestSingleThreaded": "Direct log4Rich",
    "bestMultiThreaded": "Direct log4Rich", 
    "lowestLatency": "Direct log4Rich",
    "recommendations": [
      "SLF4J → log4j2 → log4Rich shows 1.4x performance improvement over standard Logback",
      "For SLF4J applications, use log4j2-log4Rich bridge for significant performance gains"
    ]
  }
}
```

### Console Summary
```
=== PERFORMANCE COMPARISON SUMMARY ===

Best Performers:
  Single-threaded: Direct log4Rich
  Multi-threaded:  Direct log4Rich
  Lowest latency:  Direct log4Rich
  Lowest memory:   SLF4J → log4j2 → log4Rich

Throughput Results (messages/second):
  SLF4J → log4j2 → log4Rich:    85432 (single) |   298765 (multi)
  SLF4J → Logback:              60123 (single) |   180234 (multi)
  SLF4J → log4j2:               70456 (single) |   220123 (multi)
  Direct log4Rich:             120234 (single) |   401234 (multi)
  log4j 1.x:                    45123 (single) |   120456 (multi)
  Java Util Logging:            35234 (single) |    90123 (multi)

Recommendations:
  • SLF4J → log4j2 → log4Rich shows 1.4x performance improvement over standard Logback
  • For SLF4J applications, use log4j2-log4Rich bridge for significant performance gains
  • For maximum performance, consider direct log4Rich usage when possible
```

## Troubleshooting

### Common Issues

**Issue**: Tests fail with ClassNotFoundException  
**Solution**: Ensure all dependencies are in local Maven repository

**Issue**: JMH benchmarks don't run  
**Solution**: Run `mvn clean package` first to build the benchmark JAR

**Issue**: Results show unexpected performance  
**Solution**: Check system load, disable antivirus, ensure consistent test environment

**Issue**: Memory usage tests fail  
**Solution**: Increase JVM heap size: `-Xmx2g`

### Performance Tuning

For most accurate results:
- Run on dedicated test machine
- Disable background processes
- Use consistent JVM settings
- Run multiple test iterations
- Warm up JVM before testing

### Debug Mode

Enable debug logging:
```bash
mvn exec:java -Dlog4rich.level=DEBUG -Dexec.args="--messages 1000"
```

## Integration with CI/CD

### Performance Regression Detection
```bash
# Save baseline results
mvn exec:java -Dexec.args="--output baseline-results.json"

# Compare with current results (custom script)
./compare-performance.sh baseline-results.json current-results.json
```

### Automated Testing
```bash
# Quick performance check (reduced iterations)
mvn exec:java -Dexec.args="--messages 10000 --iterations 3 --warmup 2"
```

## Validation Goals

This performance comparison suite validates:

1. **Bridge Performance**: log4j2-log4Rich bridge adds minimal overhead
2. **SLF4J Compatibility**: Performance with SLF4J patterns is excellent  
3. **Competitive Advantage**: Our solution outperforms standard alternatives
4. **Scalability**: Multi-threaded performance scales appropriately
5. **Resource Efficiency**: Memory usage and GC impact are optimized

The results provide concrete evidence for the performance benefits of adopting the log4j2-log4Rich bridge for SLF4J applications.