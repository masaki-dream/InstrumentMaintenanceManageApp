package com.masaki.instrumentmaintenancemanageapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class InstrumentMaintenanceManageAppApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
        SpringApplication.run(InstrumentMaintenanceManageAppApplication.class, args);
    }
}
