package com.suisse.LogRecordsProcessor.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Component
public class LogsData {
    @Id
    @Getter
    private String id;
    @Getter
    @Setter
    private long duration;
    private String type;
    private String host;
    @Getter
    @Setter
    private String alert;
    private long timestamp;
    public LogsData(String id, long duration, String type, String host, String alert) {
        this.id = id;
        this.duration = duration;
        this.type = type;
        this.host = host;
        this.alert = alert;
    }
}
