package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "systemConfigurations")
public class Config {
    @Id
    private String configName;
    private String configValue;
}
