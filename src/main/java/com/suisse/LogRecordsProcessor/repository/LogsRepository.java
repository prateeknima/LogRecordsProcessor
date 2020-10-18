package com.suisse.LogRecordsProcessor.repository;

import com.suisse.LogRecordsProcessor.model.LogsData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepository extends CrudRepository<LogsData, String> {

}
