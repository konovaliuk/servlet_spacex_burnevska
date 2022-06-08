package me.braun.spacex.entity;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor

public final class Mission {


    private Long id;
    private String title;
    private String description;
    private Long customer;
    private long spaceCraft;
    private short status;
    private byte serviceType;
    private Long curator;
    private double payloadWeigh;
    private Date date;
    private int duration;
    private Long missionPrice;
    private Long servicePrice;

    public Mission(String title, String description, Long customer,
                   long spaceCraft, short status, byte serviceType, Long curator,
                   double payloadWeigh, Date date, int duration) {
        this.title = title;
        this.description = description;
        this.customer = customer;
        this.spaceCraft = spaceCraft;
        this.status = status;
        this.serviceType = serviceType;
        this.curator = curator;
        this.payloadWeigh = payloadWeigh;
        this.date = date;
        this.duration = duration;
    }
    public Mission(Long id, String title, String description, Long customer,
                   long spaceCraft, short status, byte serviceType, Long curator,
                   double payloadWeigh, Date date, int duration) {
        this.title = title;
        this.description = description;
        this.customer = customer;
        this.spaceCraft = spaceCraft;
        this.status = status;
        this.serviceType = serviceType;
        this.curator = curator;
        this.payloadWeigh = payloadWeigh;
        this.date = date;
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
