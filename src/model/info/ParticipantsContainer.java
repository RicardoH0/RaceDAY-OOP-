
package model.info;

import java.util.List;

/**
 * Objects that wrap all Participant information need to implement this interface in order for
 * the view classes to know about Participants. Use this when loading the Header information in
 * the race file.
 * 
 * @author Charles Bryan
 * @version Winter 2020
 */
public interface ParticipantsContainer {

    /**
     * @return the list of RaceParticipant objects in this race
     */
    List<RaceParticipant> getParticipants();

}
