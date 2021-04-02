package model.messages;

public class TelemetryModel extends AbstractMessage implements Telemetry {

    /**
     * Local Int.
     */
    private int myNumber;
    
    /**
     * Local Double.
     */
    private Double myDistance;
    
    /**
     * Local Int.
     */
    private int myLap;
    
    public TelemetryModel(final String theMessage) {
        super(theMessage);
        if (theMessage.startsWith("$T")) {
            final String [] str = theMessage.split(":");
            myNumber = Integer.parseInt(str[2]);
            myLap = Integer.parseInt(str[4]);
            myDistance = Double.parseDouble(str[3]);
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
    public double getDistance() {
        
        return myDistance;
    }

    @Override
    public int getLap() {
        
        return myLap;
    }

}
