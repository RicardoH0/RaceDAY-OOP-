package model.messages;

/**
 * Objects that wrap $C (line crossing) information need to implement this interface in order 
 * for the view classes to know about line crossing changes. Use this for $C lines in the 
 * race file.
 * 
 * @author Charles Bryan
 * @version Winter 2020
 */
public interface LineCrossing extends Message {

    /**
     * Get the racer's number that just crossed the finish line. 
     * 
     * @return the racer's number
     */
    int getNumber();
    
    /**
     * Get the racers new lap.
     * 
     * @return the racers new lap
     */
    int getLap();
    
    /**
     * Report on the racer's status in the race. 
     * 
     * @return true if the racer is now finished with the race, false otherwise
     */
    boolean isFinished();
    
}
