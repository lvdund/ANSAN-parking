package com.sparking.repository;

import com.sparking.entities.data.Gateway;
import com.sparking.entities.data.Manager;

import java.util.List;

public interface GatewayRepo {

    Gateway createAndUpdate(Gateway gateway);

    boolean delete(int id);

    List<Gateway> findAll();

    Gateway findById(int id);

    List<Gateway> managerFind(Manager manager);

    Gateway managerUpdate(Gateway gateway, Manager manager);

    boolean managerDelete(int id, Manager manager);
}
