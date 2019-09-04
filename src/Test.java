import edu.princeton.cs.algs4.Picture;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test {
    private JPanel ipanel;

    public Test() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }
        });

        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (arg0.getSource() == cleanButton) {
                    out1.setText("");
                    textArea1.setText("");
                    out3.setText("");
                    textField1.setText("");
                    textField4.setText("");
                    textField2.setText("");
                    textField6.setText("");
                    textField7.setText("");
                    textField8.setText("");
                    textField9.setText("");
                }
            }
        });

        OKButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (e.getSource() == OKButton1) {
                        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
                        if (!textField1.getText().isEmpty() && !textField2.getText().isEmpty()) {
                            String v = textField1.getText();
                            String w = textField2.getText();
                            out1.setText(wordnet.sap(v, w) + "\n");
                        }
                        //out1.setText(textField1.getText());
                    }
                    }catch(Exception m){
                        out1.setText("输入错误");
                    }
            }
        });

        OKButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == OKButton2) {
                    WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
                    if (!textField4.getText().isEmpty()) {
                        String n = textField4.getText();
                        boolean result = wordnet.isNoun(n);
                        if (result)
                            textArea1.setText(wordnet.Search(n));
                        else
                            textArea1.setText("不是名词 \n");
                    }
                    //out1.setText(textField1.getText());
                }
            }
        });
        OKButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] nouns = new String[4];
                WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
                try {
                    if (e.getSource() == OKButton3) {

                        if (!textField6.getText().isEmpty() && !textField7.getText().isEmpty()&&!textField8.getText().isEmpty() && !textField9.getText().isEmpty()) {
                            for(int t = 0;t < 4;t++){
                                nouns[0] = textField6.getText();
                                nouns[1] = textField7.getText();
                                nouns[2] = textField8.getText();
                                nouns[3] = textField9.getText();
                                //out3.setText("The most irrelevant noun is " + ": " + outcast.outcast(nouns));
                            }
                            int id = -1;
                            int max = -1;
                            int[] distSum = new int[nouns.length];
                            for (int i = 0; i < nouns.length; i++) {
                                for (int j = 0; j < nouns.length; j++) {
                                    distSum[i] += wordnet.distance(nouns[i], nouns[j]);
                                }
                                if (distSum[i] > max) {
                                    max = distSum[i];
                                    id = i;
                                }
                            }
                            if (id == -1) {
                                throw new IllegalArgumentException("error");
                            }
                            out3.setText("The most irrelevant noun is " + ": " + nouns[id]);
                        }
                    }
                }catch(Exception m){
                    out3.setText("输入错误");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("单词网");
        frame.setContentPane(new Test().ipanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //Picture inputImg = new Picture("seam02.jpg");
    }

    private JLabel print1;
    private JTextField textField1;
    private JLabel print2;
    private JButton OKButton1;
    private JButton closeButton;
    private JTextField textField4;
    private JButton OKButton2;
    private JLabel print3;
    private JLabel print4;
    private JLabel out1;
    private JLabel out2;
    private JButton cleanButton;
    private JTextField textField2;
    private JLabel point5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JButton OKButton3;
    private JLabel print6;
    private JLabel out3;
    private JTextArea textArea1;
    private JList list1;

}
