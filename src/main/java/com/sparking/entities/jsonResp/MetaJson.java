package com.sparking.entities.jsonResp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaJson {
    private List<FieldJson> listOfFields;
    private int totalCarBooking;
    private int totalCarSlot;
    private int totalBusySlot;
    private int totalEmptySlot;
}
