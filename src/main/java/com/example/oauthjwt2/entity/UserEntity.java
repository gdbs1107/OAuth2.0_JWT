package com.example.oauthjwt2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_entity")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String name;
    private String role;
    private String email;
    private String password;


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
