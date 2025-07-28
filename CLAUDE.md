# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a log4j2 to log4Rich bridge library that provides complete log4j2 API compatibility while delegating to the high-performance log4Rich logging framework. The bridge implements all 180+ log4j2 methods using a sophisticated layered architecture that eliminates code duplication.

### Key Architecture

- **Layer 1: Utility Classes** - Handle level translation, message extraction, marker processing, and context bridging
- **Layer 2: Central Logging Engine** - Single point where all logging methods converge (`LoggingEngine.java`)
- **Layer 3: API Implementation** - Complete log4j2 API implementation that delegates to the engine

### Core Components

- `src/main/java/org/apache/logging/log4j/` - Complete log4j2 API implementation
- `src/main/java/com/log4rich/log4j2/bridge/` - Bridge utilities and central engine
- `demo/Log4j2BridgeDemo.java` - Comprehensive feature demonstration
- `integration-tests/` - Integration tests with Spring Boot, Kafka, performance comparisons

## Build and Development Commands

### Core Maven Commands
```bash
# Build the project
mvn clean compile

# Run tests
mvn test

# Build with integration tests
mvn clean install -Pintegration-tests

# Build with performance tests
mvn clean install -Pperformance-tests

# Package with shaded jar
mvn clean package

# Generate JavaDoc
mvn javadoc:javadoc

# Run demo
mvn exec:java -Dexec.mainClass="demo.Log4j2BridgeDemo"
```

### Integration Testing
```bash
# Run performance comparison tests
cd integration-tests/performance-comparison
./run-performance-tests.sh

# Test individual modules
mvn test -pl integration-tests/spring-boot-web
mvn test -pl integration-tests/kafka-messaging
```

### Project Dependencies

The project depends on:
- **log4Rich core library** (v1.0.4) - The underlying high-performance logging framework
- **JUnit 5** for testing
- **Maven Shade Plugin** to create fat jars with dependencies included

Integration tests include Spring Boot, Kafka, and performance comparison dependencies managed through the parent integration-tests POM.

## Code Standards and Patterns

### Architectural Patterns

1. **Delegation Pattern**: All 180+ Logger methods are simple one-liners that delegate to `LoggingEngine.log()`
2. **Single Responsibility**: Each utility class has one specific purpose (level translation, message extraction, etc.)
3. **Performance First**: Early returns for disabled log levels, minimal object creation
4. **Thread Safety**: All components are thread-safe by design

### Key Implementation Details

- **LoggingEngine.java**: Central hub - ALL logging methods funnel through here
- **LevelTranslator.java**: Maps log4j2 levels to log4Rich levels
- **MessageExtractor.java**: Handles all message types (String, Message, Supplier, parameters)
- **MarkerHandler.java**: Processes log4j2 markers and hierarchy
- **ThreadContextProvider.java**: Bridges log4j2 ThreadContext to log4Rich context

### Coding Conventions

- Use existing JavaDoc patterns - comprehensive documentation for all public methods
- Follow the delegation pattern - new Logger methods should be one-liners calling LoggingEngine
- Maintain thread safety in all utility classes
- Add null checks where appropriate but keep performance optimal
- Use early returns for performance optimization (level checking, marker filtering)

## Testing Strategy

### Unit Tests
- No unit tests currently in the main project (relies on integration tests)
- Bridge methods are tested through comprehensive integration tests

### Integration Tests
- **Spring Boot Integration** (`integration-tests/spring-boot-web/`) - Tests web application logging
- **Kafka Integration** (`integration-tests/kafka-messaging/`) - Tests message processing logging
- **Performance Tests** (`integration-tests/performance-comparison/`) - Benchmarks against other logging frameworks

### Demo and Validation
- `demo/Log4j2BridgeDemo.java` provides comprehensive feature validation
- Demonstrates all major log4j2 features: basic logging, parameter formatting, exceptions, markers, ThreadContext, level checking, message objects, lambda support, and performance

## Development Workflow

### Making Changes

1. **Understand the Architecture**: All logging methods should delegate to LoggingEngine
2. **Follow Existing Patterns**: Look at existing Logger methods for delegation patterns
3. **Add JavaDoc**: Comprehensive documentation is required for all public methods
4. **Test Integration**: Run demo and integration tests to verify changes
5. **Performance Considerations**: Ensure changes don't impact the performance characteristics

### Common Tasks

- **Adding new Logger methods**: Implement as one-liner delegation to LoggingEngine
- **Modifying logging behavior**: Changes should go in LoggingEngine or utility classes
- **Adding message types**: Extend MessageExtractor to handle new Message implementations
- **Context integration**: Modify ThreadContextProvider for new context features

### Build Verification

Always run these commands before committing:
```bash
mvn clean compile
mvn test
mvn exec:java -Dexec.mainClass="demo.Log4j2BridgeDemo"
```

## Configuration Files

- `log4rich.properties` - Runtime configuration for log4Rich backend
- `log4Rich.sample.config` - Sample configuration file
- Maven profiles for integration-tests and performance-tests

## Documentation Structure

- `README.md` - Comprehensive user documentation with API examples
- `docs/DEVELOPER_GUIDE.md` - Detailed architecture and development information  
- `docs/QUICK_START_GUIDE.md` - Quick setup instructions
- `docs/USAGE_GUIDE.md` - In-depth usage examples
- JavaDoc in `src/main/javadoc/overview.html`

The project prioritizes performance and complete API compatibility while maintaining clean, maintainable code through its layered architecture.