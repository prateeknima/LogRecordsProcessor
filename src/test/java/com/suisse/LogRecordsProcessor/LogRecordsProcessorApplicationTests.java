package com.suisse.LogRecordsProcessor;

import com.suisse.LogRecordsProcessor.model.LogsData;
import com.suisse.LogRecordsProcessor.repository.LogsRepository;
import com.suisse.LogRecordsProcessor.service.LogParserServiceImpl;
import nl.altindag.log.LogCaptor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ComponentScan("com/suisse/LogRecordsProcessor/repository")
@ComponentScan("com/suisse/LogRecordsProcessor/service")
@SpringBootTest
class LogRecordsProcessorApplicationTests {

    private final Logger logger = LogManager.getLogger(LogRecordsProcessorApplicationTests.class);
    @Autowired
    LogsRepository repository;
    @Autowired
    LogsData data;
    @Autowired
    LogRecordsProcessorApplication logRecordsProcessorApplication;
    @Autowired
    LogParserServiceImpl logParserService;
    @Test
    void testForFileNotFound() throws IOException {
        logger.info("Test case when no file is present at the given location");
        LogCaptor logCaptor = LogCaptor.forClass(LogParserServiceImpl.class);
        logParserService.flagLogEvents("/abc.txt");
        String expectedMessage = "No file found at the path specified";
        List<String> infoLogMessages = logCaptor.getErrorLogs();
        System.out.println(infoLogMessages.get(0));
        assertEquals(infoLogMessages.get(0),expectedMessage);
    }

    @Test
    void testForValidInputData() throws IOException {
        logger.info("Executing Test Case for valid input");
        HashMap<String, LogsData> expectedResult = new HashMap<>();
        int flag = 0;
        logger.debug("Creating a temporary file and providing it the input data");
        File tempFile = new File("", "tempFile.txt");
        populateTestCase(expectedResult);
        FileUtils.writeStringToFile(tempFile,
                "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\",\"timestamp\":1491377495212}" + System.lineSeparator() +
                        "{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\", \"timestamp\":1491377495213}" + System.lineSeparator() +
                        "{\"id\":\"scsmbstgrc\", \"state\":\"FINISHED\", \"timestamp\":1491377495218}" + System.lineSeparator() +
                        "{\"id\":\"scsmbstgra\", \"state\":\"FINISHED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\",\"timestamp\":1491377495216}" + System.lineSeparator() +
                        "{\"id\":\"scsmbstgrc\", \"state\":\"STARTED\", \"timestamp\":1491377495210}" + System.lineSeparator() +
                        "{\"id\":\"scsmbstgrb\", \"state\":\"FINISHED\", \"timestamp\":1491377495216}");
        LogRecordsProcessorApplication.main(new String[]{"/tempFile.txt"});
        Iterable<LogsData> d = repository.findAll();
        for (LogsData logsData : d) {
            String id = logsData.getId();
            data = expectedResult.get(id);
            if ((data.getAlert() != logsData.getAlert()) || (data.getDuration() != logsData.getDuration())) {
                logger.error("The actual and expected output does not match");
                flag = 1;
            }
        }
        assertEquals(flag, 0, "Test Case Failed");
    }


    private void populateTestCase(HashMap expectedResult) {
        logger.info("Populating the expected output");
        data = new LogsData();
        data.setDuration(4);
        data.setAlert("false");
        expectedResult.put("scsmbstgra", data);
        data = new LogsData();
        data.setDuration(3);
        data.setAlert("false");
        expectedResult.put("scsmbstgrb", data);
        data = new LogsData();
        data.setDuration(8);
        data.setAlert("true");
        expectedResult.put("scsmbstgrc", data);
    }

    @AfterEach
    private void afterMessage(){
        logger.info("Test case executed");
    }


}
