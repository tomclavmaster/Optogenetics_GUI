package GUI;

import javax.swing.*;

// For JSON file:
import simple.JSONArray;
import simple.JSONObject;
import simple.parser.JSONParser;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import static GUI.Gui.functionPanels;
import static GUI.Gui.inputLinePanel;

class JSON_Out {
    static int write_json_file() {
        /*
            Method used when uploading to Raspberry Pi.
            NOTE: When uploading to Pi, filename = "settings.json"
        */

        try (FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/settings.json")) {
            JSONArray allSettings = new JSONArray();

            // Add all controlMode and intensity settings:
            for (int curr_chan = 0; curr_chan < Gui.controlButtons.length; curr_chan+=1) {
                JSONObject currObj = new JSONObject();
                currObj.put("channel", Integer.toString(curr_chan));

                // Add the currently selected intensity mode:
                int mode = -1;
                for (int j = 0; j < Gui.controlButtons[curr_chan].length; j += 1) {
                    if (Gui.controlButtons[curr_chan][j].isSelected()) {
                        mode = j;
                    }
                }
                currObj.put("controlMode", mode);


                // Add the intensity values to JSON array:
                JSONArray list = new JSONArray();
                for (JTextField curr_setting : Gui.intensitySettings[curr_chan]) {
                    String text = curr_setting.getText();

                    try {
                        if (text.isEmpty()) {
                            list.add(text);
                        } else {
                            float f = Float.valueOf(text.trim());
                            list.add(f);
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Some intensity setting not numerical: " + nfe.getMessage());
                    }

                }
                currObj.put("intensity_settings", list);

                // Put all linear/sine function info into JSON:
                JSONArray funcArr = getFunctionObject(curr_chan);
                currObj.put("functions", funcArr);

                allSettings.add(currObj);

            }

            // Add PWM conversion info:
            JSONArray pwmArr = new JSONArray();
            Component[] all_components = inputLinePanel.getComponents();
            String m_val = ((JTextField) all_components[1]).getText();
            String b_val = ((JTextField) all_components[3]).getText();

            try {
                float m_float = Float.valueOf(m_val.trim());
                pwmArr.add(m_float);
                float b_float = Float.valueOf(b_val.trim());
                pwmArr.add(b_float);
            } catch (NumberFormatException nfe) {
                // User attempting to upload settings without valid calibration values.
                JOptionPane.showMessageDialog(null, "Missing or Non-numerical PWM calibration values",
                         "Warning", JOptionPane.WARNING_MESSAGE);
                return 1;
            }

            allSettings.add(pwmArr);

            // Write all of the settings for all channels to the JSON file:
            fileWriter.write(allSettings.toJSONString());
            fileWriter.flush();

        } catch (IOException saveException) {
            saveException.printStackTrace();
        }
        return 0;
    }



    static int write_json_file(File fileToBeSaved) {
        // Method used when user saves to a local file, NOT for uploading to Raspberry Pi.

        try (FileWriter fileWriter = new FileWriter(fileToBeSaved)) {

            JSONArray allSettings = new JSONArray();

            // Add all controlMode and intensity settings:
            for (int curr_chan = 0; curr_chan < Gui.controlButtons.length; curr_chan+=1) {
                JSONObject currObj = new JSONObject();
                currObj.put("channel", Integer.toString(curr_chan));

                // Add the currently selected intensity mode:
                int mode = -1;
                for (int j = 0; j < Gui.controlButtons[curr_chan].length; j += 1) {
                    if (Gui.controlButtons[curr_chan][j].isSelected()) {
                        mode = j;
                    }
                }
                currObj.put("controlMode", mode);


                // Add the intensity values to JSON array:
                JSONArray list = new JSONArray();
                for (JTextField curr_setting : Gui.intensitySettings[curr_chan]) {
                    String text = curr_setting.getText();

                    try {
                        if (text.isEmpty()) {
                            list.add(text);
                        } else {
                            float f = Float.valueOf(text.trim());
                            list.add(f);
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Some intensity setting not numerical: " + nfe.getMessage());
                    }

                }
                currObj.put("intensity_settings", list);

                // Put all linear/sine function info into JSON:
                JSONArray funcArr = getFunctionObject(curr_chan);
                currObj.put("functions", funcArr);

                allSettings.add(currObj);

            }

            // Add PWM conversion info:
            JSONArray pwmArr = new JSONArray();
            Component[] all_components = inputLinePanel.getComponents();
            String m_val = ((JTextField) all_components[1]).getText();
            String b_val = ((JTextField) all_components[3]).getText();

            try {
                float m_float = Float.valueOf(m_val.trim());
                pwmArr.add(m_float);
                float b_float = Float.valueOf(b_val.trim());
                pwmArr.add(b_float);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                JOptionPane.showMessageDialog(null, "Save Failed: Missing or Non-numerical PWM calibration values",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return 1;
            }

            allSettings.add(pwmArr);

            // Write all of the settings for all channels to the JSON file:
            fileWriter.write(allSettings.toJSONString());
            fileWriter.flush();


        } catch (IOException saveException) {
            saveException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save file",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }



    static int read_json_file(File fileToBeRead) {

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader(fileToBeRead)) {

            //Read JSON file
            Object obj = jsonParser.parse(fileReader);
            JSONArray allSettingsList = (JSONArray) obj;


            // Clear current function windows:
            GUI.Gui.resetFunctionWindows();

            // Parse all channel settings:
            for (int curr_chan = 0; curr_chan < allSettingsList.size() - 1; curr_chan += 1) {
                JSONObject currChannel = (JSONObject) allSettingsList.get(curr_chan);

                try {

                    int mode = ((Long) currChannel.get("controlMode")).intValue();
                    for (int j = 0; j < Gui.controlButtons[curr_chan].length; j += 1) {
                        if (j == mode) {
                            Gui.controlButtons[curr_chan][j].setSelected(true);
                            Gui.channelRedraw(curr_chan, j, false);
                        } else {
                            Gui.controlButtons[curr_chan][j].setSelected(false);
                        }
                    }

                    // Grab intensity values from JSON array:
                    JSONArray intensityList = (JSONArray) currChannel.get("intensity_settings");
                    for (int settingNum = 0; settingNum < intensityList.size(); settingNum += 1) {
                        Object currSettingObj = intensityList.get(settingNum);
                        if (currSettingObj instanceof String) {
                            // Means no setting needs to be loaded.
                            Gui.intensitySettings[curr_chan][settingNum].setText("");
                        } else {
                            // Means a setting was stored.
                            String currValStr = ((Double) currSettingObj).toString();
                            Gui.intensitySettings[curr_chan][settingNum].setText(currValStr);
                        }

                    }



                    // Grab all linear/sine function info from JSON:
                    JSONArray allFunctions = (JSONArray) currChannel.get("functions");
                    for (Object functionArray : allFunctions) {
                        JSONArray currFunctionArray = (JSONArray) functionArray;

                        // Length dictates type of function:
                        if (currFunctionArray.size() == 4) {
                            // Linear function.

                            String m = String.valueOf(currFunctionArray.get(0));
                            String b = String.valueOf(currFunctionArray.get(1));
                            String t_start = String.valueOf(currFunctionArray.get(2));
                            String t_end = String.valueOf(currFunctionArray.get(3));

                            functionPanels[curr_chan].addLinearBlock(m, b, t_start, t_end);

                        } else if (currFunctionArray.size() == 5) {
                            // Sine function.

                            String a = String.valueOf(currFunctionArray.get(0));
                            String b = String.valueOf(currFunctionArray.get(1));
                            String c = String.valueOf(currFunctionArray.get(2));
                            String t_start = String.valueOf(currFunctionArray.get(3));
                            String t_end = String.valueOf(currFunctionArray.get(3));

                            functionPanels[curr_chan].addSineBlock(a, b, c, t_start, t_end);

                        } else {
                            JOptionPane.showMessageDialog(null, "Warning: Some settings could not be loaded.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
                        }

                    }


                } catch (ClassCastException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Something went wrong! This file could not be loaded.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }




            // Parse PWM conversion values:
            JSONArray pwmArr = (JSONArray) allSettingsList.get(allSettingsList.size()-1);
            String m_string = String.valueOf(pwmArr.get(0));
            String b_string = String.valueOf(pwmArr.get(1));

            Component[] all_components = inputLinePanel.getComponents();
            ((JTextField) all_components[1]).setText(m_string);
            ((JTextField) all_components[3]).setText(b_string);




        } catch (FileNotFoundException readException) {
            readException.printStackTrace();
            JOptionPane.showMessageDialog(null, "File \"" + fileToBeRead.getName() + "\" Not Found",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        } catch(IOException e){
            e.printStackTrace();
        } catch (simple.parser.ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid Settings File",
                    "Alert", JOptionPane.WARNING_MESSAGE);
        }

        return 0;
    }



    private static JSONArray getFunctionObject(int curr_chan) {
        JSONArray allFunctions = new JSONArray();

        FunctionWindow currFuncWindow = functionPanels[curr_chan];
        ArrayList< String[] > funcSettings_string = currFuncWindow.getFuncSettings();

        for (String[] funcParamsString : funcSettings_string) {
            JSONArray currFunctionArray = new JSONArray();

            for (String text : funcParamsString) {
                try {
                    double f = Float.valueOf(text.trim());
                    currFunctionArray.add(f);
                } catch (NumberFormatException nfe) {
                    System.out.println("Some intensity setting not numerical: " + nfe.getMessage());
                }
            }

            allFunctions.add(currFunctionArray);
        }


        return allFunctions;
    }

}
