package model.messages;

public class LineCrossingModel extends AbstractMessage implements LineCrossing {

    /**
     * Local Int
     */
    private int myNumber;
    /**
     * Local Int.
     */
    private int myLap;
    /**
     * Local Boolean.
     */
    private boolean myResult;


    public LineCrossingModel(final String theMessage) {
        super(theMessage);
        myResult = false;
        if (theMessage.startsWith("$C")) {
            final String [] str = theMessage.split(":");
            myNumber = Integer.parseInt(str[2]);
            myLap = Integer.parseInt(str[3]);
            myResult = Boolean.parseBoolean(str[4]);
           // System.out.println(myNumber);
            
        }
       
    }

    @Override
    public int compareTo(final Message theMess) {
        int result = 0;
        if (theMess.getTimeStamp() == this.getTimeStamp()) {
            result = 0;
        } else if (theMess.getTimeStamp() < this.getTimeStamp()) {
            result = -1;
        } else if (theMess.getTimeStamp() < this.getTimeStamp()) {
            result = 1;
        }
        return result;
    }

    @Override
    public int getNumber() {
        
        return myNumber;
    }

    @Override
    public int getLap() {
        
        return myLap;
    }

    @Override
    public boolean isFinished() {
        
        return myResult;
    }

}
