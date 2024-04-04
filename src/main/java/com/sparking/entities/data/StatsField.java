package com.sparking.entities.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "stats_field")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "field_id", nullable = false)
    private Integer fieldId;

    @Column(name = "day", nullable = false)
    private Timestamp day;

    @Column(name = "freq", nullable = true)
    private Integer freq;

    @Column(name = "cost", nullable = true)
    private Integer cost;
}
