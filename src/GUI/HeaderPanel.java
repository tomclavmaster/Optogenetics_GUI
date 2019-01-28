package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Gui.headerPanels;


public class HeaderPanel extends JPanel {

    HeaderPanel(int _panelIndex) {
        this.panelIndex = _panelIndex;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        double scale_factor = 2.1;
        g.drawImage(headerPanels[panelIndex], 5, 0, (int) (headerPanels[panelIndex].getWidth() / scale_factor),
                                                           (int) (headerPanels[panelIndex].getHeight() / scale_factor), this); // see javadoc for more info on the parameters

    }

    private int panelIndex;

}