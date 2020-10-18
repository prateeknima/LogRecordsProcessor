package com.suisse.LogRecordsProcessor.service;

/*
 * LogParserService Interface
 *
 * @author Prateek Nima
 *  */

public interface LogParserService {

    abstract void flagLogEvents(String filePath);
}
