
package model.info;

/**
 * Objects that wrap a race information need to implement this interface in order for the view
 * classes to know about the Race. Use this when loading the Header information in the race
 * file.
 * 
 * @author Charles Bryan
 * @version Winter 2020
 */
public interface RaceInformation extends ParticipantsContainer {

    /**
     * Provides he Total Time of the race.
     * 
     * @return the Total Time of the race
     */
    int getTotalTime();

    /**
     * Provides the length of the track.
     * 
     * @return the length of the track as a distance
     */
    int getTrackDistance();

    /**
     * Provides the name of the race. 
     * 
     * @return the Race Name
     */
    String getRaceName();

    /**
     * Provides the track type. 
     * 
     * @return the Track Type
     */
    String getTrackType();

    /**
     * Provides the height value for the track ratio.
     * 
     * @return the Height portion of the ratio of the track
     */
    int getHeight();

    /**
     * Provides the width value for the track ratio.
     * 
     * @return the Width portion of the ratio of the track
     */
    int getWidth();

}
