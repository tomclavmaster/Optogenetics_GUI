package GUI;

import javax.swing.*;

// For JSON file:
import simple.JSONArray;
import simple.JSONObject;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static GUI.Gui.functionPanels;
import static GUI.Gui.inputLinePanel;

class JSON_Out {
    static int write_json_file() {

        try (FileWriter file = new FileWriter(System.getProperty("user.dir") + "/settings.json")) {
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
                            float f = Float.valueOf(text.trim()).floatValue();
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
                float m_float = Float.valueOf(m_val.trim()).floatValue();
                pwmArr.add(m_float);
                float b_float = Float.valueOf(b_val.trim()).floatValue();
                pwmArr.add(b_float);
            } catch (NumberFormatException nfe) {
                System.out.println("PWM calibration value not numerical: " + nfe.getMessage());
                return 1;
            }

            allSettings.add(pwmArr);

            // Write all of the settings for all channels to the JSON file:
            file.write(allSettings.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static JSONArray getFunctionObject(int curr_chan) {
        JSONArray allFunctions = new JSONArray();

        FunctionWindow currFuncWindow = functionPanels[curr_chan];
        ArrayList< String[] > funcSettings_string = currFuncWindow.getFuncSettings();

        for (int i=0; i < funcSettings_string.size(); i+=1) {
            String[] funcParamsString = funcSettings_string.get(i);
            JSONArray currFunctionArray = new JSONArray();

            for (int j=0; j < funcParamsString.length; j+=1) {
                String text = funcParamsString[j];

                try {
                    double f = Float.valueOf(text.trim()).floatValue();
                    currFunctionArray.add(f);
//                    funcParamsDouble[j] = f;
                } catch (NumberFormatException nfe) {
                    System.out.println("Some intensity setting not numerical: " + nfe.getMessage());
                }
            }

            allFunctions.add(currFunctionArray);
        }


        return allFunctions;
    }

}
