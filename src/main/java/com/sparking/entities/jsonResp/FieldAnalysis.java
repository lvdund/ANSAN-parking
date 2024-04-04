package com.sparking.entities.jsonResp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FieldAnalysis {
    long time;
    int totalSlot;
    int freq;
    int cost;
}
