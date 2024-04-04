package com.sparking.entities.payloadReq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateNewSlotPayload {
    private int fieldId;
    private int slotIdField;
}
