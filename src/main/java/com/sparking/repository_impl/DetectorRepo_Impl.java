package com.sparking.repository_impl;

import com.sparking.entities.data.Detector;
import com.sparking.entities.data.Gateway;
import com.sparking.entities.data.Manager;
import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.entities.payloadReq.DetectorPayload;
import com.sparking.entities.payloadReq.UpdateSlotIdPayload;
import com.sparking.repository.DetectorRepo;
import com.sparking.repository.GatewayRepo;
import com.sparking.service_impl.ContractService_Impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = Exception.class, timeout = 30000)
@Repository
public class DetectorRepo_Impl implements DetectorRepo {
    private static Logger logger = LoggerFactory.getLogger(DetectorRepo_Impl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    GatewayRepo gatewayRepo;

    @Override
    public Detector createDetector(DetectorPayload detectorPayload) {
        int detectorId = detectorPayload.getId();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<Detector> detectors = entityManager.createQuery("select d from Detector d where d.id =: id")
                .setParameter("id", detectorId).getResultList();
        if (detectors.size() != 0) {
//            logger.info(detectors.toString());
            return null;
        }
        Detector detector = Detector.builder()
                .id(detectorPayload.getId())
                .addressDetector(detectorPayload.getAddressDetector())
                .slotId(detectorPayload.getSlotId())
                .gatewayId(detectorPayload.getGatewayId())
                .lastTimeSetup(currentTime)
                .build();
        entityManager.merge(detector);
        return detector;
    }

    @Override
    public boolean deleteDetector(Integer id) {
        List<Detector> detectors = entityManager.createQuery("select d from Detector d where d.id =: id")
                .setParameter("id", id).getResultList();
        if (detectors.size() != 0) {
            Detector detector = detectors.get(0);
            entityManager.createQuery("delete from Detector d where d.id =: id").setParameter("id", detector.getId()).executeUpdate();
            return true;
        }
        return false;
    }

    @Override
    public Detector createAndUpdate(Detector detector) {
        return entityManager.merge(detector);
    }

    @Override
    public boolean delete(int id) {
        Detector detector = entityManager.find(Detector.class, id);
        if(detector != null){
            entityManager.remove(detector);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<Detector> findAll() {
        return entityManager.createQuery("select d from Detector d").getResultList();
    }

    @Override
    public List<Detector> findByGateway(String gateway) {
        return entityManager.createQuery("Select d from Detector d where d.gatewayId =: gwid")
                .setParameter("gwid", Integer.parseInt(gateway)).getResultList();
    }

    @Override
    public List<Detector> findBySlotId(int id) {
        return entityManager.createQuery("select d from Detector d where d.slotId = :id")
        .setParameter("id", id).getResultList();
    }

    @Override
    public List<Detector> managerFind(Manager manager) {
        List<Gateway> gateways = gatewayRepo.managerFind(manager);
        List<Detector> detectors = new ArrayList<>();
        for(Gateway gateway: gateways){
            detectors.addAll(entityManager.createQuery("select x from Detector x where x.gatewayId =:id")
                    .setParameter("id", gateway.getId()).getResultList());
        }
        return detectors;
    }

    @Override
    public List<Detector> managerGetByGateway(Manager manager, String gateway) {
        List<Gateway> gateways = gatewayRepo.managerFind(manager);
        List<Detector> detectors = new ArrayList<>();
        for(Gateway gw: gateways){
            detectors.addAll(entityManager.createQuery("select x from Detector x where x.gatewayId =:id and x.gatewayId =: gwid")
                    .setParameter("id", gw.getId())
                    .setParameter("gwid", Integer.parseInt(gateway))
                    .getResultList());
        }
        return detectors;
    }

    @Override
    public Detector managerCreateAndUpdate(Detector detector, Manager manager) {
        return check(detector, manager) ? entityManager.merge(detector) : null;
    }

    @Override
    public Detector updateSlotId(UpdateSlotIdPayload updateSlotIdPayload) {
        String addressDetector = updateSlotIdPayload.getAddressDetector();
        int gatewayId = updateSlotIdPayload.getGatewayId();
        int slotId = updateSlotIdPayload.getSlotId();
      //  logger.info(updateSlotIdPayload.toString());


        List<Detector> detectors = entityManager
                .createQuery("select d from Detector d where d.addressDetector =: add and d.gatewayId =: gwId")
                .setParameter("add", addressDetector).setParameter("gwId", gatewayId).getResultList();

        Detector detector = detectors.get(0);
        if (detector.getSlotId().equals(slotId)) {
            return null;
        }
        detector.setSlotId(slotId);
        entityManager.merge(detector);
        return detector;
    }

    @Override
    public boolean managerDelete(int id, Manager manager) {
        Detector detector = entityManager.find(Detector.class, id);
        if(detector == null){
            return false;
        }else if(check(detector, manager)){
            entityManager.remove(detector);
        }
        return false;
    }

    @Override
    public Detector findById(int id) {
        return entityManager.find(Detector.class, id);
    }

    @Override
    public Detector managerFindById(int id, Manager manager) {
        Detector detector = entityManager.find(Detector.class, id);
        return check(detector, manager) ? detector : null;
    }

    boolean check(Detector detector, Manager manager){
        List<Detector> detectors = managerFind(manager);
        for(Detector d: detectors){
            if(d.getId().equals(detector.getId())) return true;
        }
        return false;
    }
}
