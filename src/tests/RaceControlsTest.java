package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_RACE_INFORMATION;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_MESSAGE;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_RACE_STATUS;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.PropertyChangeEnabledRaceControls;
import model.RaceModel; //TODO Change to YOUR concrete RaceControls class
import model.info.RaceInformation;
import model.info.RaceParticipant;
import model.messages.LeaderBoard;
import model.messages.LineCrossing;
import model.messages.Message;
import model.messages.Telemetry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * A test class with a set of [ordered] test cases that test a RaceControls model. Use this
 * class to test your model. 
 * 
 *  Notes:
 *  This test class uses JUnit 5 methods, assertions, and syntax. 
 *   
 *  The test cases in this class are ordered and when one fails, all following cases may or may
 *  not fail and should not be considered accurate. Take note which cases fails first and focus
 *  on it for debugging purposes.
 *  https://junit.org/junit5/docs/snapshot/user-guide/#writing-tests-test-execution-order
 *  
 *  The test cases in this class use the same RaceControlls object and do not re-instantiate 
 *  it before each test case. This prevents the need to reload the race file for every test 
 *  case.
 *  https://junit.org/junit5/docs/snapshot/user-guide/#writing-tests-test-instance-lifecycle 
 *  
 *  PropertyChangeListener is abbreviated as PCL in comments below.
 *  
 * 
 * @author Charles Bryan
 * @version Winter 2020
 */
@TestMethodOrder(OrderAnnotation.class) 
@TestInstance(Lifecycle.PER_CLASS) 
class RaceControlsTest {
    
    /**
     * Constant representing the last millisecond from the test race file. 
     */
    private static final int END_OF_RACE = 62939;
    
    /**
     * Constant representing the filename for the test race. 
     */
    private static final String RACE_FILE = "./race_files/test_race.rce";

    /** 
     * Object level Set variable to access inside and out of anonymous inner classes.  
     * From Oracle documentation: An anonymous class cannot access local variables in its 
     * enclosing scope that are not declared as final or effectively final.
     * Source: https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html
     */
    private Set<String> myExpectedMessages;
    
    /** 
     * Object level boolean variable to access inside and out of anonymous inner classes.  
     * From Oracle documentation: An anonymous class cannot access local variables in its 
     * enclosing scope that are not declared as final or effectively final.
     * Source: https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html
     */
    private boolean mySeen;
    
    /** 
     * Object level int variable to with inside and out of anonymous inner classes.  
     * From Oracle documentation: An anonymous class cannot access local variables in its 
     * enclosing scope that are not declared as final or effectively final.
     * Source: https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html
     */
    private int myExpectedTime;
    
    /** The race under test. */
    private PropertyChangeEnabledRaceControls myRace;

    @BeforeAll
    void setUpFile() {        
        //TODO: Change the instantiating type here to match your Race Controls concrete class.
        myRace = new RaceModel();
    }
    
    /**
     * This test case loads a race file and via PCL looks to see if the Race model sends the 
     * correct race information when a file is loaded. The test cases includes asserts for 
     * all information in the race header including racers.  
     */
    @Test
    @Order(1)
    void testLoadRace() {
        mySeen = false;
        final PropertyChangeListener pcl = theEvent -> {
            if (PROPERTY_RACE_INFORMATION.equals(theEvent.getPropertyName())) {
                //this method is WAY TOO long. 
                assertTrue(theEvent.getNewValue() instanceof RaceInformation);
                final RaceInformation ri = (RaceInformation) theEvent.getNewValue();
                assertEquals("TestRace", ri.getRaceName(), "Testing Race Name");
                assertEquals("OvalTrack", ri.getTrackType(), "Testing Track Type");
                assertEquals(END_OF_RACE + 1, ri.getTotalTime(), "Testing Race Total Time");
                assertEquals(2,  ri.getWidth(), "Testing track width");
                assertEquals(1,  ri.getHeight(), "Testing track width");
                assertEquals(500000, ri.getTrackDistance(), "Testing track distance");
                
                final List<RaceParticipant> racers = ri.getParticipants();
                assertEquals(5, racers.size(), "Testing number of Participants");
                
                //test the individual racers...
                final Map<String, RaceParticipant> racerMap = new HashMap<>();
                for (final RaceParticipant p : racers) {
                    racerMap.put(p.getName(), p);
                }
                assertEquals("ABC", racerMap.get("ABC").getName());
                assertEquals(10, racerMap.get("ABC").getNumber());
                assertEquals(0.0, racerMap.get("ABC").getStartingDistance());
                
                assertEquals("DEF", racerMap.get("DEF").getName());
                assertEquals(20, racerMap.get("DEF").getNumber());
                assertEquals(-5000.0, racerMap.get("DEF").getStartingDistance());
                
                assertEquals("GHI", racerMap.get("GHI").getName());
                assertEquals(30, racerMap.get("GHI").getNumber());
                assertEquals(-10000.0, racerMap.get("GHI").getStartingDistance());
                
                assertEquals("LMN", racerMap.get("LMN").getName());
                assertEquals(40, racerMap.get("LMN").getNumber());
                assertEquals(-15000.0, racerMap.get("LMN").getStartingDistance());
                
                assertEquals("XYZ", racerMap.get("XYZ").getName());
                assertEquals(50, racerMap.get("XYZ").getNumber());
                assertEquals(-20000.0, racerMap.get("XYZ").getStartingDistance());
                
                mySeen = true;
            } 
        };
        
        myRace.addPropertyChangeListener(pcl);

        try {
            myRace.loadRace(new File(RACE_FILE));
        } catch (final IOException exception) {
            fail("File not loaded or...here's the exception: " + exception.getMessage());
        }
        myRace.removePropertyChangeListener(pcl);
        
        assertTrue(mySeen, "Did not see race information!");
    }
    
    /**
     * This test case calls moveTo() in the race model two times and via PCL looks to 
     * see if the race model sends time changes. A boolean is included to check that the 
     * PCL finds a PROPERTY_TIME value in the PropertyChangeEvent. 
     */
    @Test
    @Order(2)
    void testMoveToForTime() {
        mySeen = false;
        
        mySeen = false;
        final PropertyChangeListener pcl = theEvent ->  {
            if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                assertEquals(myExpectedTime, (Integer) theEvent.getNewValue());
                mySeen = true;
            } 
        };
        
        myRace.addPropertyChangeListener(pcl);
        
        myExpectedTime = 300;
        myRace.moveTo(myExpectedTime);
        
        myExpectedTime = 0;
        myRace.moveTo(myExpectedTime);
        
        myRace.removePropertyChangeListener(pcl); 
        
        assertTrue(mySeen, "Did not see a new time!");
    }

    /**
     * This test case calls advance() in the race model five times and via PCL looks to 
     * see if the race model sends time changes. A boolean is included to check that the 
     * PCL finds a PROPERTY_TIME value in the PropertyChangeEvent. 
     */
    @Test
    @Order(3)
    void testAdvanceForTime() {
        mySeen = false;
        final PropertyChangeListener pcl = theEvent ->  {
            if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                assertEquals(myExpectedTime, (Integer) theEvent.getNewValue());
                mySeen = true;
            } 
        };
        myExpectedTime = 0;
        myRace.moveTo(myExpectedTime);
        
        myRace.addPropertyChangeListener(pcl);
        
        myExpectedTime = 1;
        myRace.advance(); // advancing to 1 millisecond
        myExpectedTime++;
        myRace.advance(); // advancing to 2 millisecond
        myExpectedTime++;
        myRace.advance(); // advancing to 3 millisecond
        myExpectedTime++;
        myRace.advance(); // advancing to 4 millisecond
        myExpectedTime++;
        myRace.advance(); // advancing to 5 millisecond
        
        myRace.removePropertyChangeListener(pcl);
        
        assertTrue(mySeen, "Did not see a new time!");
    }
    
    /**
     * This test case calls advance() in the race model one time (moving the model's internal 
     * clock from 5 to 6)  and via PCL looks to see if the race model sends the correct 
     * message. A boolean is included to check that the PCL finds a PROPERTY_MESSAGE 
     * value in the PropertyChangeEvent. 
     * The expected message: $T:5:40:-14971.31:0
     */
    @Test
    @Order(4)
    void testAdvanceForMessages() {
        mySeen = false;
        final PropertyChangeListener pcl = theEvent -> {
            if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
                assertTrue(theEvent.getNewValue() instanceof Telemetry);
                final Telemetry tm = (Telemetry) theEvent.getNewValue();
                assertEquals(40, tm.getNumber(), "Test the racer number");
                assertEquals(-14971.31, tm.getDistance(), 0.00001, "Test the distance");
                assertEquals(0, tm.getLap(), "Test the lap");
                assertEquals("$T:5:40:-14971.31:0", tm.getMessage(), "Test the message");
                mySeen = true;
            } else if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                assertEquals(6, (Integer) theEvent.getNewValue());
            } 
        };
        
        myExpectedTime = 5;
        // do this moveTo BEFORE adding the PCL so we don't "see" messages from the moveTo
        myRace.moveTo(myExpectedTime); 
        
        myRace.addPropertyChangeListener(pcl);
        
        myRace.advance(); // advancing to 6
        
        myRace.removePropertyChangeListener(pcl);
        
        assertTrue(mySeen, "Did not see a telemetry message!");
    }
    
    /**
     * This test case calls advance(int) in the race model two times and via PCL looks to 
     * see if the race model sends time changes. A boolean is included to check that the 
     * PCL finds a PROPERTY_TIME value in the PropertyChangeEvent. 
     */
    @Test
    @Order(5)
    void testAdvanceIntForTime() {
        mySeen = false;
        final PropertyChangeListener pcl = theEvent ->  {
            if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                assertEquals(myExpectedTime, (Integer) theEvent.getNewValue());
                mySeen = true;
            } 
        };
        
        myRace.addPropertyChangeListener(pcl);
        
        myExpectedTime = 0;
        myRace.moveTo(myExpectedTime);
        
        myExpectedTime = 15; //time = 0 therefore 0 + 15 = 15 expected time. 
        myRace.advance(15);
        
        myExpectedTime = 25; //time = 15 therefore 15 + 10 = 25 expected time. 
        myRace.advance(10);
        
        myRace.removePropertyChangeListener(pcl); 
        
        assertTrue(mySeen, "Did not see a new time!"); 
    }
    
    /**
     * This test case calls advance(int) in the race model one time (moving the model's 
     * internal clock from 1 to 10)  and via PCL looks to see if the race model sends the 
     * correct messages. A boolean is included to check that the PCL finds a PROPERTY_MESSAGE 
     * value in the PropertyChangeEvent. 
     * The expected messages range from 1 to 9 milliseconds. 
     */
    @Test
    @Order(6)
    void testAdvanceIntForMessages() {
        myExpectedMessages = new HashSet<>();
        myExpectedMessages.add("$T:1:10:8.32:0");
        myExpectedMessages.add("$T:4:10:20.79:0");
        myExpectedMessages.add("$T:4:30:-9975.61:0");
        myExpectedMessages.add("$T:4:50:-19973.27:0");
        myExpectedMessages.add("$T:5:40:-14971.31:0");
        myExpectedMessages.add("$T:8:20:-4954.02:0");
        myExpectedMessages.add("$T:9:50:-19946.54:0");
        
        mySeen = false;
        final PropertyChangeListener pcl = theEvent -> {
            if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
                assertTrue(theEvent.getNewValue() instanceof Telemetry);
                final Telemetry tm = (Telemetry) theEvent.getNewValue();
                myExpectedMessages.remove(tm.getMessage());
            } 
        };
        
        // do this moveTo BEFORE adding the PCL so we don't "see" messages from the moveTo
        myRace.moveTo(1);
        
        myRace.addPropertyChangeListener(pcl);

        myRace.advance(9);

        myRace.removePropertyChangeListener(pcl); 
        
        assertEquals(0, myExpectedMessages.size());
        assertTrue(myExpectedMessages.isEmpty());
    }
    
    /**
     * This test case calls moveTo(0) in the race model one time (moving the model's 
     * internal clock to 0)  and via PCL looks to see if the race model sends the 
     * correct messages. A boolean is included to check that the PCL finds a PROPERTY_MESSAGE 
     * value in the PropertyChangeEvent. 
     * With 5 racers, there are 6 expected messages for every moveTo().   
     */
    @Test
    @Order(7)
    void testMoveToZeroForMessagesAndTime() {
        mySeen = false;
        myExpectedMessages = new HashSet<>();
        myExpectedMessages.add("$T:0:50:-19994.65:0");
        myExpectedMessages.add("$T:0:40:-14995.22:0");
        myExpectedMessages.add("$T:0:30:-9995.12:0");
        myExpectedMessages.add("$T:0:20:-4994.89:0");
        myExpectedMessages.add("$T:0:10:4.16:0");
        myExpectedMessages.add("$L:0:10:20:30:40:50");
        
        mySeen = false;
        final PropertyChangeListener pcl = theEvent -> {
            if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
                assertTrue(theEvent.getNewValue() instanceof Message);
                final Message m = (Message) theEvent.getNewValue();
                myExpectedMessages.remove(m.getMessage());
            } else if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                assertEquals(0, (Integer) theEvent.getNewValue());
                mySeen = true;
            } 
        };
        
        myRace.addPropertyChangeListener(pcl);

        myRace.moveTo(0);

        myRace.removePropertyChangeListener(pcl); 
        
        assertEquals(0, myExpectedMessages.size(), "The set should be empty.");
        assertTrue(mySeen, "Did not see a new time!");

    }
    
    /**
     * This test case calls moveTo(100) in the race model one time (moving the model's 
     * internal clock to 100)  and via PCL looks to see if the race model sends the 
     * correct messages. A boolean is included to check that the PCL finds a PROPERTY_MESSAGE 
     * value in the PropertyChangeEvent. 
     * With 5 racers, there are 6 expected messages for every moveTo().   
     */
    @Test
    @Order(8)
    void testMoveToOneHunderdForMessagesAndTime() {
        mySeen = false;
        myExpectedMessages = new HashSet<>();
        myExpectedMessages.add("$T:99:10:415.90:0");
        myExpectedMessages.add("$T:98:30:-9517.07:0");
        myExpectedMessages.add("$T:96:50:-19481.43:0");
        myExpectedMessages.add("$T:94:20:-4514.63:0");
        myExpectedMessages.add("$T:92:40:-14555.33:0");
        myExpectedMessages.add("$L:0:10:20:30:40:50");
        
        final PropertyChangeListener pcl = theEvent -> {
            if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
                assertTrue(theEvent.getNewValue() instanceof Message);
                final Message m = (Message) theEvent.getNewValue();
                myExpectedMessages.remove(m.getMessage());
            } else if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                assertEquals(100, (Integer) theEvent.getNewValue());
                mySeen = true;
            } 
        };
        
        myRace.addPropertyChangeListener(pcl);

        myRace.moveTo(100);

        myRace.removePropertyChangeListener(pcl); 
        
        assertEquals(0, myExpectedMessages.size(), "The set should be empty.");
        
        assertTrue(mySeen, "Did not see a new time!");
    }
    
    /**
     * This test case calls moveTo(END_OF_RACE) in the race model one time (moving the model's 
     * internal clock to 62939)  and via PCL looks to see if the race model sends the 
     * correct messages. A boolean is included to check that the PCL finds a PROPERTY_MESSAGE 
     * value in the PropertyChangeEvent. 
     * With 5 racers, there are 6 expected messages for every moveTo().   
     */
    @Test
    @Order(9)
    void testMoveToEndForMessagesAndTime() {
        mySeen = false;
        myExpectedMessages = new HashSet<>();
        myExpectedMessages.add("$T:62933:20:52094.52:1");
        myExpectedMessages.add("$T:62933:10:6942.38:1");
        myExpectedMessages.add("$T:62936:50:27784.92:1");
        myExpectedMessages.add("$T:62939:30:38050.95:1");
        myExpectedMessages.add("$T:62939:40:3.75:1");
        myExpectedMessages.add("$L:27040:20:30:50:10:40");
        
        final PropertyChangeListener pcl = theEvent -> {
            if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
                assertTrue(theEvent.getNewValue() instanceof Message);
                final Message m = (Message) theEvent.getNewValue();
                myExpectedMessages.remove(m.getMessage());
            } else if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                assertEquals(END_OF_RACE, (Integer) theEvent.getNewValue());
                mySeen = true;
            } 
        };
        
        myRace.addPropertyChangeListener(pcl);

        myRace.moveTo(END_OF_RACE);

        myRace.removePropertyChangeListener(pcl); 
        
        assertEquals(0, myExpectedMessages.size(), "The set should be empty.");
        
        assertTrue(mySeen, "Did not see a new time!");
    }
    
    /**
     * This test case calls advance() at the end of the race model and via PCL looks to see if 
     * the race model sends PROPERTY_RACE_STATUS and false. A boolean is included to check 
     * that the PCL finds a PROPERTY_RACE_STATUS value in the PropertyChangeEvent. 
     */
    @Test
    @Order(10)
    void testAdvanceAtEndOfRace() { 
        mySeen = false;

        final PropertyChangeListener pcl = theEvent -> {
            if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                assertEquals(62939, (Integer) theEvent.getNewValue());
            } else if (PROPERTY_RACE_STATUS.equals(theEvent.getPropertyName())) {
                assertFalse((Boolean) theEvent.getNewValue());
                mySeen = true;
            }
        };
        
        myRace.addPropertyChangeListener(pcl);

        // Advances the race past 62939 to 62940 sending messages and time for 62939
        myRace.advance(); 
        
        // Attempts to Advances the race but there should be no more time so a PCE should fire
        // with PROPERTY_RACE_STATUS and false. We should NOT see time 62940 in a PCE.
        myRace.advance();

        myRace.removePropertyChangeListener(pcl); 
        
        assertTrue(mySeen, "Did not see the end of the race!");
    }
    
    /**
     * This test case calls advance(int) at the end of the race model and via PCL looks to 
     * see if  the race model sends PROPERTY_RACE_STATUS and false. A boolean is included to 
     * check  that the PCL finds a PROPERTY_RACE_STATUS value in the PropertyChangeEvent. 
     */
    @Test
    @Order(11)
    void testAdvanceIntAtEndOfRace() { 
        mySeen = false;

        final PropertyChangeListener pcl = theEvent -> {
            if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                fail("We should not see a time with this advance.");
            } else if (PROPERTY_RACE_STATUS.equals(theEvent.getPropertyName())) {
                assertFalse((Boolean) theEvent.getNewValue());
                mySeen = true;
            }
        };
        
        // do this moveTo BEFORE adding the PCL so we don't "see" messages or time
        // from the moveTo
        myRace.moveTo(END_OF_RACE);
        
        myRace.addPropertyChangeListener(pcl);
        
        // Attempts to Advances the race but there should be no more time so a PCE should fire
        // with PROPERTY_RACE_STATUS and false.  We should NOT see a PROPERTY_TIME in the PCE.
        myRace.advance(20);

        myRace.removePropertyChangeListener(pcl); 
        
        assertTrue(mySeen, "Did not see the end of the race!");
    }
    
    /**
     * This test case calls advance() in the race model through the entire race and via PCL 
     * looks to see if the race model sends the correct number of each type of messages. 
     */
    @Test
    @Order(12)
    void testAdvanceForAllMessages() {
        final Map<String, Set<String>> allMessages = new HashMap<>();
        allMessages.put("$T", new HashSet<>());
        allMessages.put("$L", new HashSet<>());
        allMessages.put("$C", new HashSet<>());
        
        mySeen = false;
        final PropertyChangeListener pcl = theEvent -> {
            if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
                if (theEvent.getNewValue() instanceof Telemetry) {
                    allMessages.get("$T").
                        add(((Message) theEvent.getNewValue()).getMessage());
                } else if (theEvent.getNewValue() instanceof LeaderBoard) {
                    allMessages.get("$L").
                        add(((Message) theEvent.getNewValue()).getMessage());
                } else if (theEvent.getNewValue() instanceof LineCrossing) {
                    allMessages.get("$C").
                        add(((Message) theEvent.getNewValue()).getMessage());
                }
            } 
        };
        
        // do this moveTo BEFORE adding the PCL so we don't "see" messages from the moveTo
        myRace.moveTo(0);
        
        myRace.addPropertyChangeListener(pcl);

        myRace.advance(END_OF_RACE + 1);

        myRace.removePropertyChangeListener(pcl); 
        
        assertEquals(52433, allMessages.get("$T").size(), "Incorrect number of Telemetry");
        assertEquals(7, allMessages.get("$L").size(), "Incorrect number of Leaderboard");
        assertEquals(5, allMessages.get("$C").size(), "Incorrect number of Line Crossings");
    }
    
    /**
     * This test case calls moveTo(int) with a negative value expecting an 
     * IllegalArgumentException.
     */
    @Test
    @Order(13)
    void testMoveToNegativeIAE() { 
        assertThrows(IllegalArgumentException.class, 
            () -> myRace.moveTo(-1));
    }
    
    /**
     * This test case calls moveTo(int) with a value larger that the race time expecting an 
     * IllegalArgumentException.
     */
    @Test
    @Order(14)
    void testMoveToPastRaceEndIAE() { 
        assertThrows(IllegalArgumentException.class, 
            () -> myRace.moveTo(END_OF_RACE + 1));
    }
    
    
    /**
     * This test case calls loadRace() with a text file that is not in the correct format 
     * expecting an IOException.
     */
    @Test
    @Order(15)
    void testLoadIOExml() { 
        final File xmlFile =  new File("./support_files/tcss305_checkstyle_sameline.xml");
        assertTrue(xmlFile.exists(), "The file is not found in the project structure");
        assertThrows(IOException.class, 
            () -> myRace.loadRace(xmlFile));
    }
    
    /**
     * This test case calls loadRace() with a binary file expecting an IOException.
     */
    @Test
    @Order(16)
    void testLoadIOEjar() {
        final File jarFile = new File("./track.jar");
        assertTrue(jarFile.exists(), "The file is not found in the project structure");
        assertThrows(IOException.class, 
            () -> myRace.loadRace(jarFile));
    }
    
    /**
     * This test case calls loadRace() with an image file expecting an IOException.
     */
    @Test
    @Order(17)
    void testLoadIOEimage() { 
        final File imageFile = new File("./images/ic_repeat.png");
        assertTrue(imageFile.exists(), "The file is not found in the project structure");
        assertThrows(IOException.class, 
            () -> myRace.loadRace(imageFile));
    }
    
}
