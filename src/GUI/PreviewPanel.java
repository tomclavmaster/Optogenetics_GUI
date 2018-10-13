package GUI;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

import static GUI.Gui.controlButtons;
import static GUI.Gui.stripes;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class PreviewPanel extends JPanel {

    PreviewPanel() {
        setPreferredSize(new Dimension(300, 200));
        setMaximumSize( getPreferredSize() );
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(0, 100, 255));

        Graphics2D g2 = (Graphics2D) g;
        TexturePaint tp = new TexturePaint(stripes, new Rectangle(0, 0, 3, 3));


        for (int i=0; i<6; i+=1) {
            for (int j=0; j<4; j+=1) {
                if (controlButtons[6*j+i][0].isSelected()) {
                    g.setColor(Color.BLUE);

                    g.fillOval(getWidth()/12 + i*getWidth()/7, getHeight()/8 + j*getHeight()/5,
                            getWidth()/9, getHeight()/6);

                } else if (controlButtons[6*j+i][1].isSelected()) {
                    g2.setPaint(tp);
                    g.fillOval(getWidth()/12 + i*getWidth()/7, getHeight()/8 + j*getHeight()/5,
                            getWidth()/9, getHeight()/6);

                } else if (controlButtons[6*j+i][2].isSelected()) {
                    GradientPaint bluetowhite = new GradientPaint(
                            getWidth()/12 + i*getWidth()/7,
                            getHeight()/8 + j*getHeight()/5,
                            Color.BLUE,
                            getWidth()/12 + i*getWidth()/7 + getWidth()/9,
                            getHeight()/8 + j*getHeight()/5 + getHeight()/6,
                            Color.WHITE);

                    g2.setPaint(bluetowhite);
                    g2.fillOval(getWidth()/12 + i*getWidth()/7, getHeight()/8 + j*getHeight()/5,
                            getWidth()/9, getHeight()/6);
                } else {
                    g.setColor(Color.GRAY);
                    g.fillOval(getWidth()/12 + i*getWidth()/7, getHeight()/8 + j*getHeight()/5,
                            getWidth()/9, getHeight()/6);
                }

            }
        }




    }


    static Graphics[] circlePreviews = new Graphics[24];

}