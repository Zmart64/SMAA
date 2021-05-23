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
        if (System.getProperty("os.name").contains("Mac")) {
            textField2.setText(System.getProperty("user.home") + "/Downloads");
        } else {
            textField2.setText(System.getProperty("user.home") + "\\Downloads");
        }

        browseButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String inpath;
                if (System.getProperty("os.name").contains("Mac")) {
                    inpath = getPathMAC(1);
                } else
                    inpath = getPathElse(1);

                textField1.setText(inpath);
            }
        });

        browseButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String outpath;
                if (System.getProperty("os.name").contains("Mac")) {
                    outpath = getPathMAC(2);
                } else
                    outpath = getPathElse(2);

                textField2.setText(outpath);


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

                JOptionPane.showMessageDialog(null, "Output saved at: " + textField2.getText() + "/RAI_table.csv", "Success!", JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }

    public String getPathMAC(int fileOrDir) {
        FileDialog d = new FileDialog(new JFrame(), "", FileDialog.LOAD);
        if (fileOrDir == 1) {
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            d.setFilenameFilter(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".csv");
                }
            });
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

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                return chooser.getSelectedFile().getPath();
            }
        }

        return "FAILED, PLEASE TRY AGAIN";
    }
}
