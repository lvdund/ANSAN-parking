package com.sparking.entities.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "pass", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "id_number", nullable = false)
    private Integer idNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "equipment", nullable = false)
    private String equipment;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "last_time_access")
    private Timestamp lastTimeAccess;

    @Column(name = "image")
    private String image;

    @Column(name = "sex", nullable = false)
    private String sex;

    @Column(name = "birth")
    private Date birth;

}
