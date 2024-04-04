package com.sparking.tune;

import com.sparking.controller.ContractController;
import com.sparking.controller.FieldController;
import com.sparking.entities.data.Contract;
import com.sparking.repository.ContractRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Date;

@Component

@Configuration
@EnableScheduling


public class ExpiredContract {
    private static Logger logger = LoggerFactory.getLogger(ExpiredContract.class);


    private static String timeExpiredContract="1800000"; // del contract if users don't parked after the timeExpiredContract
    final static long rateDelExpiredContract=1800000;  // fixedRate run method

    @Autowired
    ContractRepo contractRepo_nonstatic;

    private static ContractRepo contractRepo;

    @PostConstruct
    private void initStatic() {
        contractRepo = this.contractRepo_nonstatic;
    }
    String pathDetectorStatus;

    @Scheduled(fixedRate = rateDelExpiredContract)
    public static void deleteExpiredContract() {
        logger.info("DELETE EXPIRED CONTRACT PERIODICALLY");
        for(Contract contract: contractRepo.findAll()){
            if (contract.getTimeInBook()!=null) {
                if (new Timestamp(new Date().getTime()).getTime() - contract.getTimeInBook().getTime() >= Integer.parseInt(timeExpiredContract)
                        && contract.getTimeCarIn() == null) {
                    contractRepo.delete(contract.getId());
                }
            }
        }
    }
}
