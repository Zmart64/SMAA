import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        frame.setVisible(true);
    }

    public SMAA_Analyse_Tool() {
        browseButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                int returnval=chooser.showOpenDialog(null);

                if(returnval == JFileChooser.APPROVE_OPTION) {
                    textField2.setText(chooser.getSelectedFile().getPath());
                }
            }
        });
        browseButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("CSV Datei", "csv");
                chooser.addChoosableFileFilter(filter);
                int returnval=chooser.showOpenDialog(null);

                if(returnval == JFileChooser.APPROVE_OPTION) {
                    textField1.setText(chooser.getSelectedFile().getPath());
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String path = textField1.getText();
                String destination= textField2.getText();
                AGG agg = new AGG(path);

                double[][] raiTable = agg.calculateRAI(100000);
                agg.raiTableToCSV(raiTable);

            }
        });
    }
}



