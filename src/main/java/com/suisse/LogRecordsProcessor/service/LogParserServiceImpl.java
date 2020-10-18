package com.suisse.LogRecordsProcessor.service;

import com.suisse.LogRecordsProcessor.model.LogsData;
import com.suisse.LogRecordsProcessor.repository.LogsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LogParserServiceImpl implements LogParserService {
    @Autowired
    LogsRepository logsRepository;
    Scanner sc;
    ConcurrentHashMap<String, LogsData> mapData;
    private final Logger logger = LogManager.getLogger(LogParserServiceImpl.class);

    public LogParserServiceImpl() {
        logger.info("Initializing concurrent hashmap to enable thread safety");
        mapData = new ConcurrentHashMap<>();
    }

    /*
     * Usage: Flags log events longer than 4ms
     *
     * @param filePath - Indicates the path to the log file to be processed
     *
     * @return void
     * @author Prateek Nima
     *  */
    @Override
    public void flagLogEvents(String filePath) {
        logger.info("Method flagLogEvents successfully called in the Service Layer");
        logger.debug("Loading the log file using the file path");
        File file = new File(filePath);
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            logger.error("No file found at the path specified");
            e.printStackTrace();
        }
        logger.info("Setting up thread size for executor service. In case of database pooling set the thread size to the number of database pool size");
        int numOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        logger.debug("Executing the executor job");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (sc.hasNextLine()) {
                    String[] strData = sc.nextLine().split(",");
                    LogsData currentLog = new LogsData();
                    String id = strData[0].split("\"")[3];
                    if (!mapData.containsKey(id)) {
                        if (strData.length > 3) {
                            currentLog.setType(strData[2].split(":")[1]);
                            currentLog.setHost(strData[3].split(":")[1]);
                            String time = strData[4].split(":")[1];
                            currentLog.setTimestamp(Long.parseLong(time.substring(0, time.length() - 1)));
                        } else {
                            String time = strData[2].split(":")[1];
                            currentLog.setTimestamp(Long.parseLong(time.substring(0, time.length() - 1)));
                        }
                        mapData.put(id, currentLog);
                    } else {
                        LogsData storedLogs = mapData.get(id);
                        long difference = 0;
                        String alert = "false";
                        if (strData.length > 3) {
                            String time = strData[4].split(":")[1];
                            difference = storedLogs.getTimestamp() - Long.parseLong(time.substring(0, time.length() - 1));
                        } else {
                            String time = strData[2].split(":")[1];
                            difference = storedLogs.getTimestamp() - Long.parseLong(time.substring(0, time.length() - 1));
                        }
                        if (difference < 0)
                            difference *= -1;
                        if (difference > 4)
                            alert = "true";
                        logger.debug("Storing the data to HSQLDB database utilizing JPA");
                        LogsData d = new LogsData(id, difference, storedLogs.getType(), storedLogs.getHost(), alert);
                        logsRepository.save(d);
                    }
                }
            }
        });
        logger.debug("Shutting down the executor service");
        executor.shutdown();
        logger.info("The data has been rendered and populated to the database. You can now exit the application");
    }
}
