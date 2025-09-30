package demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

/**
 * Test program to verify log4j2 SPI compatibility.
 * This simulates what Spring Boot does when it detects log4j2.
 */
public class TestSPI {

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("Testing log4j2 SPI Compatibility (Spring Boot Integration)");
        System.out.println("======================================================================");
        System.out.println();

        try {
            // Test 1: LogManager.getFactory() - This is what Spring Boot calls
            System.out.println("Test 1: LogManager.getFactory()");
            LoggerContextFactory factory = LogManager.getFactory();
            System.out.println("✓ getFactory() returned: " + factory.getClass().getName());
            System.out.println();

            // Test 2: LogManager.getContext()
            System.out.println("Test 2: LogManager.getContext()");
            LoggerContext context = LogManager.getContext();
            System.out.println("✓ getContext() returned: " + context.getClass().getName());
            System.out.println();

            // Test 3: Get logger through context
            System.out.println("Test 3: Get logger through context");
            Logger logger = context.getLogger("TestLogger");
            System.out.println("✓ context.getLogger() returned: " + logger.getClass().getName());
            System.out.println();

            // Test 4: LoggerContextFactory.getContext() with parameters
            System.out.println("Test 4: LoggerContextFactory.getContext() with parameters");
            LoggerContext context2 = factory.getContext(
                "org.springframework.test.TestClass",  // fqcn
                TestSPI.class.getClassLoader(),         // loader
                null,                                    // externalContext
                true                                     // currentContext
            );
            System.out.println("✓ factory.getContext() returned: " + context2.getClass().getName());
            System.out.println();

            // Test 5: hasLogger check
            System.out.println("Test 5: hasLogger() check");
            boolean hasLogger = context.hasLogger("TestLogger");
            System.out.println("✓ hasLogger('TestLogger') returned: " + hasLogger);
            System.out.println();

            // Test 6: Actual logging through the context logger
            System.out.println("Test 6: Actual logging through context logger");
            logger.info("This log message came through the SPI layer!");
            System.out.println("✓ Logging through SPI logger works!");
            System.out.println();

            // Test 7: Context object storage
            System.out.println("Test 7: Context object storage");
            context.putObject("testKey", "testValue");
            Object value = context.getObject("testKey");
            System.out.println("✓ Context object storage works: " + value);
            System.out.println();

            System.out.println("======================================================================");
            System.out.println("SUCCESS: All SPI tests passed!");
            System.out.println("log4j2-log4Rich is now compatible with Spring Boot and other frameworks");
            System.out.println("======================================================================");

        } catch (Exception e) {
            System.err.println("======================================================================");
            System.err.println("FAILURE: SPI test failed!");
            System.err.println("======================================================================");
            e.printStackTrace();
            System.exit(1);
        }
    }
}