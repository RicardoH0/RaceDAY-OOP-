
package model.info;

public class RaceParticipantModel implements RaceParticipant {
    /**
     * 
     */
    private final String myName;
    /**
     * 
     */
    private final int myNumber;
    /**
     * 
     */
    private final int myDistance;

    // private final List<String> myTemList;

    public RaceParticipantModel(final String theName, final int theNumber,
                                final int theDistance) {
        myName = theName;
        myNumber = theNumber;
        myDistance = theDistance;

    }

    @Override
    public String getName() {

        return myName;
    }

    @Override
    public int getNumber() {
        // TODO Auto-generated method stub
        return myNumber;
    }

    @Override
    public int getStartingDistance() {
        // TODO Auto-generated method stub
        return myDistance;
    }

}
