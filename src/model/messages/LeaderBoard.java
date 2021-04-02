
package model.messages;

/**
 * Objects that wrap $L (leader board) information need to implement this interface in order
 * for the view classes to know about leaderboard changes. Use this for $L lines in the race
 * file.
 * 
 * @author Charles Bryan
 * @version Winter 2020
 */
public interface LeaderBoard extends Message {

    /**
     * Retrieve the $L leaderboard information. Map: $L:timestamp:r1:r2:...:rn to int[] 
     * index 0 -> r1, index 1 ->r2, index n-1 -> rn
     * 
     * @return the LeaderBoard as an array of int
     */
    int[] getLeaderBoard();

}
