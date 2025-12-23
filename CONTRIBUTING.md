# Contributing to log4j2-log4Rich Bridge

Thank you for your interest in contributing to the log4j2-log4Rich bridge! This document provides guidelines and information for contributors.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [Testing Guidelines](#testing-guidelines)
- [Documentation Requirements](#documentation-requirements)
- [Pull Request Process](#pull-request-process)
- [Issue Reporting](#issue-reporting)

## Code of Conduct

This project adheres to a code of conduct that promotes a welcoming and inclusive environment for all contributors. By participating, you agree to:

- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Gracefully accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6+
- Git
- Access to log4Rich dependency (install locally if needed)

### Development Setup

1. **Fork and Clone**
   ```bash
   git clone https://github.com/yourusername/log4j2-log4Rich.git
   cd log4j2-log4Rich
   ```

2. **Install Dependencies**
   ```bash
   # If log4Rich is not in public repository, install locally
   cd ../log4Rich
   mvn clean install
   cd ../log4j2-log4Rich
   ```

3. **Build and Test**
   ```bash
   mvn clean compile
   mvn test
   ```

4. **Run Demo**
   ```bash
   mvn exec:java -Dexec.mainClass="demo.Log4j2BridgeDemo"
   ```

## How to Contribute

### Types of Contributions

We welcome several types of contributions:

- **Bug Reports**: Report issues with existing functionality
- **Feature Requests**: Suggest new features or improvements
- **Code Contributions**: Fix bugs or implement new features
- **Documentation**: Improve or expand documentation
- **Performance**: Optimize existing code for better performance
- **Testing**: Add or improve test coverage

### Areas for Contribution

1. **API Completeness**: Ensure all log4j2 methods are implemented
2. **Performance Optimization**: Improve logging throughput and reduce latency
3. **Documentation**: Enhance guides, examples, and API documentation
4. **Testing**: Increase test coverage and add edge case tests
5. **Integration**: Support for additional frameworks and environments
6. **Bug Fixes**: Address reported issues and edge cases

## Development Workflow

### Branch Strategy

- `main`: Stable release branch
- `develop`: Integration branch for new features
- `feature/feature-name`: Feature development branches
- `bugfix/issue-number`: Bug fix branches
- `hotfix/critical-issue`: Critical production fixes

### Feature Development

1. **Create Feature Branch**
   ```bash
   git checkout -b feature/new-logging-method
   ```

2. **Implement Changes**
   - Follow coding standards
   - Add comprehensive tests
   - Update documentation
   - Ensure performance requirements

3. **Test Thoroughly**
   ```bash
   mvn clean test
   mvn exec:java -Dexec.mainClass="demo.Log4j2BridgeDemo"
   ```

4. **Commit and Push**
   ```bash
   git add .
   git commit -m "Add new logging method with CustomObject support"
   git push origin feature/new-logging-method
   ```

5. **Create Pull Request**

## Coding Standards

### Java Code Style

- **Indentation**: 4 spaces (no tabs)
- **Line Length**: 120 characters maximum
- **Naming Conventions**:
  - Classes: PascalCase (`LoggingEngine`)
  - Methods: camelCase (`extractMessage`)
  - Constants: UPPER_SNAKE_CASE (`DEFAULT_TIMEOUT`)
  - Variables: camelCase (`messageCount`)

### Code Organization

- **Package Structure**: Follow existing organization
- **Class Design**: Single responsibility principle
- **Method Size**: Keep methods focused and concise
- **Comments**: JavaDoc for public APIs, inline comments for complex logic

### Example Code Style

```java
/**
 * Extracts message string from various log4j2 message objects.
 * Supports String, Message, Supplier, and parameter formatting.
 * 
 * @param messageObj the message object (String, Message, Supplier, etc.)
 * @param params optional parameters for formatting
 * @return formatted message string
 * @throws IllegalArgumentException if messageObj is null
 */
public static String extractMessage(Object messageObj, Object... params) {
    if (messageObj == null) {
        throw new IllegalArgumentException("Message object cannot be null");
    }
    
    // Implementation details...
    return formattedMessage;
}
```

## Testing Guidelines

### Test Coverage Requirements

- **Minimum Coverage**: 90% line coverage
- **Critical Components**: 95% coverage for LoggingEngine and utilities
- **All Public Methods**: Must have test coverage
- **Edge Cases**: Test null inputs, empty parameters, exceptions

### Test Categories

1. **Unit Tests**: Individual component testing
   ```java
   @Test
   public void testMessageExtraction() {
       String result = MessageExtractor.extractMessage("Test message {}", "param");
       assertEquals("Test message param", result);
   }
   ```

2. **Integration Tests**: End-to-end functionality
   ```java
   @Test
   public void testFullLoggingFlow() {
       Logger logger = LogManager.getLogger(TestClass.class);
       logger.info("Integration test message {}", 123);
       // Verify log4Rich received message correctly
   }
   ```

3. **Performance Tests**: Throughput and latency
   ```java
   @Test
   public void testPerformance() {
       // Measure messages per second
       // Assert minimum performance threshold
   }
   ```

### Test Naming

- Descriptive test method names
- Format: `test[Component][Scenario][ExpectedResult]`
- Example: `testMessageExtractionWithNullParameterThrowsException`

## Documentation Requirements

### JavaDoc Standards

All public methods and classes must have comprehensive JavaDoc:

```java
/**
 * Brief description of what the method does.
 * 
 * <p>More detailed explanation if needed, including usage examples:
 * <pre>{@code
 * Logger logger = LogManager.getLogger(MyClass.class);
 * logger.info("Message with parameter {}", value);
 * }</pre>
 * 
 * @param paramName description of parameter
 * @return description of return value
 * @throws ExceptionType when this exception is thrown
 * @since 1.0.0
 * @see RelatedClass#relatedMethod
 */
```

### Documentation Updates

When making changes, update relevant documentation:

- **README.md**: For feature additions or API changes
- **Developer Guide**: For architecture or workflow changes
- **Usage Guide**: For new features or usage patterns
- **JavaDoc**: For all code changes

## Pull Request Process

### Before Submitting

1. **Code Quality**
   - [ ] Code follows style guidelines
   - [ ] All tests pass
   - [ ] No compiler warnings
   - [ ] Performance requirements met

2. **Documentation**
   - [ ] JavaDoc updated for all changes
   - [ ] README updated if needed
   - [ ] Examples provided for new features

3. **Testing**
   - [ ] New tests added for new functionality
   - [ ] Existing tests still pass
   - [ ] Performance tests run successfully

### Pull Request Template

```markdown
## Description
Brief description of changes made.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## Testing
- [ ] All existing tests pass
- [ ] New tests added for new functionality
- [ ] Performance tests run

## Performance Impact
- [ ] No performance impact
- [ ] Performance improved
- [ ] Performance impact acceptable (explain below)

## Checklist
- [ ] My code follows the style guidelines
- [ ] I have performed a self-review of my code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
```

### Review Process

1. **Automated Checks**: CI pipeline runs tests and checks
2. **Code Review**: Maintainer reviews code quality and design
3. **Performance Review**: Performance impact assessment
4. **Documentation Review**: Ensure documentation is complete
5. **Final Approval**: Project maintainer approval

## Issue Reporting

### Bug Reports

When reporting bugs, include:

- **Environment**: Java version, OS, Maven version
- **log4Rich Version**: Version being used
- **Bridge Version**: Bridge version
- **Steps to Reproduce**: Clear, minimal reproduction steps
- **Expected Behavior**: What you expected to happen
- **Actual Behavior**: What actually happened
- **Error Messages**: Full stack traces and error messages
- **Configuration**: Relevant configuration files

### Feature Requests

For new features, provide:

- **Use Case**: Why is this feature needed?
- **Proposed Solution**: How should it work?
- **Alternatives**: What alternatives were considered?
- **Implementation Notes**: Any implementation considerations

### Issue Template

```markdown
**Issue Type**: Bug Report / Feature Request

**Environment**:
- Java Version: 
- OS: 
- Maven Version: 
- log4Rich Version: 
- Bridge Version: 

**Description**:
Clear description of the issue or feature request.

**Steps to Reproduce** (for bugs):
1. Step 1
2. Step 2
3. Step 3

**Expected Behavior**:
What you expected to happen.

**Actual Behavior**:
What actually happened.

**Additional Context**:
Any additional information, configuration files, or screenshots.
```

## Performance Considerations

### Performance Requirements

- **Throughput**: Maintain minimum 50,000 messages/second
- **Latency**: Maximum 10 microseconds overhead per log call
- **Memory**: No memory leaks, efficient object usage
- **CPU**: Minimal CPU overhead for disabled log levels

### Performance Testing

```java
@Test
public void testThroughputRequirement() {
    Logger logger = LogManager.getLogger(PerformanceTest.class);
    
    long startTime = System.nanoTime();
    int messageCount = 100000;
    
    for (int i = 0; i < messageCount; i++) {
        logger.debug("Performance test message {} at {}", i, System.nanoTime());
    }
    
    long endTime = System.nanoTime();
    double messagesPerSecond = messageCount / ((endTime - startTime) / 1_000_000_000.0);
    
    assertTrue("Performance below requirement", messagesPerSecond > 50000);
}
```

## Getting Help

### Communication Channels

- **GitHub Issues**: For bug reports and feature requests
- **GitHub Discussions**: For questions and general discussion
- **Pull Request Comments**: For specific code review discussions

### Documentation Resources

- [Developer Guide](docs/DEVELOPER_GUIDE.md): Architecture and development details
- [Quick Start Guide](docs/QUICK_START_GUIDE.md): Get started quickly
- [Usage Guide](docs/USAGE_GUIDE.md): Comprehensive feature documentation
- [JavaDoc](target/site/apidocs/index.html): Complete API documentation

### Maintainer Contact

For project-related questions or discussion about major contributions, reach out through GitHub issues or discussions.

## Recognition

Contributors are recognized in:

- **README.md**: Contributor acknowledgments
- **Release Notes**: Major contribution recognition
- **GitHub Contributors**: Automatic GitHub recognition

Thank you for contributing to log4j2-log4Rich bridge! Your contributions help make high-performance logging accessible to the entire log4j2 community.