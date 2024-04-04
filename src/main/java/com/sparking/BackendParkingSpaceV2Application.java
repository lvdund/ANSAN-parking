package com.sparking;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.sparking.entities.data.*;
import com.sparking.entities.jsonResp.FieldAnalysis;
import com.sparking.getData.GetDataDetector;
import com.sparking.getData.TagModule;
import com.sparking.helpers.HandleSlotID;
import com.sparking.repository.*;
import com.sparking.service.FieldService;
import com.sparking.service_impl.GoogleService;
import com.sparking.tune.AnalysisFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.hibernate.internal.CoreLogging.logger;


@SpringBootApplication
@EnableSwagger2
public class BackendParkingSpaceV2Application implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(BackendParkingSpaceV2Application.class);

    final static long rateUpdateDataCam = 300 * 1000;

    @Autowired
    SlotRepo slotRepo;

    @Autowired
    FieldRepo fieldRepo;

    @Autowired
    ContractRepo contractRepo;

    @Autowired
    GoogleService googleService;

    @Autowired
    DataCamAndDetectorRepo dataCamAndDetectorRepo;

    @Autowired
    FieldService fieldService;

    @Autowired
    StatsFieldRepo statsFieldRepoRepo;


    List<String> rows = new ArrayList<>();

    List<Slot> slots = new ArrayList<>();


    public static void main(String[] args) {
        SpringApplication.run(BackendParkingSpaceV2Application.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.ant("/**")).build();
    }

    @Value("${pathCamStatus}")
    String pathDataCam;
    @Value("${pathDetectorStatus}")
    String pathDetectorStatus;
    @Value("${timeExpiredContract}")
    String timeExpiredContract;

    @Autowired
    com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        logger.info("SERVER STARTED");

       // System.out.println("******************** Start server ********************");
//        set timezone cho backend
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
//        set timezone cho controller
        objectMapper.setTimeZone(TimeZone.getDefault());

        //TODO
        // using Thread or sthg else !!!!!!!

//        logger.info("DELETE EXPIRED CONTRACT");
     //   update();
//        GetDataDetector.main(args);

//        new Thread("DETECTOR"){
//            @Override
//            public void run() {
//                logger.info("START TAG DETECTOR");
//                try {
//                    GetDataDetector.main(args);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();


        new Thread("TAG"){
            @Override
            public void run() {
                logger.info("START TAG MODULE");
                try {
                    TagModule.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();




        logger.info("UPDATE STATS FIELD");
        AnalysisFunction.updateStatsField();

        logger.info("UPDATE DATABASE SLOT USING DATA FROM CAM");
        getDataCam();

      //  logger.info("UPDATE STATS FIELD FREQ");
      //  updateStatsFieldFreqTime();
    }

//    public void update() throws FileNotFoundException, InterruptedException, UnsupportedEncodingException {
//        while (true){
//            deleteExpiredContract();
//            Thread.sleep(5000);
//        }
//    }

    @Scheduled(fixedRate = rateUpdateDataCam, initialDelay = 60000)
    public boolean getDataCam() throws FileNotFoundException, ParseException {
        logger.info("UPDATE DATA CAM COMPLETE");
        File file = new File (pathDataCam);
        if (!file.exists()) {
            return false;
        }

        Scanner myReader = new Scanner(file);
        List<String> newRows = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String row = myReader.nextLine();
            newRows.add(row);
        }
        if(!rows.equals(newRows)) {
            System.out.println("Data cam has changed");
            rows.clear();
            rows.addAll(newRows);

//            System.out.println("Rows Size " + rows.size());
            for (int i = 1; i < rows.size(); i++){
                String rowChild = rows.get(i);
                boolean status = rowChild.split(" ")[2].equals("1");

                int fieldId = Integer.parseInt(rowChild.split(" ")[1]);

                int slotID = HandleSlotID.handleSlotId(fieldId, Integer.parseInt(rowChild.split(" ")[0]));
                Slot oldSlot = slotRepo.findAll().stream()
                        .filter(slot -> slot.getId() == slotID)
                        .collect(Collectors.toList())
                        .get(0);
//                System.out.println("Debug - " + oldSlot);
                if (oldSlot != null) {
                    oldSlot.setStatusCam(status);
                    if (rowChild.split(" ").length == 4) {
                        String carNumber = rowChild.split(" ")[3];
                        oldSlot.setCarNumber(carNumber);
                    }
                }

                slotRepo.updateSlotDataCam(oldSlot);

                //            dataCamAndDetector
//                DataCamAndDetector dataCamAndDetector = dataCamAndDetectorRepo.findAll().stream()
//                        .filter(dataCam -> dataCam.getSlotId() == slotID)
//                        .collect(Collectors.toList())
//                        .get(0);
//                if (dataCamAndDetector != null) {
//                    dataCamAndDetector.setStatusCam(status);
//                    dataCamAndDetector.setTime(GetTime.getTime(rows.get(0)));
//                    dataCamAndDetectorRepo.createAndUpdate(dataCamAndDetector);
//                } else {
//                    dataCamAndDetectorRepo.createAndUpdate(DataCamAndDetector.builder()
//                            .statusCam(status)
//                            .slotId(slotID)
//                            .time(GetTime.getTime(rows.get(0)))
//                            .build());
//                }
            }
            System.out.println("Data cam has updated successfully");
        }

        myReader.close();
        return true;
    }

    public void writeDataDetector() throws  FileNotFoundException, UnsupportedEncodingException {
        List<Slot> newSlots = slotRepo.findAll();
        if(!slots.equals(newSlots)){
            PrintWriter writer = new PrintWriter(pathDetectorStatus, "UTF-8");
            for (Slot slot : newSlots){
                writer.println(slot.getId() + " " + (slot.getStatusDetector() == null ? "2" : slot.getStatusDetector() ? "1": "0"));
            }
            writer.close();
            slots.clear();
            slots.addAll(newSlots);
        }
    }

//    void deleteExpiredContract() {
//        for(Contract contract: contractRepo.findAll()){
//
//            if (contract.getTimeInBook()!=null) {
//                if (new Timestamp(new Date().getTime()).getTime() - contract.getTimeInBook().getTime() >= Integer.parseInt(timeExpiredContract)
//                        && contract.getTimeCarIn() == null) {
//                    contractRepo.delete(contract.getId());
//                }
//            }
//        }
//    }

    // auto update statistic field
    void updateStatsField() throws ParseException {
        List<StatsField> s = statsFieldRepoRepo.getLatest();
       // logger.info("Test null " + s.size());
        if (s.size()==0){
            logger.info("RUNNING IN UPDATE STATS FIELD");
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
                logger.info("Working with field " +  f.getId());
                List<FieldAnalysis> fieldAnalyses = fieldService.analysisByHour(f.getId(), sinceDateOnly, untilDateOnly, "day");

                for(FieldAnalysis fa: fieldAnalyses) {
                   // logger.info("fieldAnalyses " + fa.getFreq());
                    if(fa.getFreq()!=0){
                        statsFieldRepoRepo.createAndUpdate(StatsField.builder().
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

    void updateStatsFieldFreq() throws ParseException {
        List<StatsField> s = statsFieldRepoRepo.getLatest();
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
            logger.info("Since in updateStatsFieldFreq " + since);
            long millisInDay = 60 * 60 * 24 * 1000;
            //long currentTime = new Date().getTime();
            long sinceDateOnly = (since.getTime() / millisInDay) * millisInDay;
            long untilDateOnly = (until.getTime() / millisInDay) * millisInDay;
            //  Date clearDate = new Date(dateOnly);

            for (Field f : fields) {
                List<FieldAnalysis> fieldAnalyses = fieldService.analysis(f.getId(), since.getTime(), until.getTime(), "day");
                for(FieldAnalysis fa: fieldAnalyses) {
                    if(fa.getFreq()!=0){
                        statsFieldRepoRepo.createAndUpdate(StatsField.builder().
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

    public void updateStatsFieldFreqTime() throws FileNotFoundException, InterruptedException, UnsupportedEncodingException, ParseException {
        while (true){
            updateStatsFieldFreq();
            long millisInDay = 60 * 60 * 24 * 1000;
            Thread.sleep(millisInDay);
        }
    }

}
