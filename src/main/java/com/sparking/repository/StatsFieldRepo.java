package com.sparking.repository;

import com.sparking.entities.data.Contract;
import com.sparking.entities.data.StatsField;
import com.sparking.entities.data.User;
import com.sparking.entities.payloadReq.*;

import java.util.List;
import java.util.stream.DoubleStream;

public interface StatsFieldRepo {

    StatsField createAndUpdate(StatsField statsField);


    List<StatsField> getLatest();

    List<StatsField> findByTime(long since, long until);

    List<StatsField> findByFiledTime(long since, long until, int fieldId);
}
