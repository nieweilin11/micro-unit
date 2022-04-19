package microunit;

import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class for running unit tests without support for expected exceptions.
 */
public class BasicTestRunner extends TestRunner {

    /**
     * Creates a {@code BasicTestRunner} object for executing the test methods
     * of the class specified.
     *
     * @param testClass the class whose test methods will be executed
     */
    public BasicTestRunner(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public void invokeTestMethod(Method testMethod, Object instance, TestResultAccumulator results)
            throws IllegalAccessException {
        try {
            testMethod.invoke(instance);
            results.onSuccess(testMethod);
            Logger.trace("{} Success!", testMethod.getName());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            Logger.error(e, "Exceptions during fabrication {}");
            if (cause instanceof AssertionError) {
                Logger.trace("{} Failure!", testMethod.getName());
            } else {
                Logger.trace("{} Error!", testMethod.getName());
            }
        }
    }

    // CHECKSTYLE:OFF
    public static void main(String[] args) throws Exception {
        Class<?> testClass = Class.forName(args[0]);
        new BasicTestRunner(testClass).runTestMethods();
    }

}
