package com.example.oauthjwt2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String name;
    private String role;
    private String email;


    public void setEmail(String email) {
        if (email != null) {
            this.email = email;
        }
        return;
    }

    public void setName(String name) {
        if (name!=null){
            this.name = name;
        }
        return;
    }
}
