
package model.messages;

/**
 * Objects that wrap $T (telemetry) information need to implement this interface in order for
 * the view classes to know about telemetry changes. Use this for $T lines in the race file.
 * 
 * @author Charles Bryan
 * @version Winter 2020
 */
public interface Telemetry extends Message {

    /**
     * Get the racer's number.
     * 
     * @return the racer's number
     */
    int getNumber();

    /**
     * Get current the racer's distance around the track.
     * 
     * @return the racer's distance around the track
     */
    double getDistance();

    /**
     * Get the racers current lap.
     * 
     * @return the racers current lap
     */
    int getLap();

}
