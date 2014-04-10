package belegarbeit3;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class MainNode extends Node implements Serializable
{
    private int x, y;
    private MainNode nextMainNode;
    private AdjacentList adjacentList;
    private final int CIRCLE_RADIUS = 10;
    
    MainNode(char symbol, int x, int y)
    {
         super(symbol);
         this.x = x;
         this.y = y;
         nextMainNode = null;
         adjacentList = new AdjacentList();
    }
    
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public AdjacentList getAdjacentList() 
    {
        return adjacentList;
    }
    
    public void setX(int newX)
    {
        x = newX;
    }
    public void setY(int newY)
    {
        y = newY;
    }
    
    public MainNode getNextMainNode()
    {
        return nextMainNode;
    }
    public void setNextMainNode(MainNode newNextMainNode)
    {
        nextMainNode = newNextMainNode;
    }
    
    /**
     * Get an array of every edge from this Node to another
     * @return 
     */
    public int[][] getEdges() 
    {
        int[][] edges = new int[adjacentList.getLength()][4];
        
        AdjacentNode adjacentNodeIterator = adjacentList.getFirstNode();
        for (int i = 0; i < edges.length; i++)
        {
            double circle1X = x;
            double circle1Y = y;
            double circle2X = MainList.hasSymbol(adjacentNodeIterator.getSymbol()).getX();
            double circle2Y = MainList.hasSymbol(adjacentNodeIterator.getSymbol()).getY();
            
            /* line function */
            double lineMX = (circle2Y - circle1Y) / (circle2X - circle1X);
            double lineN = circle1Y - (circle1X * lineMX);
            
            /* Calculate intersection with first circle */
            double helper1 = 1 + lineMX * lineMX;
            double helper2 = (2 * lineMX * lineN) - (2 * lineMX * circle1Y) - (2 * circle1X);
            double helper3 = (lineN * lineN) - (2 * lineN * circle1Y) + (circle1Y * circle1Y) - (CIRCLE_RADIUS * CIRCLE_RADIUS) + (circle1X * circle1X);
            
            /* The two intersection points: */
            double x1 = (-helper2 + Math.sqrt((helper2*helper2) - (4*helper1*helper3))) / (2*helper1);
            double x2 = (-helper2 - Math.sqrt((helper2*helper2) - (4*helper1*helper3))) / (2*helper1);
            
            /* The closer one will be where the edge is drawn to */
            if (Math.abs(circle2X - x1) < Math.abs(circle2X - x2)) {
                edges[i][0] = (int)x1;
                edges[i][1] = (int)((lineMX * x1) + lineN);
            }
            else {
                edges[i][0] = (int)x2;
                edges[i][1] = (int)((lineMX * x2) + lineN);
            }
            
            /* Calculate intersection with second circle */
            helper1 = 1 + lineMX * lineMX;
            helper2 = (2 * lineMX * lineN) - (2 * lineMX * circle2Y) - (2 * circle2X);
            helper3 = (lineN * lineN) - (2 * lineN * circle2Y) + (circle2Y * circle2Y) - (CIRCLE_RADIUS * CIRCLE_RADIUS) + (circle2X * circle2X);
            
            /* The two intersection points */
            x1 = (-helper2 + Math.sqrt((helper2*helper2) - (4*helper1*helper3))) / (2*helper1);
            x2 = (-helper2 - Math.sqrt((helper2*helper2) - (4*helper1*helper3))) / (2*helper1);
            
            /* The closer one will be where the edge is drawn to */
            if (Math.abs(x1 - circle1X) < Math.abs(x2 - circle1X)) {
                edges[i][2] = (int)x1;
                edges[i][3] = (int)((lineMX * x1) + lineN);
            }
            else {
                edges[i][2] = (int)x2;
                edges[i][3] = (int)((lineMX * x2) + lineN);
            }
            adjacentNodeIterator = adjacentNodeIterator.getNextAdjacentNode();
        }
        
        return edges;
    }
    
    /**
     * Object paints itself onto the canvas
     * @param g 
    */
    public void paint(Graphics g)
    {
        g.setColor(Color.red);
        g.drawOval(x - 10, y - 10, CIRCLE_RADIUS*2, CIRCLE_RADIUS*2);

        g.setColor(Color.black);
        g.drawString(""+symbol, x - 4, y + 4);
        
        int[][] edges = getEdges();
        for (int i = 0; i < edges.length; i++) 
        {
            g.drawLine(edges[i][0], edges[i][1], edges[i][2], edges[i][3]);
            g.setColor(Color.green);
            g.fillOval(edges[i][2] - 2, edges[i][3] - 2, 6, 6);
            g.setColor(Color.black);
        }
    }
}