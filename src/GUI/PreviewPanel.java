package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Gui.*;

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


        if (num_wells == 24) {
            // This is all the original code:
            for (int i=0; i<6; i+=1) {
                for (int j=0; j<4; j+=1) {
                    if (controlButtons[6*j+i][0].isSelected()) {

                        // Determine whether to black out the circle or show blue illumination:
                        JTextField curr_setting = intensitySettings[6*j+i][0];
                        String text = curr_setting.getText();

                        try {
                            if (text.isEmpty()) {
                                g.setColor(Color.BLACK);
                            } else {
                                float f = Float.valueOf(text.trim()).floatValue();
                                if (f == 0) {
                                    g.setColor(Color.BLACK);
                                } else {
                                    g.setColor(Color.BLUE);
                                }
                            }
                        } catch (NumberFormatException nfe) {
                            System.out.println("Intensity setting not numerical error: " + nfe.getMessage());
                        }

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
        } else {
            for (int i=0; i<12; i+=1) {
                for (int j=0; j<8; j+=1) {
                    if (controlButtons[12*(j/4)+i][0].isSelected()) {

                        // Determine whether to black out the circle or show blue illumination:
                        JTextField curr_setting = intensitySettings[6*(j/4)+i][0];
                        String text = curr_setting.getText();

                        try {
                            if (text.isEmpty()) {
                                g.setColor(Color.BLACK);
                            } else {
                                float f = Float.valueOf(text.trim()).floatValue();
                                if (f == 0) {
                                    g.setColor(Color.BLACK);
                                } else {
                                    g.setColor(Color.BLUE);
                                }
                            }
                        } catch (NumberFormatException nfe) {
                            System.out.println("Intensity setting not numerical error: " + nfe.getMessage());
                        }

                        g.fillOval(getWidth()/12 + i*getWidth()/14, getHeight()/8 + j*getHeight()/10,
                                getWidth()/18, getHeight()/12);


                    } else if (controlButtons[12*(j/4)+i][1].isSelected()) {
                        g2.setPaint(tp);
                        g.fillOval(getWidth()/12 + i*getWidth()/14, getHeight()/8 + j*getHeight()/10,
                                getWidth()/18, getHeight()/12);

                    } else if (controlButtons[12*(j/4)+i][2].isSelected()) {
                        GradientPaint bluetowhite = new GradientPaint(
                                getWidth()/12 + i*getWidth()/14,
                                getHeight()/8 + j*getHeight()/10,
                                Color.BLUE,
                                getWidth()/12 + i*getWidth()/14 + getWidth()/18,
                                getHeight()/8 + j*getHeight()/10 + getHeight()/12,
                                Color.WHITE);

                        g2.setPaint(bluetowhite);
                        g.fillOval(getWidth()/12 + i*getWidth()/14,
                                   getHeight()/8 + j*getHeight()/10,
                                getWidth()/18, getHeight()/12);
                    } else {
                        g.setColor(Color.GRAY);
                        g.fillOval(getWidth()/12 + i*getWidth()/14,
                                   getHeight()/8 + j*getHeight()/10,
                                getWidth()/18, getHeight()/12);
                    }

                }
            }
        }
    }
}