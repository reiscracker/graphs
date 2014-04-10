package belegarbeit3;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author Jan Didschuneit, Simon Arnold
 */

public class Belegarbeit3 extends JFrame
{
    private MainList graph;
    private JMenuBar menuBar;
    private JMenu file, edit, faq;
    private JMenuItem graphNew, graphLoad, graphSave, nodeEdit, findWay, help, about;
            
    public Belegarbeit3()
    {
        /* characteristics of the main frame  */
        setLayout(new BorderLayout());
        setTitle("Graphomat 3000");
        setSize(700, 500);
        
        
        /* create and declare the canvas for drawning the graph */
        graph = new MainList();
        graph.setBounds(0, 21, 700, 479);
        graph.setVisible(true);
        graph.setBackground(Color.LIGHT_GRAY);
        add(graph, BorderLayout.CENTER);
        
        /* create and declare the menubar for a easier user handling */
        menuBar = new JMenuBar();
        menuBar.setVisible(true);
        add(menuBar, BorderLayout.NORTH);
        file = new JMenu("Datei");
        menuBar.add(file);
        edit = new JMenu("Bearbeiten");
        menuBar.add(edit);
        faq = new JMenu("Hilfe");
        menuBar.add(faq);
        graphNew = new JMenuItem("Graph neu");
        graphNew.addActionListener(new Action (0));
        file.add(graphNew);
        graphSave = new JMenuItem("Graph speichern");
        graphSave.addActionListener(new Action (1));
        file.add(graphSave);
        graphLoad = new JMenuItem("Graph laden");
        graphLoad.addActionListener(new Action (2));
        file.add(graphLoad);
        nodeEdit = new JMenuItem("Knoten ändern");
        nodeEdit.addActionListener(new Action (3));
        edit.add(nodeEdit);
        findWay = new JMenuItem("Weg finden");
        findWay.addActionListener(new Action (4));
        edit.add(findWay);
        help = new JMenuItem("FAQ");
        help.addActionListener(new Action (5));
        faq.add(help);
        about = new JMenuItem("About");
        about.addActionListener(new Action (6));
        faq.add(about);
        
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    
    public static void main(String[] args) 
    {
        new Belegarbeit3();
    }
    
    private void saveGraphToFile() {
        JFileChooser myFileChooser = new JFileChooser();
        if (myFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String fileName = myFileChooser.getSelectedFile() + ".graph";
            
            /* Write the object */
            try {
                ObjectOutputStream myOos = new ObjectOutputStream(new FileOutputStream(fileName));
                myOos.writeObject(graph);
                myOos.close();
            } 
            catch (IOException e) {
                System.err.println("Could not open file " + fileName);
            }
        }
    }
    private void loadGraphFromFile() {
        JFileChooser myFileChooser = new JFileChooser();
        if (myFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ObjectInputStream myOis = new ObjectInputStream(new FileInputStream(myFileChooser.getSelectedFile()));
                try {
                    graph = (MainList)myOis.readObject();
                    System.out.println("Object is: " + graph.getClass());
                    graph.repaint();
                }
                catch (ClassNotFoundException CNFE) {
                    
                }
                myOis.close();
            }
            catch (IOException IOE) {
                System.err.println("Could not read file " + myFileChooser.getSelectedFile());
            }
        }
    }
    
    class Action implements ActionListener
    {
        int optionNumber;
        Action (int number)
        {
              optionNumber = number;
        }
    
        @Override
        public void actionPerformed(ActionEvent e)
        {
          /* Graph neu */
          if (optionNumber == 0)
          {
              graph.newMainlist();
          }
          /* Graph speichern */
          if (optionNumber == 1)
          {
              saveGraphToFile();
          }
          /* Graph laden */
          if (optionNumber == 2)
          {
              loadGraphFromFile();
                System.out.println("Graph geladen\n");
          }
          /* Kante löschen */
          if (optionNumber == 3)
          {
                JLabel infoText = new JLabel("Kante löschen");
                infoText.setHorizontalAlignment(SwingConstants.CENTER);
                JLabel space = new JLabel(" ");
                JTextField firstNodeJTextField = new JTextField();
                JTextField lastNodeJTextField = new JTextField();
                final JComponent[] inputs = new JComponent[] 
                {
                    infoText,
                    space,
                    new JLabel("von Knoten"),
                    firstNodeJTextField,
                    new JLabel("zu Knoten"),
                    lastNodeJTextField,
                };
                JOptionPane.showMessageDialog(null, inputs, "Kante löschen", JOptionPane.PLAIN_MESSAGE);
                
                /* check for a correct input */
                if (firstNodeJTextField.getText().length() > 1 || lastNodeJTextField.getText().length() > 1)
                {
                    JOptionPane.showMessageDialog(rootPane, "Fehler: Falscher Datentyp eingegeben", "Fehlermeldung" , JOptionPane.ERROR_MESSAGE);
                }
                else if (firstNodeJTextField.getText().length() != 0 && lastNodeJTextField.getText().length() != 0)
                {
                    if (firstNodeJTextField.getText().equals(lastNodeJTextField.getText()))
                    {
                        JOptionPane.showMessageDialog(rootPane, "Fehler: Identische Eingabe", "Fehlermeldung" , JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        graph.hasSymbol(firstNodeJTextField.getText().charAt(0)).getAdjacentList().pop(lastNodeJTextField.getText().charAt(0));
                        graph.repaint();
                    }
                }
                else if (firstNodeJTextField.getText().length() == 0 || lastNodeJTextField.getText().length() == 0)
                {
                    JOptionPane.showMessageDialog(rootPane, "Fehler: Fehlende Eingabe", "Fehlermeldung" , JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Fehler: Unerwartetes Ereignis", "Fehlermeldung" , JOptionPane.ERROR_MESSAGE);
                }
          }
          /* Weg finden */
          if (optionNumber == 4)
          {
                /* create a own designed popup frame for information and input */
                JLabel infoText = new JLabel("Bitte geben Sie für die Wegsuche ein");
                infoText.setHorizontalAlignment(SwingConstants.CENTER);
                JLabel space = new JLabel(" ");
                JTextField firstNode = new JTextField();
                JTextField lastNode = new JTextField();
                final JComponent[] inputs = new JComponent[] 
                {
                    infoText,
                    space,
                    new JLabel("Erster Knoten (vom Typ Char!)"),
                    firstNode,
                    new JLabel("Zweiter Knoten (vom Typ Char!)"),
                    lastNode,
                };
                JOptionPane.showMessageDialog(null, inputs, "Wegsuche", JOptionPane.PLAIN_MESSAGE);
                
                /* check for a correct input */
                if (firstNode.getText().length() > 1 || lastNode.getText().length() > 1)
                {
                    JOptionPane.showMessageDialog(rootPane, "Fehler: Falscher Datentyp eingegeben", "Fehlermeldung" , JOptionPane.ERROR_MESSAGE);
                }
                else if (firstNode.getText().length() != 0 && lastNode.getText().length() != 0)
                {
                    if (firstNode.getText().equals(lastNode.getText()))
                    {
                        JOptionPane.showMessageDialog(rootPane, "Fehler: Identische Eingabe", "Fehlermeldung" , JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        System.out.println("You entered " + firstNode.getText() + ", " + lastNode.getText());
                        graph.findWay(firstNode.getText().charAt(0), lastNode.getText().charAt(0));
                        /* hier dann Übergabe der eingegeben Werte für die Suche an die ausgelagerte Methode */
                    }
                }
                else if (firstNode.getText().length() == 0 || lastNode.getText().length() == 0)
                {
                    JOptionPane.showMessageDialog(rootPane, "Fehler: Fehlende Eingabe", "Fehlermeldung" , JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Fehler: Unerwartetes Ereignis", "Fehlermeldung" , JOptionPane.ERROR_MESSAGE);
                }
          }
          /* FAQ */
          if (optionNumber == 5)
          {
                /* create a message dialog for help */
                JOptionPane.showMessageDialog(rootPane, 
                        "\nKnoten erstellen\n"
                        + "Klicken Sie mit der linken Maustaste in das Zeichenfeld und folgen \nSie dem erscheinenden Dialog bis der Knoten erstellt wurde\n\n"
                        + "Knoten ändern\n"
                        + "Klicken Sie mit der mittleren Maustaste auf einen Knoten, um sein Symbol zu ändern.\n\n"
                        + "Knoten löschen\n"
                        + "Rechtsklicken Sie auf den gewünschten Knoten, jener wird dann OHNE \nweitere Nachfrage gelöscht\n\n"
                        + "Kanten zeichnen\n"
                        + "Klicken Sie zuerst auf den Anfangsknoten, dann auf den Endknoten, \ndaraufhin wird die Kante gerichtet gezeichnet\n\n",
                        "Hilfethemen", 
                        JOptionPane.PLAIN_MESSAGE); 
          }
          /* About */
          if (optionNumber == 6)
          {
                /* create a given message dialog for about */
                JOptionPane.showMessageDialog(rootPane, 
                        "Ersteller: Simon Arnold und Jan Didschuneit\n"
                        + "Versionsnummer: 1.0\n"
                        + "Erstellt mit: Netbeans 7.3\n", 
                        "About", 
                        JOptionPane.PLAIN_MESSAGE); 
          }
        }
    }

}
