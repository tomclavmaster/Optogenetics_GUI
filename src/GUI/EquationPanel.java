package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Gui.intensityEquation;


public class EquationPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(intensityEquation, 0, 0, 270, 29, this); // see javadoc for more info on the parameters
    }

}