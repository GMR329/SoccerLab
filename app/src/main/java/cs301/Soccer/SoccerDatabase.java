package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database = new Hashtable();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {

        boolean isSuccessful = false;

        String fullName = firstName + " ## " + lastName;

        if(database.containsKey(fullName) || firstName.equals("") || lastName.equals("") ||
                uniformNumber == 0 || teamName.equals("")){
            isSuccessful = false;
        }else{
            //instantiate new soccer player
            SoccerPlayer toAdd = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);

            //add the soccer player to the database with fullName key
            database.put(fullName, toAdd);
            isSuccessful = true;
        }

        return isSuccessful;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        boolean isSuccessful = false;

        String fullNameKey = firstName + " ## " + lastName;

        if(database.containsKey(fullNameKey)){
            database.remove(fullNameKey);
            isSuccessful = true;
        }else{
            isSuccessful = false;
        }

        return isSuccessful;
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        SoccerPlayer retrievePlayer;

        String fullNameKey = firstName + " ## " + lastName;

        if(database.containsKey(fullNameKey)){
            retrievePlayer = database.get(fullNameKey);
        }else{
            retrievePlayer = null;
        }

        return retrievePlayer;
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        boolean isSuccessful = false;

        String fullNameKey = firstName + " ## " + lastName;

        if(database.containsKey(fullNameKey)){
            SoccerPlayer selected = database.get(fullNameKey);
            selected.bumpGoals();
            isSuccessful = true;
        }else{
            isSuccessful = false;
        }

        return isSuccessful;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        boolean isSuccessful = false;

        String fullNameKey = firstName + " ## " + lastName;

        if(database.containsKey(fullNameKey)){
            SoccerPlayer selected = database.get(fullNameKey);
            selected.bumpYellowCards();
            isSuccessful = true;
        }else{
            isSuccessful = false;
        }

        return isSuccessful;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        boolean isSuccessful = false;

        String fullNameKey = firstName + " ## " + lastName;

        if(database.containsKey(fullNameKey)){
            SoccerPlayer selected = database.get(fullNameKey);
            selected.bumpRedCards();
            isSuccessful = true;
        }else{
            isSuccessful = false;
        }

        return isSuccessful;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        int toReturn = -1;

        if(teamName == null){
            toReturn = database.size();
        }else{
            toReturn = 0;
            for(SoccerPlayer curr : database.values()){
                if(curr.getTeamName().equals(teamName)){
                    toReturn++;
                }
            }
        }

        return toReturn;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        SoccerPlayer toReturn = null;

        int increment = -1;

        if(idx > numPlayers(teamName)){
            toReturn = null;
        }else{
            for(SoccerPlayer curr : database.values()){
                //if the team name is empty, return
                if(teamName == null){
                    increment++;
                    if(increment == idx) return curr;
                }else{
                    if(curr.getTeamName().equals(teamName)){
                        increment++;
                        if(increment == idx) return curr;
                    }
                }
            }//iterate over soccer players
        }

        return toReturn;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        Scanner fileIn;

        if(file == null){
            return false;
        }

        try{
            fileIn = new Scanner(file);
        }catch(Exception ex){
            ex.printStackTrace();
            return file.exists();
        }

        while(fileIn.hasNext()){
            String firstN = fileIn.nextLine();
            String lastN = fileIn.nextLine();
            String teamN = fileIn.nextLine();
            int uniNum = Integer.parseInt(fileIn.nextLine());

            SoccerPlayer player = new SoccerPlayer(firstN, lastN, uniNum, teamN);

            int goals = Integer.parseInt(fileIn.nextLine());
            int yellows = Integer.parseInt(fileIn.nextLine());
            int reds = Integer.parseInt(fileIn.nextLine());

            for(int i = 0; i < goals; i++){
                player.bumpGoals();
            }

            for(int i = 0; i < yellows; i++){
                player.bumpYellowCards();
            }

            for(int i = 0; i < reds; i++){
                player.bumpRedCards();
            }

            String key = firstN + " ## " + lastN;

            //clear old entry
            if(database.containsKey(key)){
                database.remove(key);
            }

            database.put(key, player);
        }

        fileIn.close();

        return file.exists();
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(file);
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }

        for(SoccerPlayer play : database.values()){
            pw.println(logString(play.getFirstName()));
            pw.println(logString(play.getLastName()));
            pw.println(logString(play.getTeamName()));
            pw.println(logString(Integer.toString(play.getUniform())));
            pw.println(logString(Integer.toString(play.getGoals())));
            pw.println(logString(Integer.toString(play.getYellowCards())));
            pw.println(logString(Integer.toString(play.getRedCards())));
        }

        pw.close();

        return true;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return new HashSet<String>();
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
