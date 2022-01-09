package yogi;

import java.awt.*;
import java.util.Random;
import yogi.Sprite;

public class Obstacle extends Sprite {
    public Obstacle(int x, int y) {
        super(x, y,null);
        Random rand = new Random();
        setImage(rand.nextInt(2)==0?"src/resources/terrain_block.png":"src/resources/obstacle.png");
 
    }

    public Rectangle getBound(int i) {
        if (i == 0) return new Rectangle(this.x+1000, this.y, this.width+45, this.height+45);
        return null;
    }
}
