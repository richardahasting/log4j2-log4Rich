# Work In Progress

## Current Task: Fix Missing LogManager.getFactory() for Spring Boot Compatibility (Issue #2)

**Date:** 2025-09-30

### Problem
Spring Boot applications fail to start when using log4j2-log4Rich as a drop-in replacement because `LogManager.getFactory()` and related SPI methods are missing.

### Plan
1. Examine current LogManager implementation
2. Research log4j2 SPI interfaces (LoggerContextFactory, LoggerContext)
3. Implement LoggerContext interface for log4Rich
4. Implement LoggerContextFactory interface
5. Add getFactory() and getContext() methods to LogManager
6. Build and test
7. Verify Spring Boot compatibility

### Status
- Started implementation