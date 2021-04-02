
package model.messages;

/**
 * Interface that defines the behaviors of a Message object.
 * 
 * @author Charles Bryan
 * @version Winter 2020
 */
public interface Message extends Comparable<Message> {

    /**
     * Retrieve the time stamp for this message object.
     * 
     * @return the time stamp for this message object.
     */
    int getTimeStamp();

    /**
     * Retrieve the original-unedited message that this object carries.
     * 
     * @return the message that this object carries.
     */
    String getMessage();

}
