package GUI;

import com.jcraft.jsch.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


//import jlatexmath.*;




public class Gui extends JFrame implements ActionListener {

    private Gui() {
        initMainWindow();
        initFunctionWindows();
        frame.setVisible(true);
    }

    private void initMainWindow() {

        // Load stripe image:
        try {
            stripes = ImageIO.read(getClass().getClassLoader().getResource("GUI/stripes.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load intensity equation image:
        try {
            intensityEquation = ImageIO.read(getClass().getClassLoader().getResource("GUI/intensityEquation.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load header equation images:
        try {
            headerPanels = new BufferedImage[9];
            headerPanels[0] = ImageIO.read(getClass().getClassLoader().getResource("GUI/channel.png"));
            headerPanels[1] = ImageIO.read(getClass().getClassLoader().getResource("GUI/const.png"));
            headerPanels[2] = ImageIO.read(getClass().getClassLoader().getResource("GUI/blink.png"));
            headerPanels[3] = ImageIO.read(getClass().getClassLoader().getResource("GUI/func.png"));
            headerPanels[4] = ImageIO.read(getClass().getClassLoader().getResource("GUI/intensity.png"));
            headerPanels[5] = ImageIO.read(getClass().getClassLoader().getResource("GUI/i_min.png"));
            headerPanels[6] = ImageIO.read(getClass().getClassLoader().getResource("GUI/i_max.png"));
            headerPanels[7] = ImageIO.read(getClass().getClassLoader().getResource("GUI/duty_cycle.png"));
            headerPanels[8] = ImageIO.read(getClass().getClassLoader().getResource("GUI/period.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        UIManager.put( "control" , Color.WHITE);
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }

        initMenuBar();

        // Main GUI window initialization:
        frame = new JFrame("LED Controller");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 770);
//        frame.setResizable(false);

        // Initialize nested panels:
        initLeftPanel();
        initCenterPanel();

        // Add all panels to main panel:
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,0));
        mainPanel.add(leftPanel, BorderLayout.LINE_START);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        frame.add(mainPanel);


        frame.setJMenuBar(menuBar);

    }

    private static void initFunctionWindows() {
        int chan_num = 0;
        String[] letters = {"A","B","C","D"};
        for (String letter : letters) {
            for (int j = 1; j <= 6; j += 1) {
                functionPanels[chan_num] = new FunctionWindow(letter + j);
                chan_num += 1;
            }
        }
    }

    private void initMenuBar() {
        //Create the menu bar.
        menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItemOpen;
        JMenuItem menuItemSaveAs;

        //Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        // a group of JMenuItems
        menuItemOpen = new JMenuItem("Open");
        menu.add(menuItemOpen);

        menuItemSaveAs = new JMenuItem("Save As");
        menu.add(menuItemSaveAs);


        menuItemOpen.addActionListener(
            e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open");
                fileChooser.setPreferredSize(new Dimension(800,680));
                fileChooser.setFileFilter(new FileNameExtensionFilter(".json files", "json"));

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File fileToBeRead = fileChooser.getSelectedFile();
                    JSON_Out.read_json_file(fileToBeRead);  // load from file

                    frame.validate();
                    frame.repaint();

                }
            }
        );


        menuItemSaveAs.addActionListener(
            e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save As");
                fileChooser.setPreferredSize(new Dimension(800,600));
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File fileToBeSaved = fileChooser.getSelectedFile();

                    String suffix = ".json";
                    if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(suffix)){
                        fileToBeSaved = new File(fileChooser.getSelectedFile() + suffix);
                    }

                    JSON_Out.write_json_file(fileToBeSaved);  // save to file

                    frame.validate();
                    frame.repaint();
                }
            }
        );
    }

    private void initLeftPanel() {
        // Primary left panel object:
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));

        // Plate selection panel:
        JPanel plateTypePanel = new JPanel();
        plateTypePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        plateTypePanel.setLayout(new BoxLayout(plateTypePanel, BoxLayout.Y_AXIS));

        TitledBorder plateBorder = new TitledBorder("Plate Type");
        plateBorder.setTitleJustification(TitledBorder.LEFT);
        plateBorder.setTitlePosition(TitledBorder.TOP);
        plateTypePanel.setBorder(plateBorder);

        JRadioButton plate24Well = new JRadioButton("24 Plate Well", true);
        ButtonGroup bG = new ButtonGroup();
        bG.add(plate24Well);
        plateTypePanel.add(plate24Well);
        leftPanel.add(plateTypePanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Overview panel:
        PreviewPanel overviewPanel = new PreviewPanel();
        overviewPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        overviewPanel.setLayout(new BoxLayout(overviewPanel, BoxLayout.Y_AXIS));
        overviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);


        TitledBorder overviewBorder = new TitledBorder("Overview (LED Preview)");
        overviewBorder.setTitleJustification(TitledBorder.LEFT);
        overviewBorder.setTitlePosition(TitledBorder.TOP);
        overviewPanel.setBorder(overviewBorder);
        leftPanel.add(overviewPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        // Intensity panel, houses top text panel and lower inputs:
        JPanel intensityPanel = new JPanel();
        intensityPanel.setLayout(new BoxLayout(intensityPanel, BoxLayout.Y_AXIS));
        intensityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create border and intensity equation:
        TitledBorder intensityBorder = new TitledBorder("Intensity / PWM Conversion");
        intensityBorder.setTitleJustification(TitledBorder.LEFT);
        intensityBorder.setTitlePosition(TitledBorder.TOP);
        intensityPanel.setBorder(intensityBorder);

        EquationPanel intensityEquationPanel = new EquationPanel();
        intensityPanel.add(intensityEquationPanel);

        // Allow input of intensity line parameters:
        inputLinePanel = new JPanel();
        inputLinePanel.setLayout(new BoxLayout(inputLinePanel, BoxLayout.X_AXIS));
        inputLinePanel.add( new JLabel("m="));
        JTextField mInput = new JTextField();
        mInput.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, mInput.getPreferredSize().height) );
        inputLinePanel.add(mInput);
        inputLinePanel.add( new JLabel("b="));
        JTextField bInput = new JTextField();
        bInput.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, bInput.getPreferredSize().height) );
        inputLinePanel.add(bInput);
        intensityPanel.add(inputLinePanel);
        leftPanel.add(intensityPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        // Board specs panel:
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        TitledBorder infoPanelBorder = new TitledBorder("Interface Information:");
        infoPanelBorder.setTitleJustification(TitledBorder.LEFT);
        infoPanelBorder.setTitlePosition(TitledBorder.TOP);
        infoPanel.setBorder(infoPanelBorder);


        infoPanel.add( new JLabel("Duty cycle = Percent time \"on\" per period."));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add( new JLabel("Period = Length of blink cycle."));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add( new JLabel("<html>E.g. For 100ms blinks with 900ms pause, <br>   set duty cycle to 10% and period to 1s.</html>"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 50)));


        infoPanel.add( new JLabel("Function Max Sample Rate: 100 kHz (i.e. 100 ms)"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add( new JLabel("Blink Min Period: 100 ms (i.e. 100 kHz)"));

        leftPanel.add(infoPanel);


        // Board specs panel:
        JPanel boardSpecPanel = new JPanel();
        boardSpecPanel.setLayout(new BoxLayout(boardSpecPanel, BoxLayout.Y_AXIS));
        boardSpecPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        TitledBorder boardSpecBorder = new TitledBorder("Board Specifications:");
        boardSpecBorder.setTitleJustification(TitledBorder.LEFT);
        boardSpecBorder.setTitlePosition(TitledBorder.TOP);
        boardSpecPanel.setBorder(boardSpecBorder);
        boardSpecPanel.add( new JLabel("-24 Plate Well"));
        boardSpecPanel.add( new JLabel("-Raspberry Pi Zero W with Raspbian Stretch"));
        boardSpecPanel.add( new JLabel("-Adafruit TLC5947 LED Driver"));
        leftPanel.add(boardSpecPanel);
    }

    private void initCenterPanel() {

        centerPanel = new JPanel();
        centerGridBag = new GridBagLayout();
        centerConstraints = new GridBagConstraints();
        centerPanel.setLayout(centerGridBag);
        centerConstraints.fill = GridBagConstraints.HORIZONTAL;
//        centerPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2,true));
        centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 2, true),
                                                                new EmptyBorder(10,10,0,10)));


        makeButtonHeader();

        int chan_num = 0;
        String[] letters = {"A", "B", "C", "D"};
        for (String letter : letters)
            for (int j = 1; j <= 6; j += 1) {
                makeChannelButtons(letter + j, chan_num);
                makeIntensityPanel(chan_num);

                chan_num += 1;
            }


        JButton submit = new JButton("Submit and Upload!");
        submit.addActionListener(this);
        submit.setActionCommand("submit_settings");

        centerConstraints.gridwidth = 9;
        centerConstraints.gridx = 0;
        centerConstraints.gridy = chan_num + 1;
        centerConstraints.weighty = 0;
        centerConstraints.weightx = 1;
        centerGridBag.setConstraints(submit, centerConstraints);
        centerPanel.add(submit);
    }


    private void makeButtonHeader() {
        String[] labels = {"Channel:", "Const", "Blink", "Func", "I(uW/mm^2)", "I_min", "I_max", "duty_cycle (%)", "period (s)"};

        for (int i = 0; i<headerPanels.length; i+=1) {
            HeaderPanel currPanel = new HeaderPanel(i);
            currPanel.setPreferredSize(new Dimension(85, 25));
            currPanel.setMinimumSize(currPanel.getPreferredSize());
            JPanel jpanel = new JPanel();
            jpanel.add(currPanel);

            centerConstraints.weightx = 1;
            centerConstraints.weighty = 0;
            centerConstraints.gridx = i;
            centerConstraints.gridy = 0;
            centerGridBag.setConstraints(jpanel, centerConstraints);
            centerPanel.add(jpanel);
        }

    }


    private void makeChannelButtons(String label, int chan_num) {

        JLabel channelNum = new JLabel("Channel " + label + ":\t");

        centerConstraints.gridx = 0;
        centerConstraints.gridy = chan_num + 1;
        centerConstraints.weightx = 0;
//        centerConstraints.weighty = 1;


        centerGridBag.setConstraints(channelNum, centerConstraints);
        centerPanel.add(channelNum);

        controlButtons[chan_num] = new JRadioButton[]{new JRadioButton(), new JRadioButton(), new JRadioButton()};
        controlButtons[chan_num][0].setSelected(true);


        for (int i = 0; i<3; i+=1) {
            // In order to properly resize buttons, require interacting with the constraints panel:
            centerConstraints.gridx = i+1;
            centerConstraints.gridy = chan_num + 1;
            centerConstraints.weightx = 0.1;
            centerGridBag.setConstraints(controlButtons[chan_num][i], centerConstraints);
            centerPanel.add(controlButtons[chan_num][i]);

        }

        // Add the three buttons to a button group:
        ButtonGroup bG = new ButtonGroup();
        bG.add(controlButtons[chan_num][0]);
        bG.add(controlButtons[chan_num][1]);
        bG.add(controlButtons[chan_num][2]);

        // Add action listeners on radio buttons:
        for (int i = 0; i<3; i+=1) {
            controlButtons[chan_num][i].addActionListener(this);
            controlButtons[chan_num][i].setActionCommand(Integer.toString(chan_num) + "," + Integer.toString(i));
        }

    }


    private void makeIntensityPanel(int chan_num) {
        intensitySettings[chan_num] = new JTextField[]{new JTextField("",20), new JTextField("",20),
                new JTextField("",20), new JTextField("",20),
                new JTextField("",20)};


        for (int i = 0; i<5; i+=1) {
            centerConstraints.gridx = i+4;
            centerConstraints.gridy = chan_num + 1;
            centerConstraints.weightx = 1;
//            centerConstraints.weighty = 1;

            centerGridBag.setConstraints(intensitySettings[chan_num][i], centerConstraints);
            centerPanel.add(intensitySettings[chan_num][i]);
        }
        for (int i=1; i<5; i+=1) {
            intensitySettings[chan_num][i].setEnabled(false);
        }

        // Will adjust colors after intensity values input and text box loses focus:
        intensitySettings[chan_num][0].addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                frame.repaint();
            }
        });
    }


    static void channelRedraw(int chan_num, int buttonNum, boolean showFunctionWindow) {
        switch (buttonNum) {
            case 0:
                for (int i = 1; i < 5; i += 1) {
                    intensitySettings[chan_num][i].setEnabled(false);
                }
                intensitySettings[chan_num][0].setEnabled(true);
                break;

            case 1:
                intensitySettings[chan_num][0].setEnabled(false);
                for (int i = 1; i < 5; i += 1) {
                    intensitySettings[chan_num][i].setEnabled(true);
                }
                break;

            case 2:
                for (int i = 0; i < 5; i += 1) {
                    intensitySettings[chan_num][i].setEnabled(false);
                }
                if (showFunctionWindow)
                    functionPanels[chan_num].showWindow();
                break;
        }
    }


    public void actionPerformed(ActionEvent e) {
        String curr_action = e.getActionCommand();
        frame.repaint();

        if (curr_action.equals("submit_settings")) {
            int exit_code = JSON_Out.write_json_file();
            if (exit_code == 0) {
                uploadJSON();
            }
        } else {
            int chan_num = Integer.parseInt(curr_action.split(",")[0]);
            int buttonNum = Integer.parseInt(curr_action.split(",")[1]);

            channelRedraw(chan_num, buttonNum, true);
        }
    }

    public static void resetFunctionWindows() {
        functionPanels = new FunctionWindow[24];
        initFunctionWindows();
    }



    private void uploadJSON() {
        JSch jsch = new JSch();
        Session session;
        try {
            session = jsch.getSession("pi", "192.168.4.1");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(15000); // Timeout after 15 seconds without connection.
            session.setPassword("raspberry");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;


            String settingsDirectory = (System.getProperty("user.dir") + "/settings.json");
            sftpChannel.put(settingsDirectory, "/home/pi/LED_Driver/Adafruit_TLC5947_master/settings.json");
            sftpChannel.exit();
            session.disconnect();

        } catch (JSchException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Check connection to Raspberry Pi",
                    "Error: Unable to connect", JOptionPane.ERROR_MESSAGE);
        } catch (SftpException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Check connection to Raspberry Pi",
                    "Error: Unable to connect", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        Gui g = new Gui();
    }

    private JFrame frame;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JFrame centerFrame;

    private GridBagLayout centerGridBag;
    private GridBagConstraints centerConstraints;

    static JPanel inputLinePanel;
    static BufferedImage stripes;
    static BufferedImage intensityEquation;
    static BufferedImage[] headerPanels;

    static JRadioButton[][] controlButtons = new JRadioButton[24][3];
    static JTextField[][] intensitySettings = new JTextField[24][5];
    static FunctionWindow[] functionPanels = new FunctionWindow[24];

    // Necessary for menu bar:
    private JMenuBar menuBar;
}