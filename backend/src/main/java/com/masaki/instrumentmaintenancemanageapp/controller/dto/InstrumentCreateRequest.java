package com.masaki.instrumentmaintenancemanageapp.controller.dto;

public class InstrumentCreateRequest {
    private String name;
    private String maintenanceType;
    private String description;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
