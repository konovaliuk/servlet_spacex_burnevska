package me.braun.spacex.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Status {
    @Setter(AccessLevel.NONE)
    private Short id;
    private String status;


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
