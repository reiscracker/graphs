package belegarbeit3;

import java.io.Serializable;

public class AdjacentList implements Serializable
{
    private AdjacentNode firstNode;

    public AdjacentList() {
        firstNode = null;
    }
    
    public int getLength()
    {
        if (firstNode == null)
        {
            return 0;
        }
        else
        {
            int count = 1;
            AdjacentNode adjacentNodeIterator = firstNode;
            while (adjacentNodeIterator.getNextAdjacentNode() != null)
            {
                count++;
                adjacentNodeIterator = adjacentNodeIterator.getNextAdjacentNode();
            }
            return count;
        }
        
    }
    
    /**
     * Adds a new Node to the list
     * @param newAdjacentNode
     */
    public void push(AdjacentNode newAdjacentNode)
    {
        if (firstNode == null)
        {
            firstNode = newAdjacentNode;
        }
        else
        {
            /* set Iterator to last element of the list */
            AdjacentNode adjacentNodeIterator = firstNode;
            for (int i = 0; i < getLength() - 1; i++)
            {
                adjacentNodeIterator = adjacentNodeIterator.getNextAdjacentNode();
            }
            adjacentNodeIterator.setNextAdjacentNode(newAdjacentNode);
        } 
    }
    
    public void changeSymbol(char oldSymbol, char newSymbol)
    {
        AdjacentNode symbolToChange = hasSymbol(oldSymbol);
        if (symbolToChange != null) symbolToChange.setSymbol(newSymbol);
    }
    
    public void pop(char popThisSymbol)
    {
        if (firstNode == null)
        {
            System.err.println("AdjazenzList leer");
        }
        /* if first element is the one to pop */
        else if (firstNode.getSymbol() == popThisSymbol)
        {
            if (firstNode.getNextAdjacentNode() != null)
            {
                firstNode = firstNode.getNextAdjacentNode();
            }
            else
            {
                firstNode = null;
            }
        }
        else 
        {
            AdjacentNode adjacentNodeIterator = firstNode;
            while (adjacentNodeIterator.getNextAdjacentNode() != null && adjacentNodeIterator.getNextAdjacentNode().getSymbol() != popThisSymbol)
            {
                adjacentNodeIterator = adjacentNodeIterator.getNextAdjacentNode();
            }
            if (adjacentNodeIterator.getNextAdjacentNode() == null)
            {
                System.err.println("Element nicht enthalten");
            }
            else
            {
                /* Link this element to the one after the next one */
                adjacentNodeIterator.setNextAdjacentNode(adjacentNodeIterator.getNextAdjacentNode().getNextAdjacentNode());
            }
        }
    }
    
    /**
     * Check if symbol already exists in the list
     * @param symbolToCheck
     * @return The Node that contains this symbol, null if the symbol was not found
     */
    public AdjacentNode hasSymbol(char symbolToCheck)
    {
        if (firstNode == null)
        {
            return null;
        }
        /* if first element is the one */
        else
        {
            AdjacentNode adjacentNodeIterator = firstNode;
            while ( adjacentNodeIterator.getNextAdjacentNode() != null && adjacentNodeIterator.getSymbol() != symbolToCheck)
            {
                adjacentNodeIterator = adjacentNodeIterator.getNextAdjacentNode();
            }
            /* We either found the symbol or the result is the outcome of the comparison with the last element.
             * If this is still not the symbol we are looking for, it does not exist */
            if (adjacentNodeIterator.getSymbol() == symbolToCheck)
            {
                return adjacentNodeIterator;
            }
            else 
            {
                return null;
            }
        }
    }

    public AdjacentNode getFirstNode() {
        return firstNode;
    }
}