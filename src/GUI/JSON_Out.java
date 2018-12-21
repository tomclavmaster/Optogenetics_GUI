package GUI;

import javax.swing.*;

// For JSON file:
import simple.JSONArray;
import simple.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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



    static int write_json_file(File file) {
        // Method used when user saves to a local file, NOT for uploading to Raspberry Pi.

        try (FileWriter fileWriter = new FileWriter(file)) {


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
                return 1;
            }

            allSettings.add(pwmArr);

            // Write all of the settings for all channels to the JSON file:
            System.out.println(allSettings.toJSONString());
            fileWriter.write(allSettings.toJSONString());
            fileWriter.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }



    static int read_json_file(File file) {
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
