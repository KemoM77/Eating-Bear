package yogi;

import java.awt.event.KeyEvent;

public class Yogi extends Sprite{
    private int velx;
    private int vely;
        private final int park_height = 769; 
       private final int park_width = 1600;

    public Yogi(int x, int y) {
        super(x, y, "src/resources/yogi.png");
        
    }

    public void move() {
        this.x += this.velx;
        this.y += this.vely;

        if (this.x < 1 ) {
            this.x = 1;
        }
        if (this.y < 1) {
            this.y = 1;
        }
        if((this.y+this.height) >park_height-60){
             this.y = (park_height-this.height-60);
            
        }

        if (this.x+this.width > park_width-20 ){
            this.x = (park_width-this.width-20);
        }
    }

    public int getDx() {
        return velx;
    }

    public int getDy() {
        return vely;
    }
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            this.velx = -1;
        }
        if (key == KeyEvent.VK_D) {
            this.velx = 1;
        }
        if (key == KeyEvent.VK_W) {
            this.vely = -1;
        }
        if (key == KeyEvent.VK_S) {
            this.vely = 1;
        }
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_D ) {
            this.velx = 0;
        }
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
            this.vely = 0;
        }
       
    }
}
