package com.simulation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyFrame extends JFrame {
    static Thread game;
    static JFrame frame;
    static MyPanel simulation = new MyPanel();
    static int screenshotNum = 1;

    public static void start() throws IOException {
        frame = new JFrame("Jakub Michnicki Grain Growth");
        JPanel panel = new MyPanel();

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton addGrains = new JButton("Add");
        JButton changeSize = new JButton("Change XY");
        JButton clearTabs = new JButton("Clear");
        JButton step = new JButton("Step");
        JButton changeBCN = new JButton("Change");
        JButton saveToImg = new JButton("Save Image");
        JLabel sizeLabel = new JLabel("Size");
        JLabel grainsLabel = new JLabel("How many grains");
        JLabel iterationsLabel = new JLabel("Iterations amount");
        JLabel nucleationLabel = new JLabel("Nucleation type");
        JLabel ktLabel = new JLabel("kt");
        JLabel JLabel = new JLabel("J");
        JLabel NLabel = new JLabel("N");
        JLabel xLabel = new JLabel("XX");
        JLabel yLabel = new JLabel("YY");
        JLabel dispLabel = new JLabel("Dispersion");
        JLabel everyILabel = new JLabel("x its");
        JLabel embryosLabel = new JLabel("x embryo");
        JTextField widthTF = new JTextField();
        JTextField heightTF = new JTextField();
        JTextField grainsTF = new JTextField();
        JTextField iterationsTF = new JTextField();
        JTextField JTF = new JTextField();
        JTextField ktTF = new JTextField();
        JTextField NTF = new JTextField();
        JTextField xTF = new JTextField();
        JTextField yTF = new JTextField();
        JTextField dispTF = new JTextField();
        JTextField everyITF = new JTextField();
        JTextField embryosTF = new JTextField();
        String[] boundaryCondition = {"Periodic", "Absorbing"};
        String[] neighborhood = {"VonNeumann", "HexRandom"};
        String[] nucleation = {"Random", "Homogeneous"};
        String[] simEnergy = {"Simulation", "Energy"};
        String[] method = {"MC", "SRX"};
        String[] whatToShow = {"None", "Before", "After"};
        JComboBox boundaryCombo = new JComboBox(boundaryCondition);
        JComboBox neighborhoodCombo = new JComboBox(neighborhood);
        JComboBox nucleationCombo = new JComboBox(nucleation);
        JComboBox simEnergyCombo = new JComboBox(simEnergy);
        JComboBox methodCombo = new JComboBox(method);
        JComboBox whatToShowCombo = new JComboBox(whatToShow);
        sizeLabel.setBounds(simulation.getWidth(), 10, 100, 15);
        widthTF.setBounds(simulation.getWidth(), 30, 60, 20);
        widthTF.setText("650");
        heightTF.setBounds(simulation.getWidth() + 60, 30, 60, 20);
        heightTF.setText("650");
        changeSize.setBounds(simulation.getWidth(), 55, 100, 20);
        startButton.setBounds(simulation.getWidth(), 80, 100, 20);
        stopButton.setBounds(simulation.getWidth(), 105, 100, 20);
        clearTabs.setBounds(simulation.getWidth(), 130, 100, 20);
        grainsLabel.setBounds(simulation.getWidth(), 155, 100, 15);
        grainsTF.setBounds(simulation.getWidth(), 175, 100, 20);
        grainsTF.setText("10");
        addGrains.setBounds(simulation.getWidth(), 200, 100, 20);
        iterationsLabel.setBounds(simulation.getWidth(), 225, 100, 20);
        iterationsTF.setBounds(simulation.getWidth(), 250, 100, 20);
        iterationsTF.setText("1");
        step.setBounds(simulation.getWidth(), 275, 100, 20);
        boundaryCombo.setBounds(simulation.getWidth(), 300, 100, 20);
        neighborhoodCombo.setBounds(simulation.getWidth(), 325, 100, 20);
        changeBCN.setBounds(simulation.getWidth(), 350, 100, 20);
        nucleationLabel.setBounds(simulation.getWidth(), 375, 100, 15);
        nucleationCombo.setBounds(simulation.getWidth(), 395, 100, 20);
        saveToImg.setBounds(simulation.getWidth(), 420, 100, 20);
        ktLabel.setBounds(simulation.getWidth() + 125, 10, 50, 15);
        JLabel.setBounds(simulation.getWidth() + 50 + 125, 10, 50, 15);
        ktTF.setBounds(simulation.getWidth() + 125, 30, 50, 20);
        ktTF.setText("2");
        JTF.setBounds(simulation.getWidth() + 125 + 50, 30, 50, 20);
        JTF.setText("1");
        NLabel.setBounds(simulation.getWidth() + 125, 55, 50, 15);
        NTF.setBounds(simulation.getWidth() + 125, 75, 50, 20);
        NTF.setText("10");
        xLabel.setBounds(simulation.getWidth() + 125, 100, 33, 15);
        yLabel.setBounds(simulation.getWidth() + 125 + 33, 100, 33, 15);
        dispLabel.setBounds(simulation.getWidth() + 125 + 66, 100, 33, 15);
        xTF.setBounds(simulation.getWidth() + 125, 120, 33, 20);
        yTF.setBounds(simulation.getWidth() + 125 + 33, 120, 33, 20);
        dispTF.setBounds(simulation.getWidth() + 125 + 66, 120, 33, 20);
        xTF.setText("1");
        yTF.setText("1");
        dispTF.setText("20");
        simEnergyCombo.setBounds(simulation.getWidth() + 125, 155, 100, 20);
        methodCombo.setBounds(simulation.getWidth() + 125, 180, 100, 20);
        everyILabel.setBounds(simulation.getWidth() + 125, 205, 50, 15);
        embryosLabel.setBounds(simulation.getWidth() + 125 + 50, 205, 70, 15);
        everyITF.setBounds(simulation.getWidth() + 125, 225, 50, 20);
        embryosTF.setBounds(simulation.getWidth() + 125 + 50, 225, 50, 20);
        everyITF.setText("5");
        embryosTF.setText("8");
        whatToShowCombo.setBounds(simulation.getWidth() + 125, 250, 100, 20);


        frame.add(startButton);
        frame.add(sizeLabel);
        frame.add(widthTF);
        frame.add(heightTF);
        frame.add(changeSize);
        frame.add(stopButton);
        frame.add(clearTabs);
        frame.add(addGrains);
        frame.add(saveToImg);
        frame.add(grainsTF);
        frame.add(grainsLabel);
        frame.add(iterationsLabel);
        frame.add(iterationsTF);
        frame.add(step);
        frame.add(boundaryCombo);
        frame.add(nucleationCombo);
        frame.add(nucleationLabel);
        frame.add(changeBCN);
        frame.add(neighborhoodCombo);
        frame.add(ktTF);
        frame.add(JTF);
        frame.add(JLabel);
        frame.add(ktLabel);
        frame.add(NTF);
        frame.add(NLabel);
        frame.add(xLabel);
        frame.add(yLabel);
        frame.add(dispLabel);
        frame.add(xTF);
        frame.add(yTF);
        frame.add(dispTF);
        frame.add(simEnergyCombo);
        frame.add(methodCombo);
        frame.add(embryosLabel);
        frame.add(embryosTF);
        frame.add(everyILabel);
        frame.add(everyITF);
        frame.add(whatToShowCombo);
        frame.add(panel);


        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game = new Thread(new MyPanel());
                game.start();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.interrupt();
            }
        });
        addGrains.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int grains = Integer.parseInt(grainsTF.getText());
                simulation.setGrains(grains);
                SwingUtilities.updateComponentTreeUI(frame);
                MyPanel.insert();
            }
        });
        changeSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isNumeric(widthTF.getText()) || !isNumeric(heightTF.getText()))
                    JOptionPane.showMessageDialog(new JFrame(), "Width and height have to be numbers that are above 0.", "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    int width = Integer.parseInt(widthTF.getText());
                    int height = Integer.parseInt(heightTF.getText());
                    simulation.setRes(width, height);
                    SwingUtilities.updateComponentTreeUI(frame);

                }
            }
        });
        clearTabs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MyPanel.clearTabs();
            }
        });
        step.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int step = Integer.parseInt(iterationsTF.getText());
                MyPanel panel = new MyPanel();
                panel.step(step);
            }
        });
        changeBCN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String a = (String) boundaryCombo.getItemAt((boundaryCombo.getSelectedIndex()));
                String b = (String) neighborhoodCombo.getItemAt((neighborhoodCombo.getSelectedIndex()));
                String c = (String) nucleationCombo.getItemAt((nucleationCombo.getSelectedIndex()));
                String d = (String) simEnergyCombo.getItemAt((simEnergyCombo.getSelectedIndex()));
                String f = (String) methodCombo.getItemAt((methodCombo.getSelectedIndex()));
                double kt = Double.parseDouble(ktTF.getText());
                double J = Double.parseDouble(JTF.getText());
                double XX = Double.parseDouble(xTF.getText());
                double YY = Double.parseDouble(yTF.getText());
                int dispersion = Integer.parseInt(dispTF.getText());
                int N = Integer.parseInt(NTF.getText());
                simulation.setBCnN(MyPanel.BoundaryCondition.valueOf(a), MyPanel.Neighborhood.valueOf(b));
                simulation.setNucleation(MyPanel.Nucleation.valueOf(c));
                simulation.setJkt(J, kt);
                simulation.setDXXYY(XX, YY, dispersion, N);
                simulation.setEnergy(MyPanel.ShowType.valueOf(d));
                simulation.setMethod(MyPanel.MethodName.valueOf(f));
                simulation.setEmbryos(Integer.parseInt(everyITF.getText()), Integer.parseInt(embryosTF.getText()));
            }
        });
        saveToImg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                panel.paint(img.getGraphics());
                String fileName = "ss" + screenshotNum + ".png";
                File outputfile = new File(fileName);
                try {
                    ImageIO.write(img, "png", outputfile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                screenshotNum++;
            }
        });
    }

    JFrame giveFrame() {
        return frame;
    }

    public static boolean isNumeric(String str) {
        try {
            for (char ch : str.toCharArray()) {
                Double.parseDouble(String.valueOf(ch));
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}