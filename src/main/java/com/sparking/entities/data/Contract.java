package com.sparking.entities.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "contract")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "field_id", nullable = false)
    private Integer fieldId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "time_car_in", nullable = true)
    private Timestamp timeCarIn;

    @Column(name = "time_car_out", nullable = true)
    private Timestamp timeCarOut;

    @Column(name = "time_in_book")
    private Timestamp timeInBook;

    @Column(name = "time_out_book")
    private Timestamp timeOutBook;

    @Column(name = "car_number", nullable = false)
    private String carNumber;

    @Column(name = "dt_create", nullable = false)
    private Timestamp dtCreate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "cost")
    private String cost;

}
