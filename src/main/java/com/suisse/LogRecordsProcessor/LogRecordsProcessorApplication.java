package com.suisse.LogRecordsProcessor;

import com.suisse.LogRecordsProcessor.dao.LogParserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class LogRecordsProcessorApplication {
    private static final Logger logger = LogManager.getLogger(LogRecordsProcessorApplication.class);
    @Autowired
    LogParserDAO logParserDAO;
    @Autowired
    LogRecordsProcessorApplication recordProcessor;

    public static void main(String[] args) {

        SpringApplication.run(LogRecordsProcessorApplication.class, args);
        ApplicationContext context = new AnnotationConfigApplicationContext(LogRecordsProcessorApplication.class);
        logger.info("Application successfully started on port 8081");
        context.getBean(LogRecordsProcessorApplication.class).parseData(args[0]);
    }

    public void parseData(String filePath) {
        logParserDAO.processLogEvents(filePath);
    }


}
