package com.suisse.LogRecordsProcessor.dao;

import com.suisse.LogRecordsProcessor.service.LogParserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogParserDAO {
    @Autowired
    LogParserService logParserService;
    private final Logger logger = LogManager.getLogger(LogParserDAO.class);

    /*
     * Usage: Passes the data to the service layer without any processing
     *
     * @param filePath - Indicates the path to the log file to be processed
     *
     * @return void
     * @author Prateek Nima
     *  */
    public void processLogEvents(String filePath) {
        logger.info("Method processLogEvents Successfully Called in the DAO Layer");
        logParserService.flagLogEvents(filePath);
    }

}
