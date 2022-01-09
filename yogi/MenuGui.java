package yogi;


import javax.swing.*;
import java.awt.*;

public class MenuGui extends JPanel {
    public MenuGui(LayoutManager layout) {
        super(layout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon ii = new ImageIcon("src/resources/menu.jpg");
        Image background = ii.getImage();
        g.drawImage(background,0,0,null);
    }
}
