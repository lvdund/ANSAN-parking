package com.sparking.entities.payloadReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ManLoginPayload {
        private String token;
        private int id;
        private List<Integer> listFields; // fields owned by Manager
}
