package utility;

import api.ValidRequests;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class LogTestsExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final Logger logger = LogManager.getLogger(ValidRequests.class.getName());
    File consoleLog = new File("log", "myApp.log");
    String startMessage, finishMessage;
    String logTest;
    Boolean fromTest = false;

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Method testMethod = context.getRequiredTestMethod();
        startMessage = testMethod.getName() + " start";
        logger.info(startMessage);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws IOException {
        Method testMethod = context.getRequiredTestMethod();
        finishMessage = testMethod.getName() + " finish ";
        logger.info(finishMessage);
        logTest = "<html> <body> ";
        FileUtils.readLines(consoleLog, StandardCharsets.UTF_8).forEach(s -> {
            if (s.contains(startMessage)) { fromTest = true; }
            if (s.contains(finishMessage)) { fromTest = false; }
            if (fromTest) {logTest = logTest + s + "  <br />";}
        });
        logTest = logTest + finishMessage + "  </body> </html>";
        attachment(logTest);

    }

    @Attachment(value = "Test log.html", type = "text/html")
    public String attachment(String logTest) {
        return logTest;
    }

}
