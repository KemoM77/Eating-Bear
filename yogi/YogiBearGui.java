package yogi;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class YogiBearGui {
    private final int SCREEN_WIDTH = 1600;
    private final int SCREEN_HEIGHT = 769;

    MenuGui panel = new MenuGui(new GridBagLayout());
   
    private JMenuBar menuBar;
    private JMenu menu;
    
        private JMenuBar menuBar2;
    private JMenu menu2;
    
    private JFrame highscoresFrame;
    public JFrame gameFrame;
    private JFrame menuFrame;
    
    
    Database scoresDb = new Database(0);    
    JButton gameButton = new JButton("New game");
    JButton DbButton = new JButton("Scores");
    
    public YogiBearGui() throws HeadlessException, SQLException, SQLException {  
        this.menuFrame = new JFrame("Main Menu");
        updateHighscoresFrame();
       
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        this.gameButton.addActionListener(new ActionListener(){
             @Override
            public void actionPerformed(ActionEvent e) {
                updateGamesFrame(); 
                gameFrame.setVisible(true);
            }           
        });
        
        this.DbButton.addActionListener(new ActionListener(){
             @Override
            public void actionPerformed(ActionEvent e) {
                if(gameFrame != null)
                gameFrame.dispose();
            try {
                updateHighscoresFrame();
                highscoresFrame.setVisible(true);
            } catch (HeadlessException ex) {
                Logger.getLogger(YogiBearGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            
        });
        
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0,0, 35 , 100);        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(gameButton,gridBagConstraints);        
        gridBagConstraints.gridy = 1;
        panel.add(DbButton,gridBagConstraints);

        menuFrame.add(panel);
        
        
        menuBar2 = new JMenuBar();
        menuFrame.setJMenuBar(menuBar2);
        menu2 = new JMenu("Menu");
        
        
        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               menuFrame.dispose();
               menuFrame.setVisible(true);
            }
        });
        
        menu2.add(restart);
        
                JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               System.exit(0);
            }
        });
        menu2.add(exit);
        menuBar2.add(menu2);
        
        menuFrame.setSize(700,450);
        menuFrame.setResizable(false);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }
    
    private void updateHighscoresFrame(){
        try {
            highscoresFrame = new JFrame("Highscores");             
            ArrayList<yogi.HighScore> result = scoresDb.getHighScores();
            JTable table = new JTable(scoresDb.getDataMatrix(),scoresDb.getColumnNamesArray());
            table.setEnabled(false);
            table.setRowHeight(50);
            JScrollPane sp = new JScrollPane(table);
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(50);
            columnModel.getColumn(1).setPreferredWidth(230);
            columnModel.getColumn(2).setPreferredWidth(120);
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
            cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
            columnModel.getColumn(0).setCellRenderer(cellRenderer);
            columnModel.getColumn(1).setCellRenderer(cellRenderer);
            columnModel.getColumn(2).setCellRenderer(cellRenderer);
            highscoresFrame.add(sp);
          
            highscoresFrame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
            highscoresFrame.setResizable(false);
            highscoresFrame.setLocationRelativeTo(null);
        } catch (SQLException ex) {
            Logger.getLogger(YogiBearGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateGamesFrame(){
        gameFrame = new JFrame("Yogi Bear Game!");
        gameFrame.add(new GameEngine(this));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        menuBar = new JMenuBar();
        gameFrame.setJMenuBar(menuBar);
        menu = new JMenu("Menu");
        
        
        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               gameFrame.dispose();
               updateGamesFrame();
               gameFrame.setVisible(true);
            }
        });
        
        menu.add(restart);
        
        
        JMenuItem main = new JMenuItem("Main Menu");
        main.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               gameFrame.dispose();
               
            }
        });
        menu.add(main);
        menuBar.add(menu);
        
        gameFrame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        try {
            
            new YogiBearGui();
        } catch (HeadlessException ex) {
            Logger.getLogger(YogiBearGui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(YogiBearGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
