package org.svens187.inputparser.sample;


import javafx.scene.input.KeyCode;
import org.svens187.inputparser.InputFormatter;
import org.svens187.inputparser.arrayinit.TranslateArray;
import org.svens187.inputparser.date.Translate;
import org.svens187.inputparser.url.URL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class Gui extends JDialog implements KeyListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea1;
    private JTextArea outputTextArea;
    private JLabel output;
    private JButton openFileButton;
    private JButton GOButton;
    private JLabel validity;
    private JComboBox comboBox1;

    final int OPTION_ONE_URL = 0x00;
    final int OPTION_TWO_DATE = 0x01;
    final int OPTION_THREE_ARRAYS = 0x02;

    public Gui() {
        setContentPane(contentPane);
        setModal(true);
        validity.setBackground(Color.RED);
        validity.repaint();
        getRootPane().setDefaultButton(buttonOK);
        outputTextArea.setEnabled(false);
        textArea1.addKeyListener(this);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFileChooser();
            }
        });


        GOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

        switchAndPerform();


            }
        });


        comboBox1.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            System.out.println(comboBox1.getSelectedIndex());
                                            if (comboBox1.getSelectedIndex() == OPTION_ONE_URL)
                                                textArea1.setText("for example:\nhttp://www.tianya.cn:8080/one/def?name=yaojg&no=1#details");
                                            if (comboBox1.getSelectedIndex() == OPTION_TWO_DATE) {
                                                textArea1.setText("This method should get every date:\n");
                                                textArea1.append("31.12.1999\n");
                                                textArea1.append("12-31-1999\n");
                                                textArea1.append("1999-12-31\n");
                                            }
                                            if (comboBox1.getSelectedIndex() == OPTION_THREE_ARRAYS) {
                                                textArea1.setText("Beispiele für die Initialisierung von Integer-Arrays und eine  Konvertierung short zu Unicode:\n ");
                                                textArea1.append("{1,2,43,65}\n");
                                                textArea1.append("\n-->  \\u0001\\u0002\\u002b\\u0041");
                                            }
                                        }
                                    }
        );

    }

    Date date;


    private void switchAndPerform(){
        switch (comboBox1.getSelectedIndex()) {
            case OPTION_ONE_URL:
                formatURL();
                break;
            case OPTION_TWO_DATE:
                formatDate();
                break;
            case OPTION_THREE_ARRAYS:
                initArray();
                break;
            default:
                break;
        }
    }
    private void initArray() {

        final InputFormatter<String> arrayFormatter = new TranslateArray();
        String ret = arrayFormatter.parse(textArea1.getText());
        outputTextArea.setText(ret);
        if (arrayFormatter.isValid(textArea1.getText())) validity.setText("valid");
        else validity.setText("not valid");
    }

    private void formatURL() {
        URL url = new URL();
        String ret = url.parse(textArea1.getText());
        outputTextArea.setText(ret);
        if (url.isValid(textArea1.getText())) validity.setText("valid");
        else validity.setText("not valid");
    }


    private void formatDate() {
        final InputFormatter<Date> test = new Translate();
        String ret = test.parse(textArea1.getText());
        if (ret.equals("")) {
            date = test.format(textArea1.getText());
            outputTextArea.setText(date.toString());
            if (test.isValid(textArea1.getText())) validity.setText("valid");
        } else {
            validity.setText("not valid");
            outputTextArea.setText(ret);
        }
    }

    private void onFileChooser() {
        JFileChooser chooser = new JFileChooser();

        int returnVal = chooser.showOpenDialog(openFileButton);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            Charset charset = Charset.forName("UTF-8");
            Path path = null;
            String paths = chooser.getSelectedFile().getAbsolutePath();
            path = Paths.get(paths, "");
            try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    //separate all csv fields into string array
                    textArea1.append(line);
                }
            } catch (IOException e1) {
                System.err.println(e1);
            }


        }
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        Gui dialog = new Gui();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        System.out.println(textArea1.getText());
        switchAndPerform();




        int keyCodde = e.getKeyCode();
         System.out.println("Keycode: " +keyCodde + " " +KeyEvent.getKeyText(keyCodde));
    }
}
