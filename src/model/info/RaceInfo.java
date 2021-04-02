package model.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RaceInfo implements RaceInformation {
    /**
     * Int for magic number.
     */
    private int myRACE = 0;
    /**
     * Int for magic number.
     */
    private int myTRACK = 1;
    /**
     * Int for magic number.
     */
    private int myWIDTH = 2;
    /**
     * Int for magic number.
     */
    private int myHEIGHT = 3;
    /**
     * Int for magic number.
     */
    private int myDISTANCE = 4;
    /**
     * Int for magic number.
     */
    private int myTIME = 5;
    
    /**
     * Int for magic number.
     */
    private int myRACEPARTIS = 5;
    
    /**
     * Int for magic number.
     */
    private int myRACER = 7;
    
    /**
     * Int for magic number.
     */
    private int myInfo = 1;
    
    /**
     * Local RaceInfo List.
     */
    private final List<List<String>>  myRaceInfo;
    

    
    public RaceInfo(final List<List<String>> theRaceInfo) {
        myRaceInfo = theRaceInfo;
        

    }
    
    @Override
    public List<RaceParticipant> getParticipants() {
        final List<RaceParticipant> raceParticipant = new ArrayList<>();
       
        for (int i = myRACER; i < myRaceInfo.size(); i++) {
            String name = "";
            int num = 0;
            int dist = 0;
            final int index = myRaceInfo.get(i).get(0).length();
            name = myRaceInfo.get(i).get(1);
            dist = (int) Double.parseDouble(myRaceInfo.get(i).get(2));
            final Scanner sc = new Scanner(myRaceInfo.get(i).get(0));
            if (myRaceInfo.get(i).get(0).contains("#")) {
                
                final String temNum = myRaceInfo.get(i).get(0).substring(1, index); 
                num = Integer.parseInt(temNum);
            }
            raceParticipant.add(new RaceParticipantModel(name, num, dist)); 
        }
        
        return raceParticipant;
    }

    @Override
    public int getTotalTime() {
        
        return Integer.parseInt(myRaceInfo.get(myTIME).get(myInfo));
                        //Integer.parseInt(myRaceInfo.get(myTIME));
    }

    @Override
    public int getTrackDistance() {
        
        return Integer.parseInt(myRaceInfo.get(myDISTANCE).get(myInfo));
                        //Integer.parseInt(myRacesInfo.get(myDISTANCE));
    }

    @Override
    public String getRaceName() {
        
        return myRaceInfo.get(myRACE).get(myInfo);
                        //myRaceInfo.get(myRACE);
    }

    @Override
    public String getTrackType() {
        
        return myRaceInfo.get(myTRACK).get(myInfo);
                        //myRaceInfo.get(myTRACK);
    }    
        
    @Override
    public int getHeight() {
        
        return Integer.parseInt(myRaceInfo.get(myHEIGHT).get(myInfo));
                        //Integer.parseInt(myRaceInfo.get(myHEIGHT));
    }

    @Override
    public int getWidth() {
        
        return Integer.parseInt(myRaceInfo.get(myWIDTH).get(myInfo));
                        //Integer.parseInt(myRaceInfo.get(myWIDTH));
    }

}

//final List<List<String>> raceParticipant = new ArrayList<>();
//for (int i = myRACER; i < myRaceInfo.size(); i++) {
//  final List<String> temList = myRaceInfo.get(i);
//  raceParticipant.add(temList);
//}

