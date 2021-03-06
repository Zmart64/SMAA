import smaa_calculation.Utils;
import smaa_creation.AGG;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * provides a GUI for easy Interaction
 */
public class SMAATool {
    private JButton startButton;
    private JTextField sourceTextField;
    private JTextField targetTextField;
    private JButton browseSourceButton;
    private JButton browseTargetButton;
    private JPanel mainPanel;
    private JLabel imgLabel;
    private final boolean isMac;
    private static final String HOME = "user.home";

    /**
     * setting up Analyse Tool (no functionality)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("SMAArt Tool");
        frame.setContentPane(new SMAATool().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        //center window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    /**
     * setting up functionality of Analyse-Tool
     */
    public SMAATool() {

        isMac = System.getProperty("os.name").contains("Mac");
        if (isMac) {
            targetTextField.setText(System.getProperty(HOME) + "/Downloads");
        } else {
            targetTextField.setText(System.getProperty(HOME) + "\\Downloads");
        }

        browseSourceButton.addActionListener(actionEvent -> onBrowseSource());
        browseTargetButton.addActionListener(actionEvent -> onBrowseTarget());
        startButton.addActionListener(actionEvent -> onStart());
    }


    private void browse(String fileOrDir) {
        String path;
        if (isMac) {
            path = getPathMac(fileOrDir);
        } else
            path = getPathOtherSystem(fileOrDir);

        if (fileOrDir.equals("file"))
            sourceTextField.setText(path);
        else
            targetTextField.setText(path);
    }

    private void onBrowseSource() {
        browse("file");
    }

    private void onBrowseTarget() {
        browse("dir");
    }

    /**
     * performs SMAA and outputs file, gives option to open File-Exporer at the output path
     **/
    private void onStart() {
        try {
            String sourcePath = sourceTextField.getText();
            String outpath = targetTextField.getText();
            AGG agg = new AGG(sourcePath);
            double[][] raiTable = Utils.calculateRAI(agg, 10000);

            String fileName = isMac ? sourcePath.substring(sourcePath.lastIndexOf('/') + 1)
                    : sourcePath.substring(sourcePath.lastIndexOf('\\') + 1);
            String pathToFile = outpath + "/output_" + fileName;


            String open = isMac ? "open " + outpath : "Explorer.exe " + outpath;

            Utils.exportCsv(raiTable, pathToFile);

            int choice =  JOptionPane.showConfirmDialog(null,
                    "Output saved at: " + pathToFile + "\nDo you want to open the file in your File-Explorer?", "Success!", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                Runtime.getRuntime().exec(open);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Check if your file is formatted properly and ensure that the given paths are valid.",
                    "SMAA FAILED", JOptionPane.ERROR_MESSAGE);
        }
    }


    public String getPathMac(String fileOrDir) {
        FileDialog dialog = new FileDialog(new JFrame(), "", FileDialog.LOAD);
        if (fileOrDir.equals("file")) {
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            dialog.setFilenameFilter((dir, name) -> name.endsWith(".csv"));
        } else {
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
        }
        dialog.setVisible(true);
        String path = dialog.getDirectory() + dialog.getFile();

        if (path.contains("null") && fileOrDir.equals("dir")) {
            path = System.getProperty(HOME) + "/Downloads";
        } else if (path.contains("null") && fileOrDir.equals("file")) {
            path = "";
        }
        return path;
    }

    public String getPathOtherSystem(String fileOrDir) {
        if (fileOrDir.equals("file")) {
            FileDialog d = new FileDialog(new JFrame(), "Please select a .csv-File", FileDialog.LOAD);
            d.setFile("*.csv");
            d.setVisible(true);
            return d.getDirectory() + d.getFile();

        } else {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile().getPath();
            }
        }

        return System.getProperty(HOME) + "\\Downloads";
    }
}
