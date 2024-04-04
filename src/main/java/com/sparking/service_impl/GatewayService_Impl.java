package com.sparking.service_impl;

import com.sparking.entities.data.Gateway;
import com.sparking.entities.data.Manager;
import com.sparking.entities.jsonResp.GatewayJson;
import com.sparking.repository.DetectorRepo;
import com.sparking.repository.GatewayRepo;
import com.sparking.repository.ManagerRepo;
import com.sparking.service.GatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GatewayService_Impl implements GatewayService {
    public static Logger logger = LoggerFactory.getLogger(GatewayService_Impl.class);

    @Autowired
    GatewayRepo gatewayRepo;

    @Autowired
    DetectorRepo detectorRepo;

    @Autowired
    ManagerRepo managerRepo;

    @Override
    public GatewayJson createAndUpdate(Gateway gateway) {
        return data2Json(gatewayRepo.createAndUpdate(gateway));
    }

    @Override
    public boolean delete(int id) {
        return gatewayRepo.delete(id);
    }

    @Override
    public List<GatewayJson> findAll() {

        return gatewayRepo.findAll().stream().map(gateway -> data2Json(gateway)).collect(Collectors.toList());
    }

    @Override
    public List<Gateway> managerFind(String phone) {
        // find GW using /mn/
        Manager manager = managerRepo.findByEmail(phone);

        if(manager == null) return null;
        return gatewayRepo.managerFind(manager);
    }

    @Override
    public Gateway managerUpdate(Gateway gateway, String phone) {

        Manager manager = managerRepo.findByEmail(phone);

        if(manager == null) return null;
        return gatewayRepo.managerUpdate(gateway, manager);
    }

    @Override
    public boolean managerDelete(int id, String phone) {
        Manager manager = managerRepo.findByEmail(phone);
        if(manager == null) return false;
        return gatewayRepo.managerDelete(id, manager);
    }

    public GatewayJson data2Json(Gateway gateway){
        return GatewayJson.builder()
            .id(gateway.getId())
            .address(gateway.getAddressGateway())
            .totalDetector(detectorRepo.findAll()
                    .stream()
                    .filter(detector -> detector.getGatewayId().equals(gateway.getId()))
                    .collect(Collectors.toList())
                    .size())
            .fieldId(gateway.getFieldId())
            .build();
    }
}
