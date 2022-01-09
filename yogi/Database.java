package yogi;

import yogi.HighScore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Database {
    int maxScores;
    PreparedStatement insertStatement;
    PreparedStatement deleteStatement;
    Connection connection;

    public Database(int maxScores) throws SQLException {
        if(maxScores == 0){
            this.maxScores=10;
        }else{
             this.maxScores = maxScores;
        }
        String dbURL = "jdbc:derby://localhost:1527/HIGHS;";
        connection = DriverManager.getConnection(dbURL);
        String insertQuery = "INSERT INTO HIGHS ( NAME, SCORE) VALUES (?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        String deleteQuery = "DELETE FROM HIGHS WHERE SCORE=?";
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    public ArrayList<HighScore> getHighScores() throws SQLException {
        String query = "SELECT * FROM HIGHS";
        ArrayList<HighScore> highScores = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("NAME");
            int score = results.getInt("SCORE");
            highScores.add(new HighScore(name, score));
        }
        sortHighScores(highScores);
        return highScores;
    }

    public void putHighScore(String name, int score) throws SQLException {
        ArrayList<HighScore> highScores = getHighScores();
        if (highScores.size() < maxScores) {
            insertScore(name, score);
        } else {
            int leastScore = highScores.get(highScores.size() - 1).getScore();
            if (leastScore < score) {
                deleteScores(leastScore);
                insertScore(name, score);
            }
        }
    }

    /**
     * Sort the high scores in descending order.
     * @param highScores 
     */
    private void sortHighScores(ArrayList<HighScore> highScores) {
        Collections.sort(highScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore t, HighScore t1) {
                return t1.getScore() - t.getScore();
            }
        });
    }

    private void insertScore(String name, int score) throws SQLException {
        //Timestamp ts = new Timestamp(System.currentTimeMillis());
        //insertStatement.setTimestamp(1, ts);
        insertStatement.setString(1, name);
        insertStatement.setInt(2, score);
        insertStatement.executeUpdate();
    }

    private void deleteScores(int score) throws SQLException {
        deleteStatement.setInt(1, score);
        deleteStatement.executeUpdate();
    }
    
        
        public String[][] getDataMatrix () throws SQLException{
        String[][] columnNames = new String[10][3];
        ArrayList<HighScore> highscores = getHighScores();
        int cnt = 0;
        for(HighScore hs : highscores){
            columnNames[cnt][0] = String.valueOf(cnt+1);
            columnNames[cnt][1] = hs.getName();
            columnNames[cnt][2] = String.valueOf(hs.getScore());
            cnt++;
        }
        for(;cnt < 10; cnt++){
            columnNames[cnt][0] = String.valueOf(cnt+1);
            columnNames[cnt][1] = "";
            columnNames[cnt][2] = "";

        }
        return columnNames;
    }
    

    public String[] getColumnNamesArray (){
        String[] columnNames = {"#", "Name", "Baskets collected"};
        return columnNames;
    }
}
