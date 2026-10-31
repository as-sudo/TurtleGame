package p02.pres;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BackgroundPanel extends JPanel {

    private Image backgroundImage;

    public BackgroundPanel() {
        loadBackground();
        if (backgroundImage != null) {
            setPreferredSize(new Dimension(backgroundImage.getWidth(null), backgroundImage.getHeight(null)));
        } else {
            setPreferredSize(new Dimension(600, 400));
        }
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(new File("Z:\\studia\\s2\\p02.game\\graphics\\bg.png"));
        } catch (IOException e) {
            backgroundImage = null;
        }
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

   public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(java.awt.Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}