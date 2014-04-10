package belegarbeit3;

import java.io.Serializable;

public class AdjacentNode extends Node implements Serializable
{
    private AdjacentNode nextAdjacentNode = null;

    AdjacentNode(char symbol)
    {
        super(symbol);
    }
    
    public AdjacentNode getNextAdjacentNode()
    {
        return nextAdjacentNode;
    }
    public void setNextAdjacentNode(AdjacentNode newNextListNode)
    {
        nextAdjacentNode = newNextListNode;
    }
    
}