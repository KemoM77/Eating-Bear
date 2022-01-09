package yogi;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine extends JPanel implements ActionListener {

    private int score ;
    private int level ;
    private int yogiLife ;
    private int hunterNumber ;
    private int terrainNumber ;
    private int basketNumber ;

    private Yogi yogibear;
    private List<Basket> baskets;
    private List<Ranger> rangers;
    private List<Obstacle> Obstacles;
    
    private Timer timer;
    private YogiBearGui mainframe;

    private boolean alive;

    private final int Frame_WIDTH = 1600;
    private final int Frame_HEIGHT = 769;    

    private final int YogiXPos = 0;
    private final int YogiYPos = 358;
    private final int DELAY = 10;

    private final Rectangle startZone = new Rectangle(YogiXPos,YogiYPos,60,75);

    //Clock
    private long tStart;
    private long tCurrent;
   
    public GameEngine(YogiBearGui f) {
        mainframe = f;
        score = 0;
        level = 1;
        yogiLife = 1;
        hunterNumber = 1;
        terrainNumber = 1;
        basketNumber = 1;
        alive = true;
  
        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(Frame_WIDTH, Frame_HEIGHT));

        yogibear = new Yogi(YogiXPos, YogiYPos);
        initTerrains();
        initBaskets();
        initHunters();

        tStart = System.currentTimeMillis();
        tCurrent = System.currentTimeMillis();

        timer = new Timer(DELAY, this);
        timer.start();
    }

     private void refresh(){
         if(level ==10){
              alive = false;
              
               String name = JOptionPane.showInputDialog(this, "Enter your name: ", "You win", JOptionPane.INFORMATION_MESSAGE);
                if(name != null)  {                   
                  try {
                      new yogi.Database(0).putHighScore(name, score);
                  } catch (SQLException ex) {
                      java.util.logging.Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                  }
                }
                 int option = JOptionPane.showConfirmDialog(this, "Start again?", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                     mainframe.gameFrame.dispose();
               mainframe.updateGamesFrame();
               mainframe.gameFrame.setVisible(true);
                    //System.out.println("dkak");
                }
                else
                 System.exit(0);
              
                
         }else{
                     
        terrainNumber += 2;
        hunterNumber +=1;
        basketNumber += 2;
        yogibear = new Yogi(YogiXPos, YogiYPos);
        initTerrains();
        initBaskets();
        initHunters();
        level++;
         }
    }
    
    private void updateYogi() {
        if(yogibear.isVisible()){
            yogibear.move();
        }
    }
     
    private void initBaskets(){
        baskets = new ArrayList<>();
        for (int i = 0; i < basketNumber; i++){
            Random random = new Random();
            boolean isDone = false;
            while (!isDone){
                Basket tmpBasket = new Basket(random.nextInt(Frame_WIDTH - 130)+40, random.nextInt(Frame_HEIGHT - 150)+40);
                Rectangle rTmpBasket = tmpBasket.getBounds();
                boolean success = true;
                int j = 0;
                while (j < Obstacles.size() && success){
                    Obstacle terrainBlock = Obstacles.get(j);
                    Rectangle rTerrain = terrainBlock.getBounds();
                    if ((rTerrain.contains(rTmpBasket)) || (rTerrain.intersects(rTmpBasket))){
                        success = false;
                    }
                    j++;
                }
                if (success){
                    baskets.add(tmpBasket);
                    isDone = true;
                }
            }
        }
    }
    
    private void updateBaskets() {
        if(baskets.isEmpty()){
            refresh();
        }else{
            for (int i = 0; i < baskets.size(); i++){
                Basket b = baskets.get(i);
                if(!b.isVisible()){
                    baskets.remove(i);
                }
            }
        }
    }
   
    private void initHunters(){
        rangers = new ArrayList<>();
        for (int i = 0; i < hunterNumber; i++){
            Random random = new Random();
            boolean isDone = false;
            while (!isDone){
                Ranger tmpHunter = new Ranger(random.nextInt(Frame_WIDTH - 130)+40, random.nextInt(Frame_HEIGHT - 150)+40);
                Rectangle rTmpHunter = tmpHunter.getBounds();
                boolean success = true;
                int j = 0;
                while (j < Obstacles.size() && success){
                    Obstacle terrainBlock = Obstacles.get(j);
                    Rectangle rTerrain = terrainBlock.getBounds();
                    if ((rTerrain.contains(rTmpHunter)) || (rTerrain.intersects(rTmpHunter)) || startZone.contains(rTmpHunter) || startZone.intersects(rTmpHunter)){
                        success = false;
                    }
                    j++;
                }
                if (success){
                    rangers.add(tmpHunter);
                    isDone = true;
                }
            }

        }
    }
     
    private void updateHunters() {
        for (Ranger hunter : rangers){
            hunter.move();
        }
    }
    
    private void initTerrains(){
        Obstacles = new ArrayList<>();
        for (int i = 0; i < terrainNumber; i++){
            Random random = new Random();
            if (i == 0) {
                Obstacles.add(new Obstacle(random.nextInt(Frame_WIDTH - 130)+40, random.nextInt(Frame_HEIGHT - 150)+40));
            }else{
                boolean isDone = false;
                boolean isPossible = true;
                int counterPossible = 0;
                while (!isDone && isPossible ){
                    Obstacle tmpTerrain = new Obstacle(random.nextInt(Frame_WIDTH - 130)+40, random.nextInt(Frame_HEIGHT - 150)+40);
                    Rectangle rTmpTerrain = tmpTerrain.getBound(0);
                    boolean success = true;
                    int j = 0;
                    while (j < Obstacles.size() && success){
                        Obstacle terrainBlock = Obstacles.get(j);
                        Rectangle rTerrain = terrainBlock.getBound(0);
                        if ((rTerrain.contains(rTmpTerrain)) || (rTerrain.intersects(rTmpTerrain)) || rTmpTerrain.contains(startZone) || rTmpTerrain.intersects(startZone)){
                            success = false;
                        }
                        j++;
                    }
                    if (success){
                        Obstacles.add(tmpTerrain);
                        isDone = true;
                    }
                    counterPossible++;
                    if(counterPossible > 500){
                        isPossible = false;
                    }
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon tmp = new ImageIcon("src/resources/back.jpg");
        Image background = tmp.getImage();
        g.drawImage(background,0,0,null);
        
                if (alive) {
            drawObjects(g);
        } else {
             String name = JOptionPane.showInputDialog(this, "Enter your name: ", "You couldn't escape...", JOptionPane.INFORMATION_MESSAGE);
                if(name != null)  {                   
                 try {

                        new yogi.Database(0).putHighScore(name, score);                     
                     
                 } catch (SQLException ex) {
                     java.util.logging.Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                 }
                }
                 int option = JOptionPane.showConfirmDialog(this, "Start again?", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                     mainframe.gameFrame.dispose();
               mainframe.updateGamesFrame();
               mainframe.gameFrame.setVisible(true);
                    //System.out.println("dkak");
                }
                else
                    
             System.exit(0);
        }
       
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
        //g.setColor(Color.RED);
        //g.drawRect(startZone.x,startZone.y,startZone.width,startZone.height);
        if (yogibear.isVisible()) {
            g.drawImage(yogibear.getImage(), yogibear.getX(), yogibear.getY(), this);
        }

        for (Basket basket : baskets) {
            if (basket.isVisible()) {
                g.drawImage(basket.getImage(), basket.getX(), basket.getY(), this);
            }
        }

        for (Ranger hunter : rangers) {
            if (hunter.isVisible()) {
                g.drawImage(hunter.getImage(), hunter.getX(), hunter.getY(), this);
            }
        }

        for (Obstacle terrain : Obstacles) {
            if (terrain.isVisible()) {
                g.drawImage(terrain.getImage(), terrain.getX(), terrain.getY(), this);
            }
        }

        tCurrent = System.currentTimeMillis();

        g.setColor(Color.BLACK);

        Font small = new Font("Helvetica", Font.BOLD, 16);
        g.drawString("Lives: " + yogiLife, 10, 15);
        g.drawString("Collected: " + score, 400, 15);
        g.drawString("Level: " + level, 970, 15);
        g.drawString("Time: "+(int) ((tCurrent-tStart)/1000) ,1400,15);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        inGame();
        updateYogi();
        updateBaskets();
        updateHunters();

        try {
            checkCollisions();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        repaint();
    }
    
    private void inGame() {
        if (!alive) {
            timer.stop();
        }
    }

    private void checkCollisions() throws SQLException {
        Rectangle ryogi = yogibear.getBounds();
        for (int i = 0; i < baskets.size(); i++){
            Basket b = baskets.get(i);
            Rectangle rb = b.getBounds();
            if(ryogi.intersects(rb)){
                b.setVisible(false);

                this.score += 1;
            }
        }
        for(Ranger hunter : rangers){
            Rectangle rh = hunter.getBounds();
            Rectangle rh_sight = hunter.getBound();
            if(startZone.intersects(rh) || startZone.contains(rh) || rh.contains(startZone) || rh.intersects(startZone)){
                if (hunter.isChangeDirection() && hunter.isInDirectionX()){
                    hunter.setX(hunter.getX()-1);
                    hunter.setChangeDirection(false);
                }
                else if (!hunter.isChangeDirection() && hunter.isInDirectionX()){
                    hunter.setX(hunter.getX()+1);
                    hunter.setChangeDirection(true);
                }
                else if (hunter.isChangeDirection() && !hunter.isInDirectionX()){
                    hunter.setY(hunter.getY()-1);
                    hunter.setChangeDirection(false);
                }
                else if (!hunter.isChangeDirection() && !hunter.isInDirectionX()){
                    hunter.setY(hunter.getY()+1);
                    hunter.setChangeDirection(true);
                }
            }
           
            
            if (ryogi.intersects(rh_sight) && !startZone.contains(ryogi)){
                yogiLife -= 1;
                yogibear = new Yogi(YogiXPos, YogiYPos);              
                 if (yogiLife == 0){
                    alive = false;
                      String name = JOptionPane.showInputDialog(this, "Enter your name: ", "You couldn't escape...", JOptionPane.INFORMATION_MESSAGE);
                if(name != null)  {                   
               new yogi.Database(0).putHighScore(name, score);      
                }
                 int option = JOptionPane.showConfirmDialog(this, "Start again?", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                     mainframe.gameFrame.dispose();
               mainframe.updateGamesFrame();
               mainframe.gameFrame.setVisible(true);
                    //System.out.println("dkak");
                }
                else
             System.exit(0);
                }

            }
            for(Obstacle terrain : Obstacles){
                Rectangle rt = terrain.getBounds();
                if(rh.intersects(rt)){
                    if (hunter.isChangeDirection() && hunter.isInDirectionX()){
                        hunter.setX(hunter.getX()-1);
                        hunter.setChangeDirection(false);
                    }
                    else if (!hunter.isChangeDirection() && hunter.isInDirectionX()){
                        hunter.setX(hunter.getX()+1);
                        hunter.setChangeDirection(true);
                    }
                    else if (hunter.isChangeDirection() && !hunter.isInDirectionX()){
                        hunter.setY(hunter.getY()-1);
                        hunter.setChangeDirection(false);
                    }
                    else if (!hunter.isChangeDirection() && !hunter.isInDirectionX()){
                        hunter.setY(hunter.getY()+1);
                        hunter.setChangeDirection(true);
                    }
                }
            }
        }
        for(Obstacle terrain : Obstacles){
            Rectangle rt = terrain.getBounds();
            if (ryogi.intersects(rt)){
                if (yogibear.getDx() > 0){
                    yogibear.setX(yogibear.getX()-2);
                }
                if (yogibear.getDx() < 0){
                    yogibear.setX(yogibear.getX()+2);
                }
                if (yogibear.getDy() > 0){
                    yogibear.setY(yogibear.getY()-2);
                }
                if (yogibear.getDy() < 0){
                    yogibear.setY(yogibear.getY()+2);
                }
            }
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            yogibear.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            yogibear.keyPressed(e);
        }
    }
}
