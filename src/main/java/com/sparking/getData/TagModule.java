package com.sparking.getData;

import com.sparking.common.ConfigVar;
import com.sparking.entities.data.*;
import com.sparking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class TagModule {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ContractRepo contractRepo;
    @Autowired
    FieldRepo fieldRepo;
    @Autowired
    SlotRepo slotRepo;
    @Autowired
    TagRepo tagRepo;


    private static UserRepo userRepoStatic;
    private static ContractRepo contractRepoStatic;
    private static FieldRepo fieldRepoStatic;
    private static SlotRepo slotRepoStatic;
    private static TagRepo tagRepoStatic;

    @PostConstruct
    private void initStatic() {
        userRepoStatic = this.userRepo;
        contractRepoStatic = this.contractRepo;
        fieldRepoStatic = this.fieldRepo;
        slotRepoStatic = this.slotRepo;
        tagRepoStatic = this.tagRepo;
    }

    final static String SIGN = "@V2X,";
    static String seq;

    enum MTY {
        ACK, DATA
    }

    ;

    static String id;
    static String lat;
    static String lng;
    static String date;
    static String time;

    enum StateServer {
        OUT, IN, NO_REG, NO_LOC, ERR
    }

    ;

    public static void start() throws IOException {

        ServerSocket listener = null;

        System.out.println("[For TAG] Server is waiting to accept user...");
        int clientNumber = 0;

        // open ServerSocket (port)
        try {
            listener = new ServerSocket(ConfigVar.portTag);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            while (true) {

                // accept a connection from Client and receive a Socket object
                Socket socketOfServer = listener.accept();
                new ServiceThread(socketOfServer, clientNumber++).start();
            }
        } finally {
            listener.close();
        }

    }

    private static void log(String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        // System.out.println(formatter.format(date));
        System.out.println("[" + formatter.format(date) + "] " + message);
    }


    private static class ServiceThread extends Thread {


        private int clientNumber;
        private Socket socketOfServer;


        public ServiceThread(Socket socketOfServer, int clientNumber) {
            this.clientNumber = clientNumber;
            this.socketOfServer = socketOfServer;

            // Log
            log("New connection with tag# " + this.clientNumber + " at "
                    + socketOfServer);
        }

        @Override
        public void run() {

            try {

                // open input/output Stream of socket
                BufferedReader is = new BufferedReader(new InputStreamReader(
                        socketOfServer.getInputStream()));
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(
                        socketOfServer.getOutputStream()));

                String okIN = null;
                String failIN = null;

                while (true) {
                    // process data from client
                    // System.out.println("DEBUG " + is.toString());
                    // String contents=null;
                    String line = null;

                    // Method 1: Read a line (==> need end with newLine \n)
                    // line = is.readLine();
                    // System.out.println("DEBUG 1: " + line);

                    /*
                     * // Method 2: .read(char[]) >> OK char charArray[] = new
                     * char[100]; is.read(charArray); contents = new
                     * String(charArray); // System.out.println("DEBUG 2: " +
                     * contents);
                     */

                    String contents = is.readLine();
                    // System.out.println("DEBUG 1: " + line);
                    if (contents == null) {
//                        	System.out.println("NO data in mess from client");
                    } else {
                        // log("Content = " + contents);
                        // System.out.print(contents);
                        // 205.205.205.6,@V2X,SIZE,ID,CMD,LAT,LONG,DATE,TIME
                        String[] pram = contents.split(",");

                        if (pram.length == 9) { // data message
                            log("Data message: " + contents);
                            TagPacket packet = new TagPacket(pram[0], pram[1],
                                    pram[2], pram[3], pram[4], pram[5], pram[6],
                                    pram[7], pram[8]);


                            //TODO:
                            // create packet_tag logging in database
                            // API to create tagId mapping with userId (using email parameter)

//                          System.out.print("Date: " + pram[6]);

//                            Tag tagCurrent = tagRepoStatic.findByTagId(packet.getId());
//                            if (tagCurrent == null) {
//                                packet.setState("2");
//                            }
//                            TagPackage tagPackage = TagPackage.builder()
//                                    .sign(packet.getSign())
//                                    .seq(packet.getNumOfPacket())
//                                    .mty(packet.getTypeOfPacket())
//                                    .tagId(packet.getId())
//                                    .lat(packet.getLat())
//                                    .log(packet.getLng())
//                                    .date(FormatDateFromNewsTag.FormatDate(packet.getDate()))
//                                    .time(packet.getTime())
//                                    .state(packet.getState())
//                                    .build();
//
//                            tagRepoStatic.createNewsFromTag(tagPackage);

//						    System.out.println("SIGN = " + packet.getSign());
//							System.out.println("SEQ = " + packet.getNumOfPacket());
//							System.out.println("MTY = " + packet.getTypeOfPacket());
//							System.out.println("ID = " + packet.getId());
//							System.out.println("LAT = " + packet.getLat());
//							System.out.println("LONG = " + packet.getLng());
//							System.out.println("DATE = " + packet.getDate());
//							System.out.println("TIME = " + packet.getTime());
//							System.out.println("STATE = " + packet.getState());

                            //1. tagID registered by User ?
                            // return user
                            StateServer state = null;

                            String timeT = packet.getDate() + " " + packet.getTime(); // need to format time

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                            Date parsedDate = dateFormat.parse(timeT);
                            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

                            double latT = Double.parseDouble(packet.getLat());
                            double lngT = Double.parseDouble(packet.getLng());
                            double latF;
                            double lngF;
                            double range;
                            final int RANGE = 1;

                            boolean carIn = true;
                            if (packet.getState().equals("0")) {
                                carIn = false;
                            }
                            ;
                            boolean carOut = !carIn;

                            // CHECK LOC and FULL SLOT
                            //TODO:
                                // tune RANGE and GPS to  match with only field !!!!
                            System.out.println("[TagModule] Process with LOC");
                            int fieldId = -1;
                            Double price = 0.0;
                            boolean checkFull = false;
                            List<Field> fields = fieldRepoStatic.findAll();
                            for (Field f : fields) {
                                latF = Double.parseDouble(f.getLatitude());
                                lngF = Double.parseDouble(f.getLongitude());
                                range = (latF - latT) * (latF - latT) + (lngF - lngT) * (lngF - lngT);
                                if (range < RANGE) {
                                    fieldId = f.getId();
                                    price = f.getPrice();
                                    int busySlot = (int) slotRepoStatic.findAll().stream()
                                            .filter(slot -> ((slot.getStatusDetector() != null && slot.getStatusDetector())
                                                    || (slot.getStatusCam() != null && slot.getStatusCam()))
                                                    && slot.getFieldId().equals(f.getId()))
                                            .count();
                                    int totalSlot = (int) slotRepoStatic.findAll().stream()
                                            .filter(slot -> slot.getFieldId().equals(f.getId())).count();
                                    if (busySlot == totalSlot) {
                                        checkFull = true;
                                    }
                                }
                            }

                            // CHECK REGISTERED TAG
                            System.out.println("[TagModule] Process with tagID = " + packet.getId());
                            Tag tag = tagRepoStatic.findByTagId(packet.getId());
                            User user = userRepoStatic.findById(tag.getUserId());
                          //  User user = userRepoStatic.findByTagId(packet.getId());



                            if (fieldId < 0) {
                                state = StateServer.NO_LOC;
                                System.out.println("[TagModule] Tag isn't in any fields");
                            } else if (checkFull) {
                                state = StateServer.ERR;
                                System.out.println("[TagModule] Full parking");
                            } else if (user == null) {
                                state = StateServer.NO_REG;
                                System.out.println("[TagModule] No user with " + packet.getId());
                            } else {

                                System.out.println("[TagModule] FiedId is " + fieldId);
                                Contract contract = null;
                                boolean checkBooked = false; //
                                boolean checkStateContract = false;

                                List<Contract> contracts = contractRepoStatic.findByUser(user);
                                if (contracts.size()<1) {
                                    System.out.println("No Contract");
                                    state = StateServer.ERR;
                                    if (carIn) {
                                        checkStateContract = true;  // người dùng mới hoàn toàn, chưa tạo contract
                                    } else {
                                        System.out.println("Car -out but no contract !?");
                                    }
                                } else {
                                    // CHECK STATUS CONTRACT
                                    Contract c = contracts.get(contracts.size() - 1); // latest contracts
                                    System.out.println("Current/latest contract state " + c.getStatus());
                                    if (carIn) {
                                        if (c.getStatus().equals("V")) { // xe vào + đã book
                                            checkBooked = true;
                                            contract = c;
                                            checkStateContract = true;
                                        } else if (c.getStatus().equals("R")) { // xe vào + chưa book
                                            checkStateContract = true;
                                        } else { // xe vào trong khi contract báo đang đỗ
                                            checkStateContract = false;
                                        }
                                    } else { // xe ra
                                        if (c.getStatus().equals("Y") || c.getStatus().equals("I")) { // xe ra khi đang đỗ
                                            checkBooked = true;  // có contract
                                            contract = c;
                                            checkStateContract = true;
                                        } else {
                                            checkStateContract = false;
                                        }
                                    }

                                    if (checkStateContract) {
                                        //   System.out.println("Check contract " + contract.getId() +  contract.getStatus());
                                        if (!checkBooked && carIn) { // no booked before >> book immediately
                                            // find all Fields and find whether Tag in a field ?
                                            // Tag in field > parking
                                            contractRepoStatic.createAndUpdate(Contract.builder()
                                                    .fieldId(fieldId)
                                                    .timeInBook(null)
                                                    .timeOutBook(null)
                                                    .carNumber(user.getEquipment())
                                                    .dtCreate(new Timestamp(new Date().getTime()))
                                                    .timeCarIn(timestamp)
                                                    .timeCarOut(null)
                                                    .status("I")
                                                    .cost("")
                                                    .userId(user.getId())
                                                    .build());
                                            state = StateServer.IN;
                                            System.out.println("[TagModule] Car in parking with new contract");
                                        } else { // booked >> check LOC || car out
                                            int fieldIdContract = contract.getFieldId();
                                            if (fieldId == fieldIdContract) { // match contract update tag
                                                if (carIn) { // update booked and tag in
                                                    contract.setTimeCarIn(timestamp);
                                                    contract.setStatus("Y");
                                                    contractRepoStatic.createAndUpdate(contract);
                                                    state = StateServer.IN;
                                                    System.out.println("[TagModule] OK: Car in parking with booked contract");
                                                } else { // update when tag out
                                                    contract.setTimeCarOut(timestamp);
                                                    contract.setStatus("R");
                                                    double cost = (double) (contract.getTimeCarOut().getTime() - contract.getTimeCarIn().getTime()) / 1000 / 60 / 60 * price;

                                                    if (cost > 0) {
                                                        contract.setCost(String.valueOf(cost));
                                                        contractRepoStatic.createAndUpdate(contract);
                                                        state = StateServer.OUT;
                                                        System.out.println("[TagModule] Car out OK");
                                                    } else {
                                                        System.out.println("Time car out < Time car in ");
                                                        state = StateServer.ERR;
                                                    }

                                                }
                                            } else {
                                                if (carOut) {
                                                    state = StateServer.ERR;
                                                    System.out.println("[TagModule] Tag -out is not in right fields of contract");
                                                } else { // arrive wrong field
                                                    System.out.println("[TagModule] Car in parking with new contract BUT wrong field");
                                                    contractRepoStatic.createAndUpdate(Contract.builder()
                                                            .fieldId(fieldId)
                                                            .timeInBook(null)
                                                            .timeOutBook(null)
                                                            .carNumber(user.getEquipment())
                                                            .dtCreate(new Timestamp(new Date().getTime()))
                                                            .timeCarIn(timestamp)
                                                            .timeCarOut(null)
                                                            .status("Y")
                                                            .cost("")
                                                            .userId(user.getId())
                                                            .build());
                                                    state = StateServer.IN;
                                                    contract.setStatus("R"); // change current(wrong) contract to R
                                                    contractRepoStatic.createAndUpdate(contract);

                                                }
                                            }
                                        }
                                    } else {
                                        state = StateServer.ERR;
                                        System.out.println("ERROR STATE of current or latest contract");
                                    }
                                }
                            }


                            //2. Check if booked ? - contract with user and tagID
                            // return fileID


                            String mess;
                            switch (state.ordinal()) {
                                case 0:
                                case 1:
                                    mess = SIGN + packet.numOfPacket + ","
                                            + MTY.DATA.ordinal() + "," + packet.getId()
                                            + "," + packet.getState();
                                    os.write(mess);
                                    break;
                                case 2:
                                case 3:
                                case 4:
                                    mess = SIGN + packet.numOfPacket + ","
                                            + MTY.DATA.ordinal() + "," + packet.getId()
                                            + "," + state.ordinal();
                                    os.write(mess);
                                    break;
                                default:
                                    System.out.println("ERROR processing in Server");
                            }
                            os.newLine();
                            os.flush();


//                            String inout; // respone for IN and out all OK
//                            inout = packet.getState();
//                            okIN = SIGN + packet.numOfPacket + ","
//                                    + MTY.DATA.ordinal() + "," + packet.getId()
//                                    + "," + inout;
//
//
//                            failIN = SIGN + packet.numOfPacket + ","
//                                    + MTY.DATA.ordinal() + "," + packet.getId()
//                                    + "," + state.ordinal();
//
//                            double lat = Double.parseDouble(packet.getLat());
//                            double lng = Double.parseDouble(packet.getLng());
//
//                            double LATF = 21.19248992;
//                            double LNGF = 105.82557750;
//
//                            double check;
//                            check = Math.sqrt(Math.pow((lat-LATF), 2) + Math.pow((lng-LNGF), 2));
//
//                            if (check <10){
//                                os.write(okIN);
//                                System.out.println("Respone message: OK " + okIN);
//                            }
//                            else if (Integer.parseInt(packet.getNumOfPacket()) % 5 != 0){
//                                os.write(failIN);
//                                System.out.println("Respone message: FAIL " + failIN);
//                            }
//                            else {
//                                os.write(okIN);
//                                System.out.println("Respone message: OK " + okIN);
//                            }
//
//                            //log("send response");
//                            // os.write("ACK," + packet.getId());
//                            os.newLine();
//                            os.flush();
                        } else if (pram.length == 4) {
                            log("====>>> DONE: ACK message: " + contents);
                            TagPacket packet = new TagPacket(pram[0], pram[1],
                                    pram[2], pram[3]);
                            // os.write(">> OK");
                            // os.newLine();
                            // os.flush();

                            break; // end of connection OK
                        } else {
                            System.out.println("Waiting ACK message: ");
                        }
                    }


                    // Method 3: read() >> no ending
                    // int i;
                    // while ((i = is.read()) != -1) {
                    // System.out.print ((char) i);
                    // }
                    // is.close();
                    //
                    //	System.out.println("----------Reading OK--------------\n");
                    seq = "1,";
                    id = ",TAG1";
                    lat = ",21.19254315,";
                    lng = "105.82550243,";
                    date = "10/5/21,";
                    time = "6:27:49,71,";


                    // String okIN= SIGN + packet .numOfPacket + "," +
                    // MTY.DATA.ordinal() + "," + packet.getId() + "," +
                    // STATE.IN.ordinal();
                    // String failIN= SIGN + packet.numOfPacket + "," +
                    // MTY.DATA.ordinal() + "," + packet.getId() + "," +
                    // STATE.NO_LOC.ordinal();

                    /*
                     * if (packet.numOfPacket.contains("2")){ os.write(failIN);
                     * System.out.println(failIN); } else { os.write(okIN);
                     * System.out.println(okIN); }
                     *
                     *
                     * log("send response"); // os.write("ACK," +
                     * packet.getId()); os.newLine(); os.flush();
                     */

                    // End connection
                    /*
                     * if (pram.length==4) { os.write(">> OK"); os.newLine();
                     * os.flush(); break; }
                     */
                }

            } catch (IOException | ParseException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    static public class TagPacket {

        // String add; // 0
        String sign;
        String numOfPacket;
        String typeOfPacket;
        String id;
        String lat; //4
        String lng; //5
        String date;
        String time;
        String state;

        public TagPacket(String sign, String numOfPacket, String typeOfPacket,
                         String id, String lat, String lng, String date, String time,
                         String state) {
            super();
            // this.add = add;
            this.sign = sign;
            this.numOfPacket = numOfPacket;
            this.id = id;
            this.typeOfPacket = typeOfPacket;
            this.lat = lat;
            this.lng = lng;
            this.date = date;
            this.time = time;
            this.state = state;
        }

        public TagPacket(String sign, String numOfPacket, String typeOfPacket,
                         String id) {
            super();
            this.sign = sign;
            this.numOfPacket = numOfPacket;
            this.typeOfPacket = typeOfPacket;
            this.id = id;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getNumOfPacket() {
            return numOfPacket;
        }

        public void setNumOfPacket(String numOfPacket) {
            this.numOfPacket = numOfPacket;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTypeOfPacket() {
            return typeOfPacket;
        }

        public void setTypeOfPacket(String typeOfPacket) {
            this.typeOfPacket = typeOfPacket;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

}
