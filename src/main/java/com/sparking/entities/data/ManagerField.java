package com.sparking.entities.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "manager_field")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "field_id", nullable = false)
    private Integer fieldId;

    @Column(name = "manager_id", nullable = false)
    private Integer managerId;

    @Column(name = "last_time_setup", nullable = false)
    private Timestamp lastTimeSetup;

}
