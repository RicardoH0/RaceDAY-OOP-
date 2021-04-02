
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.info.RaceInfo;
import model.messages.LeaderBoardModel;
import model.messages.LineCrossingModel;
import model.messages.Message;
import model.messages.TelemetryModel;

public class RaceModel implements PropertyChangeEnabledRaceControls {

    /**
     * Manager for Property Change Listeners.
     */
    private final PropertyChangeSupport myPcs;

    /**
     * List for all message.
     */
    private List<String> myAl = new ArrayList<>();
    /**
     * Header Message List.
     */
    private final List<String> myHMList = new ArrayList<>();
    /**
     * Data List.
     */
    private final List<String> myAList = new ArrayList<>();
    /**
     * Two D List for Header Message.
     */
    private final List<List<String>> myTwoDList = new ArrayList<>();
    /**
     * Two D List for Information.
     */
    private final List<List<Message>> myM2DList = new ArrayList<>();

    /**
     * Local myTime.
     */
    private int myTime;

    /**
     * String for repeat word.
     */
    private String myTYPE = "$";

    /**
     * String for repeat word.
     */
    private String myTYPE2 = "#";

    /**
     * String for repeat word.
     */
    private String myCOLON = ":";



    public RaceModel() {
        myPcs = new PropertyChangeSupport(this);

    }

    @Override
    public void advance() {
        advance(1);
    }

    @Override
    public void advance(final int theMillisecond) {
        if (theMillisecond < 0) {

            throw new IllegalArgumentException("theMillisecond must be positive!");
        }
        changeTime(myTime + theMillisecond);
    }

    @Override
    public void moveTo(final int theMillisecond) {
        final String[] str = myAList.get(myAList.size() - 1).split(myCOLON);
        if (theMillisecond < 0 || theMillisecond > Integer.parseInt(str[1])) {

            throw new IllegalArgumentException("theMillisecond must be positive.");
        }

        changeTime(theMillisecond);
    }

    @SuppressWarnings("resource")
    @Override
    public void loadRace(final File theRaceFile) throws IOException {
        
        
        
        Scanner sc;
  //      myFile = theRaceFile;
        sc = new Scanner(theRaceFile);

        if (!theRaceFile.getName().contains(".rce")) {
            throw new IOException();
        }
        myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
                        "Load file: Start- This may take a while.Please be patient");



        int rows = 0;
        sc = new Scanner(theRaceFile);
        while (sc.hasNextLine()) {
            rows++;
            sc.nextLine();
        }
        // try {

        sc = new Scanner(theRaceFile);
        //System.out.println(rows);

        while (sc.hasNext()) {
            final String str = sc.nextLine();
            // System.out.println(str);
            myAl.add(str);

        }
        // System.out.println(myAl);

        for (int i = 0; i < myAl.size(); i++) {
            if (myAl.get(i).startsWith(myTYPE2)) {
                myHMList.add(myAl.get(i));
            } else if (myAl.get(i).startsWith(myTYPE)) {
                myAList.add(myAl.get(i));
            }
            // System.out.println(myAList.size());
        }

        //
        for (int j = 0; j < myHMList.size(); j++) {
            final String[] str = myHMList.get(j).split(myCOLON);
            final List<String> tempList = Arrays.asList(str);
            myTwoDList.add(tempList);
        }
        sc.close();
        // filterList(myAl);
        final RaceInfo ri = new RaceInfo(myTwoDList);
        myPcs.firePropertyChange(PROPERTY_RACE_INFORMATION, null, ri);
        myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
                        "Load file: RACE information loaded.");
        // System.out.println(myAl.get(1));

        int time = 0;
        myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
                        "Load file: Loading telemetry information...");
        myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
                        "Load file: Still loading...");
        for (int j = 0; j < ri.getTotalTime(); j++) {
            myM2DList.add(new ArrayList<Message>());
        }
        myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
                        "Load file: Still loading...");
        for (int i = 0; i < myAList.size(); i++) {
            myM2DList.add(new ArrayList<Message>());
            if (myAList.get(i).startsWith(myTYPE)) {
                final String[] str = myAList.get(i).split(myCOLON);
                // System.out.println(str[1]);
                if (time == Integer.parseInt(str[1])) {
                    passMessage(str[0], time, myAList.get(i));
                } else {
                    time = Integer.parseInt(str[1]);
                    passMessage(str[0], time, myAList.get(i));
                }
            }
        }
        myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
                        "Load file: Still loading...");
        myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
                        "Load file: Complete!");

    }

    
    
    
    
    private void passMessage(final String theHead, final int theTime,
                             final String theMessage) {
        switch (theHead) {
            case "$T":
                myM2DList.get(theTime).add(new TelemetryModel(theMessage));
//                myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
//                                "Load file: Loading telemetry information...");
                break;

            case "$L":
                myM2DList.get(theTime).add(new LeaderBoardModel(theMessage));
//                myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
//                                "Load file: Still loading ...");
                break;

            case "$C":
                myM2DList.get(theTime).add(new LineCrossingModel(theMessage));
//                myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null,
//                                "Load file: Still loading ...");
                break;

            default:
                break;

        }
    }
    // myNewList = theList;

    /**
     * Helper method to change the value of time and notify observers. Functional
     * decomposition.
     * 
     * @param theMillisecond the time to change to
     */
    private void changeTime(final int theMillisecond) {

        final int old = myTime;
        myTime = theMillisecond;
        final String[] str = myAList.get(myAList.size() - 1).split(myCOLON);
        //System.out.println(Integer.parseInt(str[1]));
        if (myTime >= Integer.parseInt(str[1])) {
            myPcs.firePropertyChange(PROPERTY_TIME, old, old);
            myPcs.firePropertyChange(PROPERTY_RACE_STATUS, null, false);
        } else if (myTime < 0) {
            myPcs.firePropertyChange(PROPERTY_TIME, old, old);
            myPcs.firePropertyChange(PROPERTY_RACE_STATUS, null, false);
        } else {
            myPcs.firePropertyChange(PROPERTY_TIME, old, myTime);
            myPcs.firePropertyChange(PROPERTY_RACE_STATUS, null, true);
        }

        updateMessage(old, myTime);

    }

    private void updateMessage(final int theOldTime, final int theNewTime) {
        final String[] str = myAList.get(myAList.size() - 1).split(myCOLON);

        if (theNewTime > theOldTime) {
            for (int i = theOldTime; i < theNewTime; i++) {
                for (int j = 0; j < myM2DList.get(i).size(); j++) {
                    myPcs.firePropertyChange(PROPERTY_MESSAGE, null, myM2DList.get(i).get(j));
                }
            }
        } else if (theNewTime == 0) {
            for (int j = 0; j < myM2DList.get(0).size(); j++) {
                myPcs.firePropertyChange(PROPERTY_MESSAGE, null, myM2DList.get(0).get(j));

            }
        } else if (theNewTime == Integer.parseInt(str[1])) {
            for (int j = 0; j < myM2DList.get(Integer.parseInt(str[1])).size(); j++) {
                myPcs.firePropertyChange(PROPERTY_MESSAGE, null,
                                         myM2DList.get(Integer.parseInt(str[1])).get(j));

            }
        }
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(theListener);

    }

    @Override
    public void addPropertyChangeListener(final String thePropertyName,
                                          final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(thePropertyName, theListener);

    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(theListener);

    }

    @Override
    public void removePropertyChangeListener(final String thePropertyName,
                                             final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(thePropertyName, theListener);
    }

}

// for (int t = 0; t < myAList.size(); t++) {
// final String[] str = myAList.get(t).split(colon);
// final List<String> tempList = Arrays.asList(str);
// myTime2DList.add(tempList);
// }
// for (int t = 0; t < myTime2DList.size(); t++) {
//
// time = Integer.parseInt(myTime2DList.get(t).get(1));
// final StringBuilder sb = new StringBuilder();
// sb.append(myTime2DList.get(t).get(2));
// sb.append(myTime2DList.get(t).get(3));
// sb.append(myTime2DList.get(t).get(4));
// message = sb.toString();
//
// }

// if (myTime > old) {
// for (int i = old; i < myTime; i++) {
// myPcs.firePropertyChange(PROPERTY_MESSAGE, null, myAList.get(i));
// }
// }
//
// if (myTime == 0) {
// for (int i = 0; i < myAList.size(); i++) {
// myPcs.firePropertyChange(PROPERTY_MESSAGE, null, myAList.get(0));
// }
// }

// for(final List<Message> l :myMessagesNySecond){
// l.xlear();
// }
// myMBS.clear();
// if(!scan.hasNextLine()){
// scan.close()
// throw new IOException;
//
// broadcastMessage("losad file: RACE information loaded.");
// for(int i =0; i<myCRI.getTotalTime; i++){
// myMBS.add(new ArrayyList<>());}
//
