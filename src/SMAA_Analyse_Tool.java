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
        //UIManager.getCrossPlatformLookAndFeelClassName();
        //System.setProperty("apple.awt.fileDialogForDirectories", "true");
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
                JFileChooser chooser = new JFileChooser();


                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnval = chooser.showOpenDialog(null);

                if (returnval == JFileChooser.APPROVE_OPTION) {
                    textField2.setText(chooser.getSelectedFile().getPath());
                }

            }
        });
        browseButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                /*JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("CSV Datei", "csv");
                chooser.addChoosableFileFilter(filter);
                int returnval = chooser.showOpenDialog(null);

                if (returnval == JFileChooser.APPROVE_OPTION) {
                    textField1.setText(chooser.getSelectedFile().getPath());
                }*/
                FileDialog d = new FileDialog(new JFrame(), "Bitte eine .csv Datei wählen", FileDialog.LOAD);
                d.setFile("*.csv");
                //Filter for Mac
                d.setFilenameFilter(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".csv");
                    }
                });
                d.setVisible(true);
                textField1.setText(d.getDirectory() + d.getFile());


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

            }
        });
    }
}



