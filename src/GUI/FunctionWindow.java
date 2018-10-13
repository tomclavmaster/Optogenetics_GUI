package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class FunctionWindow extends JFrame implements ActionListener {
    FunctionWindow(String _channel) {
        channel = _channel;
        initMainWindow();
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    private void initMainWindow() {
        UIManager.put( "control" , Color.WHITE);

        // Main GUI window initialization:
        frame = new JFrame("Channel " + channel + " Function");
        frame.setSize(800, 500);
        frame.setResizable(false);

        // Add all panels to main panel:
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.add(mainPanel);

        // Add all of the function option panels:
        allBlocks = new JPanel();
        allBlocks.setLayout(new BoxLayout(allBlocks, BoxLayout.Y_AXIS));

        addPanel = createAddBlock();
        refreshBlocks();
    }

    private JPanel createAddBlock() {
        // Add linear side:
        linearPanel = new JPanel();
        TitledBorder bordLinearBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1, true),
                "New Linear Function: [mx+b]", TitledBorder.LEFT, TitledBorder.TOP);
        linearPanel.setBorder(bordLinearBorder);

        linearPanel.add(new JLabel("m:"));
        linearPanel.add(new JTextField("", 5));
        linearPanel.add(new JLabel("b:"));
        linearPanel.add(new JTextField("", 5));
        linearPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        linearPanel.add(new JLabel("t(seconds) <="));
        linearPanel.add(new JTextField("", 7));
        linearPanel.add(Box.createRigidArea(new Dimension(20, 0)));


        JButton addLinearButton = new JButton("Add Linear");
        addLinearButton.addActionListener(this);
        addLinearButton.setActionCommand("add_linear_block");
        linearPanel.add(addLinearButton);


        // Add sine side:
        sinePanel = new JPanel();
        TitledBorder bordSineBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1, true),
                "New Sinusoidal Function: [Asin(Bt)+C]", TitledBorder.LEFT, TitledBorder.TOP);
        sinePanel.setBorder(bordSineBorder);

        sinePanel.add(new JLabel("A:"));
        sinePanel.add(new JTextField("", 5));
        sinePanel.add(new JLabel("B:"));
        sinePanel.add(new JTextField("", 5));
        sinePanel.add(new JLabel("C:"));
        sinePanel.add(new JTextField("", 5));
        sinePanel.add(Box.createRigidArea(new Dimension(35, 0)));
//        sinePanel.add(new JTextField("", 7));
        sinePanel.add(new JLabel("t(seconds) <="));
        sinePanel.add(new JTextField("", 7));

        JButton addSineButton = new JButton("Add Sine");
        addSineButton.addActionListener(this);
        addSineButton.setActionCommand("add_sine_block");
        sinePanel.add(addSineButton);


        // Add the remove button - removes the previous entry unless there are zero entries left.
        JButton removeButton = new JButton("Remove Previous");
        removeButton.addActionListener(this);
        removeButton.setActionCommand("remove_prev_block");


        // Encapsulate linear and sine in a JSplitPane:
        JSplitPane blockPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, linearPanel, sinePanel);
        Dimension blockPanelDim = new Dimension(frame.getWidth()-70, 130);
        blockPanel.setMaximumSize(blockPanelDim);
        blockPanel.setMinimumSize(blockPanelDim);
        blockPanel.setPreferredSize(blockPanelDim);
        blockPanel.setResizeWeight(0.5);
        blockPanel.setEnabled(false);

        JPanel blockPanelContainer = new JPanel();
        blockPanelContainer.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        Dimension blockPanelContainerDim = new Dimension(frame.getWidth()-20, 170);
        blockPanelContainer.setMaximumSize(blockPanelContainerDim);
        blockPanelContainer.setMinimumSize(blockPanelContainerDim);
        blockPanelContainer.setPreferredSize(blockPanelContainerDim);
        blockPanelContainer.add(blockPanel);
        blockPanelContainer.add(removeButton);

        return blockPanelContainer;
    }

    private JPanel createLinearBlock(String m, String b, String t_start, String t_end) {
        JPanel currLinear = new JPanel();
        currLinear.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        Dimension blockDim = new Dimension(frame.getWidth()-20, 40);
        currLinear.setMaximumSize(blockDim);
        currLinear.setMinimumSize(blockDim);
        currLinear.setPreferredSize(blockDim);

        currLinear.add(new JLabel("m:"));
        JTextField mField = new JTextField(m, 5);
        mField.setEnabled(false);
        currLinear.add(mField);

        currLinear.add(new JLabel("b:"));
        JTextField bField = new JTextField(b, 5);
        bField.setEnabled(false);
        currLinear.add(bField);

        currLinear.add(Box.createRigidArea(new Dimension(5, 0)));

        JTextField tStartField = new JTextField(t_start, 5);
        tStartField.setEnabled(false);
        currLinear.add(tStartField);
        currLinear.add(new JLabel("t(seconds) <="));
        JTextField tEndField = new JTextField(t_end, 5);
        tEndField.setEnabled(false);
        currLinear.add(tEndField);

        String[] currSettings = new String[] {m, b, t_start, t_end};
        allFuncSettings.add(currSettings);

        return currLinear;
    }

    private JPanel createSineBlock(String a, String b, String c, String t_start, String t_end) {
        JPanel currSine = new JPanel();
        currSine.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        Dimension blockDim = new Dimension(frame.getWidth()-20, 40);
        currSine.setMaximumSize(blockDim);
        currSine.setMinimumSize(blockDim);
        currSine.setPreferredSize(blockDim);

        currSine.add(new JLabel("A:"));
        JTextField aField = new JTextField(a, 5);
        aField.setEnabled(false);
        currSine.add(aField);

        currSine.add(new JLabel("B:"));
        JTextField bField = new JTextField(b, 5);
        bField.setEnabled(false);
        currSine.add(bField);

        currSine.add(new JLabel("C:"));
        JTextField cField = new JTextField(c, 5);
        cField.setEnabled(false);
        currSine.add(cField);

        currSine.add(Box.createRigidArea(new Dimension(20, 0)));
        JTextField tStartField = new JTextField(t_start, 5);
        tStartField.setEnabled(false);
        currSine.add(tStartField);

        currSine.add(new JLabel("< t(seconds) <="));
        JTextField tEndField = new JTextField(t_end, 5);
        tEndField.setEnabled(false);
        currSine.add(tEndField);

        String[] currSettings = new String[] {a, b, c, t_start, t_end};
        allFuncSettings.add(currSettings);

        return currSine;
    }

    private void refreshBlocks() {
        JScrollPane scrollPanel = new JScrollPane(allBlocks, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        allBlocks.removeAll();
        mainPanel.removeAll();

        for (BlockContainer currContainer : blockArray) {
            allBlocks.add(currContainer.block);
            allBlocks.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        allBlocks.add(addPanel);
        mainPanel.add(scrollPanel);
        frame.revalidate();
        frame.repaint();
    }



    private void flushAddPanel() {
        // Clear all the JTextFields in the "addPanel"
        Component[] linearPanelComponents = linearPanel.getComponents();
        ((JTextField) linearPanelComponents[1]).setText("");
        ((JTextField) linearPanelComponents[3]).setText("");
        ((JTextField) linearPanelComponents[6]).setText("");

        Component[] sinePanelComponents = sinePanel.getComponents();
        ((JTextField) sinePanelComponents[1]).setText("");
        ((JTextField) sinePanelComponents[3]).setText("");
        ((JTextField) sinePanelComponents[5]).setText("");
        ((JTextField) sinePanelComponents[8]).setText("");
    }

    public void actionPerformed(ActionEvent e) {
        String curr_action = e.getActionCommand();

        if (curr_action.equals("add_linear_block")) {
            addLinearBlock();
        } else if (curr_action.equals("add_sine_block")) {
            addSineBlock();
        } else if (curr_action.equals("remove_prev_block")) {
            removePrevBlock();
        }

        flushAddPanel();
        refreshBlocks();
    }

    private void addLinearBlock() {
        Component[] all_components = linearPanel.getComponents();
        String m_val = ((JTextField) all_components[1]).getText();
        String b_val = ((JTextField) all_components[3]).getText();
        String t_end_val = ((JTextField) all_components[6]).getText();

        String t_start_val;
        if (blockArray.isEmpty()) {
            t_start_val = "0";
        } else {
            BlockContainer prevBlockContainer = blockArray.get(blockArray.size()-1);
            JPanel prevBlock = prevBlockContainer.block;
            Component[] prevBlockComponents = prevBlock.getComponents();
            if (prevBlockContainer.blockType.equals("linear")) {
                t_start_val = ((JTextField) prevBlockComponents[7]).getText();
            } else {
                t_start_val = ((JTextField) prevBlockComponents[9]).getText();
            }
        }

        BlockContainer currContainer = new BlockContainer();
        currContainer.block = createLinearBlock(m_val, b_val, t_start_val, t_end_val);
        currContainer.blockType = "linear";
        blockArray.add(currContainer);
        }

    private void addSineBlock() {
        Component[] all_components = sinePanel.getComponents();
        String a_val = ((JTextField) all_components[1]).getText();
        String b_val = ((JTextField) all_components[3]).getText();
        String c_val = ((JTextField) all_components[5]).getText();
        String t_end_val = ((JTextField) all_components[8]).getText();

        String t_start_val;
        if (blockArray.isEmpty()) {
            t_start_val = "0";
        } else {
            BlockContainer prevBlockContainer = blockArray.get(blockArray.size()-1);
            JPanel prevBlock = prevBlockContainer.block;
            Component[] prevBlockComponents = prevBlock.getComponents();
            if (prevBlockContainer.blockType.equals("linear")) {
                t_start_val = ((JTextField) prevBlockComponents[7]).getText();
            } else {
                t_start_val = ((JTextField) prevBlockComponents[9]).getText();
            }
        }

        BlockContainer currContainer = new BlockContainer();
        currContainer.block = createSineBlock(a_val, b_val, c_val, t_start_val, t_end_val);
        currContainer.blockType = "sine";
        blockArray.add(currContainer);
    }

    private void removePrevBlock() {
        if (!blockArray.isEmpty()) {
            blockArray.remove(blockArray.size()-1);
            allFuncSettings.remove(allFuncSettings.size()-1);
        }
    }

    ArrayList<BlockContainer> getBlockArray() {
        return blockArray;
    }

    ArrayList< String[] > getFuncSettings() {
        return allFuncSettings;
    }

    private JFrame frame;
    private JPanel mainPanel;
    private ArrayList<BlockContainer> blockArray = new ArrayList<>();
    private ArrayList< String[] > allFuncSettings = new ArrayList<>();
    private String channel;
    private JPanel allBlocks;
    private JPanel linearPanel;
    private JPanel sinePanel;
    private JPanel addPanel;

}