package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.game.Move;
import com.jacobclarity.chessengine.uci.UciTokenData;

//Packet sent with search parameters on the current position
public class SearchPacket implements UciPacket
{
    private final Move[] searchMoves;

    private final boolean ponder;
    private final boolean infinite;

    private final long whiteTime; //millis
    private final long blackTime; //millis
    private final long whiteIncrement; //millis
    private final long blackIncrement; //millis
    private final int movesToGo; //to next time control
    private final int depth; //of search in plies
    private final int nodes;
    private final int mateWithin;

    public SearchPacket(Move[] searchMoves, boolean ponder, boolean infinite, long whiteTime, long blackTime,
                        long whiteIncrement, long blackIncrement, int movesToGo, int depth, int nodes, int mateWithin)
    {
        this.searchMoves = searchMoves;
        this.ponder = ponder;
        this.infinite = infinite;
        this.whiteIncrement = whiteIncrement;
        this.blackIncrement = blackIncrement;
        this.movesToGo = movesToGo;
        this.depth = depth;
        this.nodes = nodes;
        this.mateWithin = mateWithin;
    }


    public Move[] getSearchMoves()
    {
        return searchMoves;
    }

    public boolean shouldPonder()
    {
        return ponder;
    }

    public boolean isInfinite()
    {
        return infinite;
    }

    public long getWhiteTime()
    {
        return whiteTime;
    }

    public long getBlackTime()
    {
        return blackTime;
    }

    public long getWhiteIncrement()
    {
        return whiteIncrement;
    }

    public long getBlackIncrement()
    {
        return blackIncrement;
    }

    public int getMovesToGo()
    {
        return movesToGo;
    }

    public int getDepth()
    {
        return depth;
    }

    public int getNodes()
    {
        return nodes;
    }

    public int getMateWithin()
    {
        return mateWithin;
    }

    @Override
    public String toCommandString()
    {
        return null;
    }

    public static UciPacket parsePacket(UciTokenData[] tokens)
    {

    }
}
