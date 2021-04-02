
package model.messages;

public class LeaderBoardModel extends AbstractMessage implements LeaderBoard {
    
    /**
     * Local Int Array.
     */
    private int[] myLeaderBoard;


    public LeaderBoardModel(final String theMessage) {
        super(theMessage);

        if (theMessage.startsWith("$L")) {
            final String[] str = theMessage.split(":");
            myLeaderBoard = new int[str.length - 2];
            for (int i = 2; i < str.length; i++) {
                final int tempNum = Integer.parseInt(str[i]);
                myLeaderBoard[i - 2] = tempNum;
            }

        }
        
    }

    @Override
    public int[] getLeaderBoard() {

        return myLeaderBoard;
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

}
