
package model;

import java.beans.PropertyChangeListener;

/**
 * Defines behaviors allowing PropertyChangeListeners to be added or removed from a 
 * RaceControls object. Implementing classes should inform PropertyChangeListeners
 * when methods defined in RaceControls mutate the state of the Race. 
 * 
 * Defines a set of Properties that may be listened too. Implementing class may further define
 * more Properties. 
 * 
 * @author Charles Bryan
 * @version Winter 2020
 *
 */
public interface PropertyChangeEnabledRaceControls extends RaceControls {
   
    
    /** 
     * The property string used for time updates. When a PropetyChangeEvent is fired 
     * using this constant the expected new value is in integer representing the Race Model's
     * current time in milliseconds. 
     */
    String PROPERTY_TIME = "src.model.RaceControls.PROPERTY_TIME";

    /** 
     * The property string used for message updates. When a PropetyChangeEvent is fired 
     * using this constant the expected new value is a model.messages.Message object.
     */
    String PROPERTY_MESSAGE = "src.model.RaceControls.PROPERTY_MESSAGE";

    /** 
     * The property string used for sending textual message updates as strings. When a 
     * PropetyChangeEvent is fired using this constant the expected new value is a String.
     */
    String PROPERTY_STRING_MESSAGE = "src.model.RaceControls.PROPERTY_STRING_MESSAGE";

    /** 
     * The property string used for timer toggle updates. When a PropetyChangeEvent is fired 
     * using this constant the expected new value is a boolean (true if the race model has 
     * more time, false if the race is over)
     */
    String PROPERTY_RACE_STATUS = "src.model.RaceControls.PROPERTY_RACE_STATUS";

    /** 
     * The property string used for race information updates. When a PropetyChangeEvent is 
     * fired using this constant the expected new value is a model.info.RaceInformation object.
     * */
    String PROPERTY_RACE_INFORMATION = "src.model.RaceControls.PROPERTY_RACE_INFORMATION";

       
    
    /**
     * Add a PropertyChangeListener to the listener list. The listener is registered for 
     * all properties. The same listener object may be added more than once, and will be 
     * called as many times as it is added. If listener is null, no exception is thrown and 
     * no action is taken.
     * 
     * @param theListener The PropertyChangeListener to be added
     */
    void addPropertyChangeListener(PropertyChangeListener theListener);
    
    
    /**
     * Add a PropertyChangeListener for a specific property. The listener will be invoked only 
     * when a call on firePropertyChange names that specific property. The same listener object
     * may be added more than once. For each property, the listener will be invoked the number 
     * of times it was added for that property. If propertyName or listener is null, no 
     * exception is thrown and no action is taken.
     * 
     * @param thePropertyName The name of the property to listen on.
     * @param theListener The PropertyChangeListener to be added
     */
    void addPropertyChangeListener(String thePropertyName, PropertyChangeListener theListener);

    /**
     * Remove a PropertyChangeListener from the listener list. This removes a 
     * PropertyChangeListener that was registered for all properties. If listener was added 
     * more than once to the same event source, it will be notified one less time after being 
     * removed. If listener is null, or was never added, no exception is thrown and no action 
     * is taken.
     * 
     * @param theListener The PropertyChangeListener to be removed
     */
    void removePropertyChangeListener(PropertyChangeListener theListener);
    
    /**
     * Remove a PropertyChangeListener for a specific property. If listener was added more than
     * once to the same event source for the specified property, it will be notified one less 
     * time after being removed. If propertyName is null, no exception is thrown and no action 
     * is taken. If listener is null, or was never added for the specified property, no 
     * exception is thrown and no action is taken.
     * 
     * @param thePropertyName The name of the property that was listened on.
     * @param theListener The PropertyChangeListener to be removed
     */
    void removePropertyChangeListener(String thePropertyName, 
                                      PropertyChangeListener theListener);
}
