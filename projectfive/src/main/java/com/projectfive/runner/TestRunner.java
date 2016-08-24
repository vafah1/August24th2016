package com.projectfive.runner;

import com.beust.jcommander.JCommander;
import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.TestNGException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The entry point to the automation. The {@link TestRunner} handles bootstrapping TestNG and populating the
 * populating the {@link CommandLineParams}.
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Slf4j
public class TestRunner {

    private static final String TEST_PACKAGE = "com.projectfive.test";
    private static final String TEST_CLASS_ENDING = "Test";

    public static void main(String[] args) throws Exception {
        // Get the default state
        CommandLineParams params = CommandLineParams.get();

        // Override the default state with user supplied values
        new JCommander(params, args);

        // Bootstrap TestNG to allow building an executable JAR
        final TestNG testNG = new TestNG();
        final TestListenerAdapter adapter = new TestReporter();

        testNG.setTestClasses(getTests());
        testNG.addListener(adapter);
        testNG.run();

        // If testNG contains failures the Jar needs to throw an exception for Jenkins to fail the build
        if (testNG.hasFailure()) reportFailures(adapter);
    }

    /**
     * This function uses reflection to find the TestNG test classes stored within the {@link TestRunner#TEST_PACKAGE}
     * with the name ending with {@link TestRunner#TEST_CLASS_ENDING}.
     *
     * @return an array of TestNG test classes
     * @throws IOException if the attempt to read class path resources (jar files or directories) failed.
     * @throws ClassNotFoundException if the class to load was not found
     */
    private static Class[] getTests() throws IOException, ClassNotFoundException {
        final ClassLoader classLoader = TestRunner.class.getClassLoader();
        final ClassPath classPath = ClassPath.from(classLoader);
        final List<Class> testClasses = new ArrayList<>();
        for (ClassPath.ClassInfo info : classPath.getTopLevelClassesRecursive(TEST_PACKAGE)) {
            if (info.getName().endsWith(TEST_CLASS_ENDING)) {
                testClasses.add(classLoader.loadClass(info.getName()));
            }
        }
        return testClasses.toArray(new Class[testClasses.size()]);
    }

    /**
     * Builds a detailed {@link TestNGException} containing all failures
     *
     * @param tla the {@link TestListenerAdapter} returned after a TestNG run
     * @throws TestNGException once detailed message has been built
     */
    private static void reportFailures(TestListenerAdapter tla) throws TestNGException {
        final StringBuilder builder = new StringBuilder();

        final List<ITestResult> testFailures = tla.getFailedTests();
        if (testFailures.size() > 0) {
            builder.append("Failed Tests:\n");
            builder.append(buildErrorMessage(testFailures));
        }

        final List<ITestResult> configFailures = tla.getConfigurationFailures();
        if (configFailures.size() > 0) {
            builder.append("Configuration Failures:\n");
            builder.append(buildErrorMessage(configFailures));
        }

        throw new TestNGException(builder.toString());
    }

    /**
     * Creates a detailed message of the test failures
     *
     * @param failures the test failures from a TestNG run
     * @return the detailed test failure message
     */
    private static String buildErrorMessage(List<ITestResult> failures) {
        final StringBuilder builder = new StringBuilder();
        for (ITestResult tr : failures) {
            builder.append("\t")
                    .append(tr.getMethod().getMethodName())
                    .append(": ")
                    .append(tr.getThrowable().getMessage())
                    .append("\n");
        }
        return builder.toString();
    }
}
