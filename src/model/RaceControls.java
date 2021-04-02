package model;

import java.io.File;
import java.io.IOException;

/**
 * Define the actions that may be performed on a Race object. 
 * 
 * @author Charles Bryan
 * @version Fall 2018
 */
public interface RaceControls {

    /**
     * Advances the race's internal "clock" by 1 millisecond. All registered listeners will be 
     * notified of both the "time" change and any messages that occur during this advance. If 
     * the race is advanced beyond its total amount of time, it will not throw an exception but
     * will notify all registered listeners no time remains.
     */
    void advance();
    
    /**
     * Advances the race's internal "clock" by theMilliseconds milliseconds.  All registered 
     * listeners will be notified of both the "time" change and any messages that occur during 
     * this advance. If the race is advanced beyond its total amount of time, it will not throw
     * an exception but will notify all registered listeners no time remains. 
     * 
     * @param theMillisecond the amount of milliseconds to advance the race
     */
    void advance(int theMillisecond);
    
    /**
     * Move the Race's internal "clock" to theMilliseconds milliseconds after the start of the 
     * race. All registered listeners will be notified of the "time" change. Starting at 
     * theMilliseconds and working backward in "time," all registered listeners will be 
     * notified of the new most recent leaderboard and telemetry message (for all racers).  
     * 
     * @param theMillisecond the time to move the race's internal "clock" to
     * @throws IllegalArgumentException when theMillisecond is negative or greater
     * than the length of the race. 
     */
    void moveTo(int theMillisecond);
        
    /**
     * Load a file containing race information. All registered listeners will be notified of 
     * progress updates during the load. All registered listeners will be notified of 
     * information in the header message when the loading process completes. 
     * 
     * @param theRaceFile the file to load.
     * @throws IOException when the file is not in the appropriate format
     */
    void loadRace(File theRaceFile) throws IOException;
    
}
