package com.projectfive.runner;

import com.beust.jcommander.JCommander;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mockit.Deencapsulation.getField;
import static mockit.Deencapsulation.invoke;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Test(groups = "unit")
public class TestRunnerTest {

    private final String GET_TESTS = "getTests";
    private final String BUILD_ERROR_MESSAGE = "buildErrorMessage";
    private final String REPORT_FAILURES = "reportFailures";
    private final String TEST_PACKAGE = "TEST_PACKAGE";

    @Tested TestRunner tested;
    @Mocked CommandLineParams params;
    @Mocked JCommander jc;
    @Mocked TestNG testNG;
    @Mocked TestReporter tr;
    @Mocked ClassPath classPath;
    @Mocked TestListenerAdapter tla;
    @Mocked StringBuilder builder;
    @Mocked ITestResult testResult;

    @Test
    public void testMainSuccess() throws Exception {
        new Expectations(TestRunner.class) {{
            invoke(TestRunner.class, GET_TESTS);
            testNG.hasFailure(); result = false;
        }};
        TestRunner.main(new String[0]);
    }

    @Test
    public void testMainFailure() throws Exception {
        new Expectations(TestRunner.class) {{
            invoke(TestRunner.class, GET_TESTS);
            testNG.hasFailure(); result = true;
            invoke(TestRunner.class, REPORT_FAILURES, new Class<?>[] {TestListenerAdapter.class}, any);
        }};
        TestRunner.main(new String[0]);
    }

    @Test
    public void testGetTestsEmpty() throws Exception {
        new Expectations() {{
            classPath.getTopLevelClassesRecursive(
                    getField(TestRunner.class, TEST_PACKAGE)); result = ImmutableSet.of();
        }};
        final Class[] result = invoke(TestRunner.class, GET_TESTS);
        assertThat(result).isEmpty();
    }

    @Test
    public void testGetTestsNoTests(@Mocked ClassPath.ClassInfo classInfo) throws Exception {
        new Expectations() {{
            classPath.getTopLevelClassesRecursive(
                    getField(TestRunner.class, TEST_PACKAGE)); result = ImmutableSet.of(classInfo);
            classInfo.getName(); result = "Unknown";
        }};
        final Class[] result = invoke(TestRunner.class, GET_TESTS);
        assertThat(result).isEmpty();
    }

    @Test(expectedExceptions = ClassNotFoundException.class)
    public void testGetTests(@Mocked ClassPath.ClassInfo classInfo) throws Exception {
        new Expectations() {{
            classPath.getTopLevelClassesRecursive(
                    getField(TestRunner.class, TEST_PACKAGE)); result = ImmutableSet.of(classInfo);
            classInfo.getName(); result = getField(TestRunner.class, "TEST_CLASS_ENDING");
        }};
        final Class[] result = invoke(TestRunner.class, GET_TESTS);
        assertThat(result).hasSize(1);
    }

    @Test(expectedExceptions = Exception.class)
    public void testReportFailuresInvalid() throws Exception {
        new Expectations() {{
            tla.getFailedTests(); result = Collections.EMPTY_LIST;
            tla.getConfigurationFailures(); result = Collections.EMPTY_LIST;
        }};
        invoke(TestRunner.class, REPORT_FAILURES, tla);
    }

    @Test(expectedExceptions = Exception.class)
    public void testReportFailuresTestFailures() throws Exception {
        final String msg = "msg";
        new Expectations(tested) {{
            tla.getFailedTests(); result = new ArrayList<ITestResult>() {{ add(testResult); }};
            invoke(tested.getClass(), BUILD_ERROR_MESSAGE,
                    new Class<?>[]{List.class}, (List<ITestResult>) any); result = msg;
            tla.getConfigurationFailures(); result = Collections.EMPTY_LIST;
        }};
        invoke(TestRunner.class, REPORT_FAILURES, tla);
        new Verifications() {{
            builder.append("Failed Tests:\n");
            builder.append(msg);
        }};
    }

    @Test(expectedExceptions = Exception.class)
    public void testReportFailuresConfigFailures() throws Exception {
        final String msg = "msg";
        new Expectations(tested) {{
            tla.getFailedTests(); result = Collections.EMPTY_LIST;
            tla.getConfigurationFailures(); result = new ArrayList<ITestResult>() {{ add(testResult); }};
            invoke(tested.getClass(), BUILD_ERROR_MESSAGE,
                    new Class<?>[]{List.class}, (List<ITestResult>) any); result = msg;
        }};
        invoke(TestRunner.class, REPORT_FAILURES, tla);
        new Verifications() {{
            builder.append("Configuration Failures:\n");
            builder.append(msg);
        }};
    }

    @Test
    public void testBuildErrorMessageNoFailures() throws Exception {
        final String result = invoke(TestRunner.class, BUILD_ERROR_MESSAGE, Collections.EMPTY_LIST);
        assertThat(result).isEmpty();
    }

    @Test
    public void testBuildErrorMessageFailures(@Mocked Throwable throwable) throws Exception {
        final String name = "name";
        final String message = "message";
        new Expectations() {{
            testResult.getMethod().getMethodName(); result = name;
            testResult.getThrowable(); returns(throwable);
            throwable.getMessage(); result = message;
        }};
        final String result = invoke(TestRunner.class, BUILD_ERROR_MESSAGE,
                new ArrayList<ITestResult>(){{ add(testResult); }});
        assertThat(result).isEqualTo("\t" + name + ": " + message + "\n");
    }
}