package com.sparking.tune;

import com.sparking.controller.FieldController;
import com.sparking.entities.data.Contract;
import com.sparking.entities.data.Field;
import com.sparking.entities.data.StatsField;
import com.sparking.entities.jsonResp.FieldAnalysis;
import com.sparking.repository.ContractRepo;
import com.sparking.repository.FieldRepo;
import com.sparking.repository.StatsFieldRepo;
import com.sparking.service.FieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Component

@Configuration
@EnableScheduling

public class AnalysisFunction {

    private static Logger logger = LoggerFactory.getLogger(AnalysisFunction.class);
    //final static long rateUpdateAnalysis=60 * 60 * 24 * 1000;  // fixedRate run method
    final static long rateUpdateAnalysis=120 * 1000;

    @Autowired
    StatsFieldRepo statsFieldRepo_nonstatic;
    @Autowired
    ContractRepo contractRepo_nonstatic;
    @Autowired
    FieldRepo fieldRepo_nonstatic;
    @Autowired
    FieldService fieldService_nonstatic;

    private static StatsFieldRepo statsFieldRepo;
    private static ContractRepo contractRepo;
    private static FieldRepo fieldRepo;
    private static FieldService fieldService;

    @PostConstruct
    private void initStatic() {
        statsFieldRepo = this.statsFieldRepo_nonstatic;
        contractRepo = this.contractRepo_nonstatic;
        fieldRepo = this.fieldRepo_nonstatic;
        fieldService = this.fieldService_nonstatic;
    }


    // auto update statistic field
   public static void updateStatsField() throws ParseException {
        logger.info("UPDATE ANALYSIS DATABASE: FIRST TIME");
        List<StatsField> s = statsFieldRepo.getLatest();
        // logger.info("Test null " + s.size());
        if (s.size()==0){
           // logger.info("RUNNING IN UPDATE STATS FIELD");
            List<Contract> contracts=contractRepo.findAll();
            List<Field> fields = fieldRepo.findAll();
            Timestamp until = new Timestamp(new Date().getTime());
            Timestamp since = until;


            for(Contract contract: contracts){
                if (contract.getTimeInBook()!=null) {
                    if (contract.getTimeInBook().before(since))
                        since = contract.getTimeInBook();
                }

            }
            long millisInDay = 60 * 60 * 24 * 1000;
            //long currentTime = new Date().getTime();
            long sinceDateOnly = (since.getTime() / millisInDay) * millisInDay;
            long untilDateOnly = (until.getTime() / millisInDay) * millisInDay;
            //  Date clearDate = new Date(dateOnly);
            // logger.info("since " + sinceDateOnly );
            // logger.info("until " + untilDateOnly );

            for (Field f : fields) {
               // logger.info("Working with field " +  f.getId());
                List<FieldAnalysis> fieldAnalyses = fieldService.analysisByHour(f.getId(), sinceDateOnly, untilDateOnly, "day");

                for(FieldAnalysis fa: fieldAnalyses) {
                    // logger.info("fieldAnalyses " + fa.getFreq());
                    if(fa.getFreq()!=0){
                        statsFieldRepo.createAndUpdate(StatsField.builder().
                                id(-1).
                                fieldId(f.getId()).
                                day(new Timestamp(fa.getTime())).
                                freq(fa.getFreq()).
                                cost(fa.getCost()).
                                build());
                    }
                }
            }
        }

    }

 //   @Scheduled(fixedRate = rateUpdateAnalysis,initialDelay = 120000)
    @Scheduled(fixedRate = rateUpdateAnalysis,initialDelay = 30000)
    void updateStatsFieldFreq() throws ParseException {
        logger.info("UPDATE ANALYSIS DATABASE: PERIODICALLY");
        List<StatsField> s = statsFieldRepo.getLatest();
        if (s.size()!=0){
            //   logger.info("Size of s" + s.size());
            List<Field> fields = fieldRepo.findAll();
            Timestamp until = new Timestamp(new Date().getTime());
            Timestamp since = new Timestamp(0);

            for(StatsField sf: s){
                if (sf.getDay()!=null) {
                    if (sf.getDay().after(since))
                        since = sf.getDay();
                }

            }
            //logger.info("Since in updateStatsFieldFreq " + since);
            long millisInDay = 60 * 60 * 24 * 1000;
            //long currentTime = new Date().getTime();
            long sinceDateOnly = (since.getTime() / millisInDay) * millisInDay;
            long untilDateOnly = (until.getTime() / millisInDay) * millisInDay;
            //  Date clearDate = new Date(dateOnly);

            for (Field f : fields) {
                List<FieldAnalysis> fieldAnalyses = fieldService.analysis(f.getId(), since.getTime(), until.getTime(), "day");
                for(FieldAnalysis fa: fieldAnalyses) {
                    if(fa.getFreq()!=0){
                        statsFieldRepo.createAndUpdate(StatsField.builder().
                                id(-1).
                                fieldId(f.getId()).
                                day(new Timestamp(fa.getTime())).
                                freq(fa.getFreq()).
                                cost(fa.getCost()).
                                build());

                    }

                }
            }
        }

    }
}
