package com.sparking.repository;

import com.sparking.entities.data.BlackList;

import java.util.List;

public interface BlackListRepo {

    BlackList create(String token);

    List<BlackList> findByToken(String token);

}
