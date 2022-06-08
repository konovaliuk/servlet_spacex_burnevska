package me.braun.spacex.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

//import org.springframework.security.core.GrantedAuthority;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Role implements Serializable {

    private byte id;
    @Setter
    private String role;
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}



