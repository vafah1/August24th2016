package com.projectfive.runner;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * An adapter to TestNG to allow cleaner Jenkins logging.
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Slf4j
public class TestReporter extends TestListenerAdapter {

    private String currentInstance = null;
    private List<ITestResult> failedTests = new ArrayList<>();

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        super.onConfigurationFailure(itr);
        log.error("  fail: " + itr.getMethod().getMethodName() + "()");
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        super.onConfigurationSkip(itr);
        log.warn("  skip: " + itr.getMethod().getMethodName() + "()");
    }

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        if (!result.getInstanceName().equals(currentInstance)) {
            currentInstance = result.getInstanceName();
            log.info("start: " + currentInstance);
        }
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        log.info("  pass: " + tr.getMethod().getMethodName() + "(" + getParams(tr) + ")");
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        log.warn("  fail: " + tr.getMethod().getMethodName() + "(" + getParams(tr) + ")");
        logThrowable(tr.getThrowable());
        failedTests.add(tr);
    }

    private void logThrowable(Throwable throwable) {
        log.warn("    " + throwable.getMessage());
        final StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            log.warn("      " + element.toString());
        }
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        log.info("  skip: " + tr.getMethod().getMethodName() + "(" + getParams(tr) + ")");
    }

    public List<ITestResult> getFailedTests() {
        return this.failedTests;
    }

    private String getParams(ITestResult tr) {
        Object[] params = tr.getParameters();
        StringBuilder paramString = new StringBuilder("");
        for (int i = 0; i < params.length; i++) {
            paramString.append(params[i]);
            if (i < params.length - 1) {
                paramString.append(", ");
            }
        }
        return paramString.toString();
    }
}
