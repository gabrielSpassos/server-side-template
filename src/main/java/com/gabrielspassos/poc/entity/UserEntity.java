package com.gabrielspassos.poc.entity;

import com.gabrielspassos.poc.enumerator.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USER")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Column(name = "CREATION_DATETIME")
    private LocalDateTime creationDateTime;

    @Column(name = "UPDATE_DATETIME")
    private LocalDateTime updateDateTime;

}
