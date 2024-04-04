package com.sparking.entities.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "data_cam_and_detector")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataCamAndDetector {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "slot_id", nullable = false)
    private Integer slotId;

    @Column(name = "status_detector")
    private Boolean statusDetector;

    @Column(name = "status_cam")
    private Boolean statusCam;

    @Column(name = "time", nullable = false)
    private Timestamp time;

}
