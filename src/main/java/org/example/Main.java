package org.example;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import java.util.Scanner;
import javax.swing.JComponent.*;


public class Main {
    static JFrame frame;
    static JPanel panel;

    static JLabel status = new JLabel("");

    static JTextField keyword; //osu query search

    static JComboBox statusList = new JComboBox();
    static JSpinner minDifficulty = new JSpinner(); //osu query search
    static JSpinner maxDifficulty = new JSpinner(); //osu query search

    static JSpinner minCS = new JSpinner(); //osu query search
    static JSpinner maxCS = new JSpinner(); //osu query search

    static JSpinner minAR = new JSpinner(); //osu query search
    static JSpinner maxAR = new JSpinner(); //osu query search

    static JSpinner minOD = new JSpinner(); //osu query search
    static JSpinner maxOD = new JSpinner(); //osu query search

    static JSpinner minHP = new JSpinner(); //osu query search
    static JSpinner maxHP = new JSpinner(); //osu query search

    static JSpinner minBPM = new JSpinner(); //osu query search
    static JSpinner maxBPM = new JSpinner(); //osu query search

    static JSpinner minLength = new JSpinner(); //osu query search
    static JSpinner maxLength = new JSpinner(); //osu query search

    static JButton download; //download button for maps
    static File downloadDirectory = new File("");


    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, ParseException {
        //this is the frame
        System.out.println("\\\\");
        frame = new JFrame("Beatmap Downloader");
        //graph frame init
        createLayout(); //this function creates all the UI
        frame.setSize(450, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        System.out.println(keyword.getText());


    }

    private static void createLayout() {
        panel = new JPanel(); //panel init
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //creating vertical stack
        panel.add(Box.createVerticalGlue()); //vertical stack start
        panel.add(createTitle()); //prompt is the text which gives user info
        panel.add(createSearch());
        panel.add(createStatusDropbox());
        panel.add(createRange(minDifficulty, maxDifficulty, 0, 9.9, "Difficulty", 0.1));
        panel.add(createRange(minCS, maxCS, 0, 9.9, "Circle Size", 0.1));
        panel.add(createRange(minAR, maxAR, 0, 9.9, "Approach Rate", 0.1));
        panel.add(createRange(minOD, maxOD, 0, 9.9, "Overall Difficulty", 0.1));
        panel.add(createRange(minBPM, maxBPM, 0, 300, "BPM", 1));
        panel.add(createRange(minLength, maxLength, 0, 3000, "Length (in seconds)", 0.1));
        panel.add(createFileChooser());
        panel.add(createDownloadButton());
        panel.add(createStatus());
        panel.add(Box.createVerticalGlue()); //vertical stack end
        //adds to the frame
        frame.add(panel);
        frame.pack();
    }

    private static JPanel createFileChooser() {
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);

        JButton button = new JButton("Select Osu Folder");
        final JLabel label = new JLabel();

        button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
                downloadDirectory = fileChooser.getSelectedFile();
                button.setText("Selected: " + downloadDirectory.getPath());
            }
        });

        panel.add(button);
        panel.add(label);
        return panel;
    }

    private static JPanel createTitle() {
        JPanel promptPanel = new JPanel();
        JLabel label = new JLabel("Bootleg Osu Supporter");
        label.setFont(new Font("Times New Roman", Font.BOLD, 40));
        promptPanel.add(label);
        return promptPanel;
    }

    private static  JPanel createStatusDropbox() {
        JPanel promptPanel = new JPanel();
        JLabel hint = new JLabel("Map Status: ");
        hint.setFont(new Font("Times New Roman", Font.BOLD, 20));
        promptPanel.add(hint);
        String[] statusStrings = {"Unranked", "Ranked", "Approved", "Qualified", "Loved"};
        for (String status:statusStrings) {
            statusList.addItem(status);
        }
        statusList.setSelectedIndex(1);
        promptPanel.add(statusList);
        return promptPanel;
    }

    private static JPanel createSearch() {
        JPanel promptPanel = new JPanel();
        keyword = new JTextField("help");
        keyword.setPreferredSize(new Dimension(200, 35));
        JLabel hint = new JLabel("Keyword: ");
        hint.setFont(new Font("Times New Roman", Font.BOLD, 20));
        promptPanel.add(hint);
        keyword.setFont(new Font("Times New Roman", Font.BOLD, 20));
        promptPanel.add(keyword);
        return promptPanel;
    }

    private static JPanel createRange(JSpinner min, JSpinner max, double minValue, double maxValue, String label, double stepSize) {
        JPanel promptPanel = new JPanel();
        JLabel hint = new JLabel(label + ": ");
        hint.setFont(new Font("Times New Roman", Font.BOLD, 20));
        promptPanel.add(hint);

        SpinnerModel minModel = new SpinnerNumberModel(minValue, minValue, maxValue, stepSize);
        min.setModel(minModel);
        min.setPreferredSize(new Dimension(100, 35));
        min.setFont(new Font("Times New Roman", Font.BOLD, 20));
        promptPanel.add(min);

        JLabel spacer = new JLabel(" to ");
        hint.setFont(new Font("Times New Roman", Font.BOLD, 20));
        promptPanel.add(spacer);

        SpinnerModel maxModel = new SpinnerNumberModel(maxValue, minValue, maxValue, stepSize);
        max.setModel(maxModel);
        max.setPreferredSize(new Dimension(100, 35));
        max.setFont(new Font("Times New Roman", Font.BOLD, 20));
        promptPanel.add(max);
        return promptPanel;
    }

    private static JPanel createDownloadButton() {
        JPanel promptPanel = new JPanel();
        download = new JButton("Search and download");
        download.setPreferredSize(new Dimension(300, 50));
        download.setFont(new Font("Times New Roman", Font.BOLD, 20));
        download.addActionListener(e -> {
            //System.out.println(minDifficulty.getValue());
            //System.out.println((double) (Integer) minDifficulty.getValue());
            //System.out.println(keyword.getText());
            //your actions
            try {
                SearchHandler.searchBeatmaps(
                        keyword.getText(),
                        statusList.getSelectedIndex(),
                        (Double) minDifficulty.getValue(),
                        (Double) maxDifficulty.getValue(),
                        (Double) minCS.getValue(),
                        (Double) maxCS.getValue(),
                        (Double) minAR.getValue(),
                        (Double) maxAR.getValue(),
                        (Double) minOD.getValue(),
                        (Double) maxOD.getValue(),
                        (Double) minBPM.getValue(),
                        (Double) maxBPM.getValue(),
                        (Double) minLength.getValue(),
                        (Double) maxLength.getValue(),
                        status,
                        downloadDirectory,
                        0
                );
                updateFrame();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        promptPanel.add(download);
        return promptPanel;
    }

    private static JPanel createStatus() {
        JPanel promptPanel = new JPanel();
        status.setFont(new Font("Times New Roman", Font.BOLD, 10));
        promptPanel.add(status);
        return promptPanel;
    }

    public static void updateFrame() {
        frame.getContentPane().removeAll(); //remove current frame components
        createLayout(); //redraw the components
        frame.setSize(500, 800);
        frame.toFront();
        frame.requestFocus();
    }
}