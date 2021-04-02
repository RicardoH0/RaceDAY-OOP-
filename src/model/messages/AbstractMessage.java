
package model.messages;

public abstract class AbstractMessage implements Message {
    /**
     * 
     */
    private int myTime;
    /**
     * 
     */
    private String myMessage = "";



    protected AbstractMessage(final String theMessage) {
        final String [] str = theMessage.split(":");
        myTime = Integer.parseInt(str[1]);
        myMessage = theMessage;

    }

    /**
     * Retrieve the time stamp for this message object.
     * 
     * @return the time stamp for this message object.
     */
    public int getTimeStamp() {
        return myTime;
    }

    /**
     * Retrieve the original-unedited message that this object carries.
     * 
     * @return the message that this object carries.
     */
    public String getMessage() {
        return myMessage;
    }

}

//myRaceMessage = theRaceMessage;
//for (int t = 0; t < myRaceMessage.size(); t++) {
//
//  myTime = Integer.parseInt(myRaceMessage.get(t).get(1));
//  final StringBuilder sb = new StringBuilder();
//  sb.append(myRaceMessage.get(t).get(2));
//  sb.append(myRaceMessage.get(t).get(3));
//  sb.append(myRaceMessage.get(t).get(4));
//  myMessage = sb.toString();
//
//}int timeTo =myCT +theMillisecind){
// if(time >= myMBS.size) {
//     timeto =mMBS.size
// }
// while Currenttime >= MBS.size{
//     for()
//     
//     mypcs.faire(PRS,null, false);
// } else {
//     setCurrentTime(myCurrentTime);
// }


//i--
//(final Messafe m : foundRacer.values()){
    //MPCS.fira(P_MESSAGE, null,m);
//}