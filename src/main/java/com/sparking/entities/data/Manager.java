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
@Data
@Table(name = "manager")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "pass", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pass;

    @Column(name = "last_time_access")
    private Timestamp lastTimeAccess;

    @Column(name = "acp", nullable = true)
    private Boolean acp;

    @Column(name = "address", nullable = true)
    private String address ;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "sex", nullable = true)
    private String sex;

    @Column(name = "birth", nullable = true)
    private Date birth;

    @Column(name = "id_number", nullable = true)
    private int idNumber;

}
