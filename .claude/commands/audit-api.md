Audit the log4j2-log4Rich bridge against the real Apache log4j-api to find missing interfaces or methods.

## Steps

1. **Fetch real API source**: For each SPI file in our `src/main/java/org/apache/logging/log4j/`, fetch the corresponding file from the real log4j-api on GitHub at `https://raw.githubusercontent.com/apache/logging-log4j2/2.x/log4j-api/src/main/java/` and compare method signatures.

2. **Key files to audit** (in priority order):
   - `util/StackLocatorUtil.java` — commons-logging 1.3.x depends on specific overloads
   - `spi/LoggerContextFactory.java` — Spring Boot calls shutdown()
   - `Logger.java` — base interface, must match real API
   - `spi/ExtendedLogger.java` — SLF4J binding depends on this
   - `spi/AbstractLoggerAdapter.java` — commons-logging extends this
   - `spi/LoggerContext.java` — context management
   - `spi/Provider.java` — ServiceLoader discovery

3. **Check consuming libraries**: Verify method signatures against what these libraries actually call:
   - commons-logging 1.3.x: `Log4jApiLogFactory` → `StackLocatorUtil.getCallerClass(Class<?>)` → `AbstractLoggerAdapter.getContext(Class<?>)`
   - SLF4J binding: `ExtendedLogger`, `LoggerContext`, `StatusLogger`
   - Spring Boot: `LogManager`, `LoggerContextFactory.shutdown()`

4. **Report findings**: List each missing method with its signature, which library needs it, and what error would occur at runtime (typically `NoSuchMethodError` or `IncompatibleClassChangeError`).

5. **Implement fixes**: Add the missing methods, build with `mvn clean compile`, and run the demo to verify no regressions.

Reference: See memory file `compatibility-audit.md` for the known method signature inventory.
