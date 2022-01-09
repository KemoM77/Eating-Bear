package yogi;

import java.awt.*;
import java.util.Random;
import yogi.Sprite;

public class Ranger extends Sprite{
    private final int hunter_size = 40;
        private final int park_height = 769; 
       private final int park_width = 1600;

    private boolean Direction;
    private boolean changeDirection;
    private  Random rand = new Random();;
    
    public Ranger(int x, int y) {
        super(x, y,"src/resources/ranger.png");
        this.Direction = (0==rand.nextInt(2)) ? false : true; 
        changeDirection=true;
    }

////
    public boolean isChangeDirection() {
        return changeDirection;
    }
    
    public void setChangeDirection(boolean changeDirection) {
        this.changeDirection = changeDirection;
    }

    public void setDirection(boolean Direction) {
        this.Direction = Direction;
    }

    public boolean isInDirectionX() {
        return Direction;
    }
/// 

    public void move(){
        if(this.Direction) {      
            if(this.changeDirection){
                this.x += 1;
                if(this.x+this.width == park_width-20) {
                    this.changeDirection = false;
                            }
            }     
            else{
                this.x -= 1;
                if(this.x==0){
                    this.changeDirection = true;
                }
            }
        }        
        else{
            if(this.changeDirection){
                this.y += 1;
                if(this.y+this.height == park_height-60) {
                    this.changeDirection = false;
                }
            }
            
            else{
                this.y -= 1;
                if(this.y == 0){
                    this.changeDirection = true;
                }
            }
        }
    }
    
    public Rectangle getBound() {
        if(Direction && changeDirection){
            return new Rectangle(this.x, this.y, this.width+hunter_size, this.height);
        }
        if(Direction && !changeDirection) {
            return new Rectangle(this.x-5, this.y, this.width+hunter_size, this.height);
        }
        if(!Direction && changeDirection) {
            return new Rectangle(this.x, this.y, this.width, this.height+hunter_size);
        }
        if(!Direction && !changeDirection) {
            return new Rectangle(this.x, this.y-5, this.width, this.height+hunter_size);
        }else{
             return null;
        }
       
    }
}

