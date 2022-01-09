package yogi;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Sprite {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean visible;
    protected Image image;

    public Sprite(int x, int y, String imageName) {
        this.x = x;
        this.y = y;
        this.visible = true;
        if(imageName != null){
        ImageIcon ii = new ImageIcon(imageName);
        this.image = ii.getImage();
        this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
        }
       
    }

    public void setImage(String imageName) {
       ImageIcon ii = new ImageIcon(imageName);
        this.image = ii.getImage();
          this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Image getImage() {
        return this.image;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }
}