package com.sparking.entities.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tag_package")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id", nullable = false)
    private Integer newsId;

    @Column(name = "sign", nullable = false)
    private String sign;

    @Column(name = "seq", nullable = false)
    private String seq;

    @Column(name = "mty", nullable = false)
    private String mty;

    @Column(name = "tag_id", nullable = false)
    private String tagId;

    @Column(name = "lat", nullable = false)
    private String lat;

    @Column(name = "log", nullable = false)
    private String log;

    @Column(name = "tag_date", nullable = false)
    private String date;

    @Column(name = "tag_time", nullable = false)
    private String time;

    @Column(name = "state", nullable = false)
    private String state;
}
