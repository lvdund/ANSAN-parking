package com.sparking.entities.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "field")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Field  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "image")
    private String image;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "openstatus", nullable = false)
    private String openstatus;

    @Column(name = "space", nullable = false)
    private BigDecimal space;

    @Column(name = "details", nullable = false, columnDefinition = "longtext")
    private String details;

    @Column(name = "id_area")
    private Integer idArea;
}
