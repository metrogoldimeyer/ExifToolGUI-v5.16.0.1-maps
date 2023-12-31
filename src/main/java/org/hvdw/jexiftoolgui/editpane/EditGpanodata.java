package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PRESERVE_MODIFY_DATE;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;


public class EditGpanodata {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(EditGpanodata.class);
    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    public void setFormattedFieldFormats(JFormattedTextField[] theFields) {
        Locale currentLocale = Locale.getDefault();
        NumberFormat formatter = NumberFormat.getNumberInstance(currentLocale );
        formatter.setMaximumFractionDigits(4);
        for (JFormattedTextField field : theFields) {
            field.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(formatter)));
        }
    }


    public void resetFields(JFormattedTextField[] gpanoFields, JTextField gpanoStitchingSoftwaretextField,  JCheckBox[] gpanoBoxes) {

        for (JFormattedTextField field: gpanoFields) {
            field.setText("");
        }
        for (JCheckBox checkbox: gpanoBoxes) {
            checkbox.setSelected(false);
        }
        gpanoStitchingSoftwaretextField.setText("");
    }

    public boolean checkFieldsOnNotBeingEmpty(JFormattedTextField[] gpanoFields, JComboBox gpanoPTcomboBox) {
        boolean complete = true;
        // Only first 6 are mandatory so we now use a for loop
        for (int index = 0;  index < 6; index++) {
            if (gpanoFields[index].getText().trim().isEmpty()) {
                complete = false;
            }
        }
        return complete;
    }

    public void copyGpanoFromSelected(JFormattedTextField[] gpanoFields, JTextField gpanoStitchingSoftwaretextField, JComboBox gpanoPTCombobox, JCheckBox[] gpanoBoxes) {
        File[] files = MyVariables.getLoadedFiles();
        int SelectedRow = MyVariables.getSelectedRowOrIndex();
        String[] gpano_params =  {"-xmp:StitchingSoftware","-xmp:CroppedAreaImageHeightPixels","-xmp:CroppedAreaImageWidthPixels","-xmp:CroppedAreaLeftPixels","-xmp:CroppedAreaTopPixels","-xmp:FullPanoHeightPixels","-xmp:FullPanoWidthPixels","-xmp:ProjectionType","-xmp:UsePanoramaViewer","-xmp:PoseHeadingDegrees","-xmp:InitialViewHeadingDegrees","-xmp:InitialViewPitchDegrees","-xmp:InitialViewRollDegrees","-xmp:InitialHorizontalFOVDegrees"};
        String fpath ="";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(gpanoFields, gpanoStitchingSoftwaretextField, gpanoBoxes);

        if (Utils.isOsFromMicrosoft()) {
            fpath = files[SelectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[SelectedRow].getPath();
        }
        cmdparams.add(Utils.platformExiftool());
        cmdparams.add("-e");
        cmdparams.add("-n");
        cmdparams.addAll( Arrays.asList(gpano_params));
        cmdparams.add(fpath);
        try {
            res = CommandRunner.runCommand(cmdparams);
            logger.info("res is\n{}", res);
        } catch(IOException | InterruptedException ex) {
            logger.debug("Error executing command");
        }
        if (res.length() > 0) {
            displayCopiedInfo(gpanoFields, gpanoStitchingSoftwaretextField, gpanoPTCombobox, res);
        }
    }

    private void displayCopiedInfo(JFormattedTextField[] gpanoFields, JTextField gpanoStitchingSoftwaretextField, JComboBox gpanoPTCombobox, String exiftoolInfo) {
        String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
        //for(int i = 0; i < lines.length; i++) {
        for (String line : lines) {
            String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
            String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
            //Wit ALL spaces removed from the tag we als need to use identiefiers without spaces
            logger.info(SpaceStripped, " ; value: ", cells[1], "\n");
            if (SpaceStripped.contains("CroppedAreaImageHeightPixels")) {
                gpanoFields[0].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("CroppedAreaImageWidthPixels")) {
                gpanoFields[1].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("CroppedAreaLeftPixels")) {
                gpanoFields[2].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("CroppedAreaTopPixels")) {
                gpanoFields[3].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("FullPanoHeightPixels")) {
                gpanoFields[4].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("FullPanoWidthPixels")) {
                gpanoFields[5].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("ProjectionType")) {
                gpanoPTCombobox.setSelectedItem(cells[1].trim());
                logger.info("projection type", cells[1].trim());
                //gpanoFields[6].setText(cells[1]);
            }
            if (SpaceStripped.contains("PoseHeadingDegrees")) {
                gpanoFields[6].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("StitchingSoftware")) {
                gpanoStitchingSoftwaretextField.setText(cells[1].trim());
            }
            if (SpaceStripped.contains("InitialViewHeadingDegrees")) {
                gpanoFields[7].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("InitialViewPitchDegrees")) {
                gpanoFields[8].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("InitialViewRollDegrees")) {
                gpanoFields[9].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("InitialHorizontalFOVDegrees")) {
                gpanoFields[10].setText(cells[1].trim());
            }
        }
    }


    public void writeGpanoTags(JFormattedTextField[] gpanoFields, JCheckBox[] gpanoBoxes, JTextField gpanoStitchingSoftwaretextField, JComboBox gpanoPTcomboBox, JProgressBar progressBar) {

        List<String> cmdparams = new ArrayList<String>();
        File[] files = MyVariables.getLoadedFiles();
        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();

        cmdparams.add(Utils.platformExiftool());
        boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
        if (preserveModifyDate) {
            cmdparams.add("-preserve");
        }
        if (!gpanoBoxes[6].isSelected()) { // default overwrite originals, when set do not
            cmdparams.add("-overwrite_original");
        }
        cmdparams.addAll(Utils.AlwaysAdd());


        // These are manadatory anyway and do not need a checkbox. They need to be controlled first if they are not empty
        cmdparams.add("-xmp:CroppedAreaImageHeightPixels=" + gpanoFields[0].getText().trim());
        cmdparams.add("-xmp:CroppedAreaImageWidthPixels=" + gpanoFields[1].getText().trim());
        cmdparams.add("-xmp:CroppedAreaLeftPixels=" + gpanoFields[2].getText().trim());
        cmdparams.add("-xmp:CroppedAreaTopPixels=" + gpanoFields[3].getText().trim());
        cmdparams.add("-xmp:FullPanoHeightPixels=" + gpanoFields[4].getText().trim());
        cmdparams.add("-xmp:FullPanoWidthPixels=" + gpanoFields[5].getText().trim());
        // Get combobox value
        cmdparams.add("-xmp:ProjectionType=" + gpanoPTcomboBox.getSelectedItem());
        cmdparams.add("-xmp:UsePanoramaViewer=1");
        if (gpanoBoxes[0].isSelected()) {
            cmdparams.add("-xmp:PoseHeadingDegrees=" + gpanoFields[6].getText().trim());
        }
        if (gpanoBoxes[1].isSelected()) {
            cmdparams.add("-xmp:StitchingSoftware=" + gpanoStitchingSoftwaretextField.getText().trim());
        }
        if (gpanoBoxes[2].isSelected()) {
            cmdparams.add("-xmp:InitialViewHeadingDegrees=" + gpanoFields[7].getText().trim());
        }
        if (gpanoBoxes[3].isSelected()) {
            cmdparams.add("-xmp:InitialViewPitchDegrees=" + gpanoFields[8].getText().trim());
        }
        if (gpanoBoxes[4].isSelected()) {
            cmdparams.add("-xmp:InitialViewRollDegrees=" + gpanoFields[9].getText().trim());
        }
        if (gpanoBoxes[5].isSelected()) {
            cmdparams.add("-xmp:InitialHorizontalFOVDegrees=" + gpanoFields[10].getText().trim());
        }

        for (int index: selectedIndices) {
            //logger.info("index: {}  image path: {}", index, files[index].getPath());
            if (Utils.isOsFromMicrosoft()) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
        }

        CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);

    }
}
