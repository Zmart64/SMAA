import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SMAA_Analyse_Tool {
    private JTextField textField1;
    private JButton startButton;
    private JTextField textField2;
    private JButton browseButton1;
    private JButton browseButton2;
    private JPanel Mainpanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("SMAA Analyse Tool");
        frame.setContentPane(new SMAA_Analyse_Tool().Mainpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        //center window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public SMAA_Analyse_Tool() {
        browseButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textField2.setText(getPath(2));

            }
        });
        browseButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textField1.setText(getPath(1));


            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String path = textField1.getText();
                String destination = textField2.getText();
                AGG agg = new AGG(path);

                double[][] raiTable = Utils.calculateRAI(agg, 100000);
                Utils.exportRaiTable(raiTable, destination);

                JOptionPane.showMessageDialog(null, "Output saved under: " + path, "Success!", JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }

    public String getPath(int dirOrFile){
        FileDialog d = new FileDialog(new JFrame(), "Bitte eine .csv Datei wählen", FileDialog.LOAD);
        if (dirOrFile == 1) {
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            d.setFile("*.csv");
            //Filter for Mac
            d.setFilenameFilter(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".csv");
                }
            });
        }
        else {
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
        }
        d.setVisible(true);
        return d.getDirectory() + d.getFile();

    }

    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}



