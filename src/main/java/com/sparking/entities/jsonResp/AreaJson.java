package com.sparking.entities.jsonResp;

import com.sparking.entities.data.District;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AreaJson {
    private int id;
    private String areaName;
    private District district;
}
