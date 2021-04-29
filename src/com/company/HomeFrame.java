package com.company;

import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeFrame extends JFrame implements ActionListener {
    private JSpinner spinner ;
    private JLabel label;
    private JPanel panel;
    private JButton button;
    private int nbOfSenders = 0;

    public HomeFrame(){

        label = new JLabel();
        panel = new JPanel();
        button = new JButton();
        spinner = new JSpinner(new SpinnerNumberModel(0,0,20,1));
        spinner.setPreferredSize(new Dimension(250,20));
        button.setText("Next");
        button.setBackground(new Color(0xff1251));
        button.setPreferredSize(new Dimension(150,30));
        button.addActionListener(this);
        label.setBackground(Color.DARK_GRAY);
        label.setForeground(Color.lightGray);
        label.setPreferredSize(new Dimension(420,100));
        label.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        label.setText("  Choose the number of Branch offices: ");
        label.setOpaque(true);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        panel.setPreferredSize(new Dimension(100,200));
        panel.add(spinner);
        panel.add(button);

        this.setSize(420, 420);
        this.setTitle("Home");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(0,50));
        this.add(label, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()==button){
            nbOfSenders = (int) spinner.getValue();
            this.dispose();
        }}
    public int getNbOfSenders(){return nbOfSenders;}
}