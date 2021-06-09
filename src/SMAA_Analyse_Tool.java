import smaa_creation.AGG;
import smaa_calculation.Utils;

import javax.swing.*;
import java.awt.*;

public class SMAA_Analyse_Tool {
    private JButton startButton;
    private JTextField sourceTextField;
    private JTextField targetTextField;
    private JButton browseSourceButton;
    private JButton browseTargetButton;
    private JPanel mainPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("SMAA Analyse Tool");
        frame.setContentPane(new SMAA_Analyse_Tool().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        //center window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public SMAA_Analyse_Tool() {
        if (System.getProperty("os.name").contains("Mac")) {
            targetTextField.setText(System.getProperty("user.home") + "/Downloads");
        } else {
            targetTextField.setText(System.getProperty("user.home") + "\\Downloads");
        }

        browseSourceButton.addActionListener(actionEvent -> onBrowseSource());
        browseTargetButton.addActionListener(actionEvent -> onBrowseTarget());
        startButton.addActionListener(actionEvent -> onStart());
    }


    private void onBrowseSource() {
        String inpath;
        if (System.getProperty("os.name").contains("Mac")) {
            inpath = getPathMAC(1);
            if (inpath.contains("null")) inpath = "";
        } else
            inpath = getPathElse(1);

        sourceTextField.setText(inpath);
    }

    private void onBrowseTarget() {
        String outpath;
        if (System.getProperty("os.name").contains("Mac")) {
            outpath = getPathMAC(2);
            if (outpath.contains("null")) outpath = System.getProperty("user.home") + "/Downloads";
        } else
            outpath = getPathElse(2);

        targetTextField.setText(outpath);
    }

    private void onStart() {
        String sourcePath = sourceTextField.getText();
        AGG agg = new AGG(sourcePath);
        double[][] raiTable = Utils.calculateRAI(agg, 100000);

        String pathToFile = targetTextField.getText() + "/RAI_table.csv";
        Utils.exportCsv(raiTable, pathToFile);
        JOptionPane.showMessageDialog(null, "Output saved at: " + pathToFile, "Success!", JOptionPane.INFORMATION_MESSAGE);
    }


    public String getPathMAC(int fileOrDir) {
        FileDialog d = new FileDialog(new JFrame(), "", FileDialog.LOAD);
        if (fileOrDir == 1) {
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            d.setFilenameFilter((dir, name) -> name.endsWith(".csv"));
        } else {
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
        }
        d.setVisible(true);
        return d.getDirectory() + d.getFile();

    }

    public String getPathElse(int fileOrDir) {
        if (fileOrDir == 1) {
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

        return "FAILED, PLEASE TRY AGAIN";
    }
}
