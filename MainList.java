package belegarbeit3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Stack;

public class MainList extends JPanel implements Serializable
{
    private static MainNode firstNode;
    
    MainList() 
    {
        firstNode = null;
        addMouseListener(new myMouseListener());       
    }
    
    /**
     * Get number of elements in the list
     * @return number of elements
     */
    private int getLength()
    {
        if (firstNode == null)
        {
            return 0;
        }
        else
        {
            int count = 1;
            MainNode mainNodeIterator = firstNode;
            while (mainNodeIterator.getNextMainNode() != null)
            {
                count++;
                mainNodeIterator = mainNodeIterator.getNextMainNode();
            }
            return count;
        }
        
    }
    
    public void newMainlist ()
    {
        firstNode = null;
        repaint();
    }
    
    /**
     * Adds a new Node to the list
     * @param newMainNode 
     */
    public void push(MainNode newMainNode)
    {
        if (firstNode == null)
        {
            firstNode = newMainNode;
        }
        else
        {
            /* set Iterator to last element of the list */
            MainNode mainNodeIterator = firstNode;
            for (int i = 0; i < getLength() - 1; i++)
            {
                mainNodeIterator = mainNodeIterator.getNextMainNode();
            }
            mainNodeIterator.setNextMainNode(newMainNode);
        } 
    }
    
    public void changeSymbol(char oldSymbol, char newSymbol)
    {
        MainNode symbolToChange = hasSymbol(oldSymbol);
        if (symbolToChange != null) symbolToChange.setSymbol(newSymbol);
    }
    
    public void pop(char popThisSymbol)
    {
        if (firstNode == null)
        {
            System.err.println("List leer");
        }
        /* if first element is the one to pop */
        else if (firstNode.getSymbol() == popThisSymbol)
        {
            if (firstNode.getNextMainNode() != null)
            {
                firstNode = firstNode.getNextMainNode();
            }
            else
            {
                firstNode = null;
            }
        }
        else 
        {
            MainNode mainNodeIterator = firstNode;
            while (mainNodeIterator.getNextMainNode() != null && mainNodeIterator.getNextMainNode().getSymbol() != popThisSymbol)
            {
                mainNodeIterator = mainNodeIterator.getNextMainNode();
            }
            if (mainNodeIterator.getNextMainNode() == null)
            {
                System.err.println("Element nicht enthalten");
            }
            else
            {
                /* Link this element to the one after the next one */
                mainNodeIterator.setNextMainNode(mainNodeIterator.getNextMainNode().getNextMainNode());
            }
        }
    }
    
    /**
     * Check if symbol already exists in the list
     * @param symbolToCheck
     * @return The Node that contains this symbol, null if the symbol was not found
     */
    public static MainNode hasSymbol(char symbolToCheck)
    {
        if (firstNode == null)
        {
            return null;
        }
        /* if first element is the one */
        else
        {
            MainNode mainNodeIterator = firstNode;
            while ( mainNodeIterator.getNextMainNode() != null && mainNodeIterator.getSymbol() != symbolToCheck)
            {
                mainNodeIterator = mainNodeIterator.getNextMainNode();
            }
            /* We either found the symbol or the result is the outcome of the comparison with the last element.
             * If this is still not the symbol we are looking for, it does not exist */
            if (mainNodeIterator.getSymbol() == symbolToCheck)
            {
                return mainNodeIterator;
            }
            else 
            {
                return null;
            }
        }
    }
    
    private MainNode getNodeAtCoords(int x, int y) {
        MainNode mainNodeIterator = firstNode;
        /* Iterate through all nodes */
        for (int i = 0; i < getLength(); i++)
        {
            /* If the coordinates of the mouse are within this node */
            if ( (x > mainNodeIterator.getX() - 10 && x < mainNodeIterator.getX() + 10) &&
                (y > mainNodeIterator.getY() - 10 && y < mainNodeIterator.getY() + 10) )
            {
                return mainNodeIterator;
            }
            else
            {
                mainNodeIterator = mainNodeIterator.getNextMainNode();
            }
        }
        return null;
    }
    
    public Stack findWay(char startSymbol, char endSymbol)
    {
        Stack wanderedList = new Stack();
        LinkedList visitedList = new LinkedList();

        /* Check if both symbols exist */
        if (hasSymbol(startSymbol) == null || hasSymbol(endSymbol) == null) {
            return null;
        }
        
        wanderedList.push(startSymbol);
        visitedList.push(startSymbol);
        AdjacentNode adjacentIterator;
        
        System.out.println("Started at symbol " + startSymbol + ", trying to find " + endSymbol);
        
        while (!wanderedList.isEmpty()) {
            if ((char)wanderedList.lastElement() == endSymbol) break;
            /* Start at the first adjacent node from the last element we wandered to */
            adjacentIterator = hasSymbol((char)wanderedList.lastElement()).getAdjacentList().getFirstNode();
            
            /* Find an edge to an unvisited node */
            while (adjacentIterator != null && visitedList.contains(adjacentIterator.getSymbol())) {
                System.out.println("Symbol " + adjacentIterator.getSymbol() + " already visited, increasing..");
                adjacentIterator = adjacentIterator.getNextAdjacentNode();
            }
            /* Now we're either at the end of the list or found an unvisited node */
            if (adjacentIterator == null) {
                System.out.println("No unvisited edges from " + wanderedList.lastElement() + " .. pop");
                wanderedList.pop();
            }
            else {
                System.out.println("Pushing new symbol " + adjacentIterator.getSymbol());
                wanderedList.push(adjacentIterator.getSymbol());
                visitedList.push(adjacentIterator.getSymbol());
            }
        }
        System.out.println("Found way: " + wanderedList);
        
        
        
        
        return null;
    }
    
    public void paint(Graphics g)
    {
        super.paintComponent(g);
        if (firstNode != null)
        {
            MainNode mainNodeIterator = firstNode;
            for (int i = 0; i < getLength(); i++)
            {
                /* Let each node paint itself with all his edges */
                mainNodeIterator.paint(g);

                mainNodeIterator = mainNodeIterator.getNextMainNode();
            }
        }
    }
    
    class myMouseListener extends MouseAdapter implements MouseMotionListener
    {
        private MainNode startNode = null;
        
        public void mouseClicked(MouseEvent me) {
            
            /* Add a Node on leftclick */
            if (me.getButton() == MouseEvent.BUTTON1)
            {
                AddOnLeftclick(me);
            }

            /* Change a symbol on middle click */
            else if (me.getButton() == MouseEvent.BUTTON2)
            {
                ChangeOnMiddleclick(me);
            }

            /* Remove node on rightclick */
            else if (me.getButton() == MouseEvent.BUTTON3)
            {
                PopOnRightclick(me);
            }
                
            repaint();
        }
        public void mousePressed(MouseEvent me) 
        {
            startNode = getNodeAtCoords(me.getX(), me.getY());
        }
        public void mouseReleased(MouseEvent me) 
        {
            if (startNode != null)
            {
                MainNode endNode = getNodeAtCoords(me.getX(), me.getY());
                if (endNode != null && !startNode.equals(endNode) && 
                    startNode.getAdjacentList().hasSymbol(endNode.getSymbol()) == null &&
                    endNode.getAdjacentList().hasSymbol(startNode.getSymbol()) == null) 
                {
                    System.out.println("Edge was created from " + startNode.getSymbol() + " to " + endNode.getSymbol());
                    startNode.getAdjacentList().push(new AdjacentNode(endNode.getSymbol()));
                }
                startNode = null;
                repaint();
            }
        }
        
        private void AddOnLeftclick(MouseEvent me)
        {
            String getSymbol = JOptionPane.showInputDialog(null, "Enter character:", "", 1);
                if (getSymbol == null)
                {
                    System.err.println("Abbrechen gedrückt");
                }
                else if (getSymbol.length() == 1)
                {
                    char newSymbol = getSymbol.charAt(0);
                    if (hasSymbol(newSymbol) == null)
                    {
                        System.out.println("Pushing symbol " + newSymbol);
                        push(new MainNode(newSymbol, me.getX(), me.getY()));
                    }
                    else System.err.println("Symbol " + newSymbol + " already exists!");
                }
                else if (getSymbol.length() > 1)
                {
                    JOptionPane.showMessageDialog(null, "Fehler: Fehlerhafter Datentyp", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Fehler: Keine Eingabe", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
                }
        }
        
        private void ChangeOnMiddleclick(MouseEvent me)
        {
            MainNode nodeToChange = getNodeAtCoords(me.getX(), me.getY());
            if (nodeToChange != null)
            {
                String getSymbol = JOptionPane.showInputDialog(null, "Enter new character:", "", 1);
                if (getSymbol == null)
                {
                    System.err.println("Abbrechen gedrückt");
                }
                else if (getSymbol != null && getSymbol.length() == 1)
                {
                    char newSymbol = getSymbol.charAt(0);
                    if (hasSymbol(newSymbol) == null)
                    {
                        System.out.println("Changing symbol " + nodeToChange.getSymbol() + " to "+ newSymbol);
                        nodeToChange.setSymbol(newSymbol);
                    }
                    else System.err.println("Symbol " + newSymbol + " already exists!");
                }
                else if (getSymbol.length() > 1)
                {
                    JOptionPane.showMessageDialog(null, "Fehler: Fehlerhafter Datentyp", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Fehler: Keine Eingabe", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
                }
                }
        }
        
        private void PopOnRightclick(MouseEvent me)
        {
            /* Try to pop a node at these coords */
            MainNode nodeToPop = getNodeAtCoords(me.getX(), me.getY());
            if (nodeToPop != null) {
                char symbolToPop = nodeToPop.getSymbol();
                pop(nodeToPop.getSymbol());
                
                /*  Clean up all edges to this node */
                MainNode mainNodeIterator = firstNode;
                for (int i = 0; i < getLength(); i++)
                {
                    mainNodeIterator.getAdjacentList().pop(symbolToPop);
                    mainNodeIterator = mainNodeIterator.getNextMainNode();
                }
            }

            /* Try to pop an edge at these coords 
            MainNode mainNodeIterator = firstNode;
            for (int i = 0; i < getLength(); i++) 
            {
                int[][] edges = mainNodeIterator.getEdges();
                for (int j = 0; j < edges.length; j++)
                {
                    float m = (edges[j][3] - edges[j][1]) / (edges[j][2] - edges[j][0]);
                    float n = (edges[j][1] - m*edges[j][0]);
                    if ((me.getX()*m + n) >= me.getY() - 3 || (me.getX()*m + n) <= me.getY() + 3)
                        
                }
            } */
        }
    }
    
}