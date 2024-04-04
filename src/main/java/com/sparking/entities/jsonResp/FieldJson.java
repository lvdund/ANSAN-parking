package com.sparking.entities.jsonResp;

import com.sparking.entities.data.Area;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FieldJson {

    private int id;
    private String name;
    private String latitude;
    private String longitude;
    private String address;
    private String image;
    private Double price;
    private String openstatus;
    private BigDecimal space;
    private String details;
    private int totalBook;
    private int totalSlot;
    private int busySlot;
    private AreaJson area;
}
