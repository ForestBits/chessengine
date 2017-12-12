package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.game.Move;
import com.jacobclarity.chessengine.game.NotationException;
import com.jacobclarity.chessengine.uci.UciParseException;
import com.jacobclarity.chessengine.uci.UciToken;
import com.jacobclarity.chessengine.uci.UciTokenData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private final long moveWithin; //millis
    private final int movesToGo; //to next time control
    private final int depth; //of search in plies
    private final int nodes;
    private final int mateWithin;

    public SearchPacket(Move[] searchMoves, boolean ponder, boolean infinite, long whiteTime, long blackTime,
                        long whiteIncrement, long blackIncrement, long moveWithin, int movesToGo, int depth, int nodes, int mateWithin)
    {
        this.searchMoves = Arrays.copyOf(searchMoves, searchMoves.length);
        this.ponder = ponder;
        this.infinite = infinite;
        this.whiteTime = whiteTime;
        this.blackTime = blackTime;
        this.whiteIncrement = whiteIncrement;
        this.blackIncrement = blackIncrement;
        this.moveWithin = moveWithin;
        this.movesToGo = movesToGo;
        this.depth = depth;
        this.nodes = nodes;
        this.mateWithin = mateWithin;
    }


    public Move[] getSearchMoves()
    {
        return Arrays.copyOf(searchMoves, searchMoves.length);
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

    public long getMoveWithin()
    {
        return moveWithin;
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
        StringBuilder builder = new StringBuilder();

        builder.append("go ");

        if (searchMoves != null)
        {
            StringBuilder moveString = new StringBuilder();

            moveString.append("searchmoves");

            for (Move move : searchMoves)
                moveString.append(' ').append(move.toNotation());

            builder.append(moveString.toString());
        }

        if (ponder)
            builder.append(" ponder");

        if (whiteTime != 0)
            builder.append(" wtime ").append(whiteTime);

        if (blackTime != 0)
            builder.append(" btime ").append(blackTime);

        if (whiteIncrement != 0)
            builder.append(" winc ").append(whiteIncrement);

        if (blackIncrement != 0)
            builder.append(" binc ").append(blackIncrement);

        if (movesToGo != 0)
            builder.append(" movestogo ").append(movesToGo);

        if (depth != 0)
            builder.append(" depth ").append(depth);

        if (nodes != 0)
            builder.append(" nodes ").append(nodes);

        if (mateWithin != 0)
            builder.append(" mate ").append(mateWithin);

        if (moveWithin != 0)
            builder.append(" movetime ").append(moveWithin);

        if (infinite)
            builder.append(" infinite");

        return builder.toString();
    }

    public static UciPacket parsePacket(UciTokenData[] tokens)
    {
        //the values given here should be interpreted as "no value given"

        Move[] searchMoves = null;

        boolean ponder = false;
        boolean infinite = false;

        long whiteTime = 0;
        long blackTime = 0;
        long whiteIncrement = 0;
        long blackIncrement = 0;
        long moveWithin = 0;
        int movesToGo = 0;
        int depth = 0;
        int nodes = 0;
        int mateWithin = 0;
        
        //the subcommands for the go command can come in any order, so parse whatever comes next as an option

        for (int i = 1; i < tokens.length; ++i)
        {
            UciToken token = tokens[i].getToken();

            String[] values = tokens[i].getValues();

            switch (token)
            {
                case SEARCH_MOVES:
                    //searchmoves <move1> ... <moveN>

                    if (values.length == 0)
                        throw new UciParseException("searchmoves token included, but no move list found");

                    List<Move> moves = new ArrayList<Move>();

                    for (String move : values)
                        try
                        {
                            moves.add(Move.fromNotation(move));
                        }

                        catch (NotationException ex)
                        {
                            throw new UciParseException("searchmoves list contained error: " + ex.getMessage());
                        }

                    searchMoves = moves.toArray(new Move[moves.size()]);

                    break;

                case PONDER:
                    //ponder

                    ponder = true;

                    break;

                case WHITE_TIME:
                    //wtime <x>

                    if (values.length != 1)
                        throw new UciParseException("wtime token should be followed by one integer value");

                    try
                    {
                        whiteTime = Long.parseLong(values[0]);
                    }

                    catch (NumberFormatException ex)
                    {
                        throw new UciParseException("wtime value is not a number");
                    }

                    if (whiteTime < 0)
                        throw new UciParseException("wtime value must be greater than 0");

                    break;

                case BLACK_TIME:
                    //btime <x>

                    if (values.length != 1)
                        throw new UciParseException("btime token should be followed by one integer value");

                    try
                    {
                        blackTime = Long.parseLong(values[0]);
                    }

                    catch (NumberFormatException ex)
                    {
                        throw new UciParseException("btime value is not a number");
                    }

                    if (blackTime < 0)
                        throw new UciParseException("btime value must be greater than 0");
                    
                    break;
                
                case WHITE_INCREMENT:
                    //winc <x>

                    if (values.length != 1)
                        throw new UciParseException("winc token should be followed by one integer value");

                    try
                    {
                        whiteIncrement = Long.parseLong(values[0]);
                    }

                    catch (NumberFormatException ex)
                    {
                        throw new UciParseException("winc value is not a number");
                    }

                    if (whiteIncrement < 0)
                        throw new UciParseException("winc value must be greater than 0");
                    
                    break;
                
                case BLACK_INCREMENT:
                    //binc <x>

                    if (values.length != 1)
                        throw new UciParseException("binc token should be followed by one integer value");

                    try
                    {
                        blackIncrement = Long.parseLong(values[0]);
                    }

                    catch (NumberFormatException ex)
                    {
                        throw new UciParseException("binc value is not a number");
                    }

                    if (blackIncrement < 0)
                        throw new UciParseException("binc value must be greater than 0");
                    
                    break;
                
                case MOVES_TO_GO:
                    //movestogo <x>

                    if (values.length != 1)
                        throw new UciParseException("movestogo token should be followed by one integer value");

                    try
                    {
                        movesToGo = Integer.parseInt(values[0]);
                    }

                    catch (NumberFormatException ex)
                    {
                        throw new UciParseException("movestogo value is not a number");
                    }

                    if (movesToGo < 0)
                        throw new UciParseException("movestogo value must be greater than 0");
                    
                    break;
                
                case DEPTH:
                    //depth <x>

                    if (values.length != 1)
                        throw new UciParseException("depth token should be followed by one integer value");

                    try
                    {
                        depth = Integer.parseInt(values[0]);
                    }

                    catch (NumberFormatException ex)
                    {
                        throw new UciParseException("depth value is not a number");
                    }

                    if (depth < 0)
                        throw new UciParseException("depth value must be greater than 0");
                    
                    break;
                
                case NODES:
                    //nodes <x>

                    if (values.length != 1)
                        throw new UciParseException("nodes token should be followed by one integer value");

                    try
                    {
                        nodes = Integer.parseInt(values[0]);
                    }

                    catch (NumberFormatException ex)
                    {
                        throw new UciParseException("nodes value is not a number");
                    }

                    if (nodes < 0)
                        throw new UciParseException("nodes value must be greater than 0");
                    
                    break;
                
                case MATE:
                    //mate <x>

                    if (values.length != 1)
                        throw new UciParseException("mate token should be followed by one integer value");

                    try
                    {
                        mateWithin = Integer.parseInt(values[0]);
                    }

                    catch (NumberFormatException ex)
                    {
                        throw new UciParseException("mate value is not a number");
                    }

                    if (mateWithin < 0)
                        throw new UciParseException("mate value must be greater than 0");
                    
                    break;
                
                case MOVE_TIME:
                    //movetime <x>

                    if (values.length != 1)
                        throw new UciParseException("movetime token should be followed by one integer value");

                    try
                    {
                        moveWithin = Long.parseLong(values[0]);
                    }

                    catch (NumberFormatException ex)
                    {
                        throw new UciParseException("movetime value is not a number");
                    }

                    if (moveWithin < 0)
                        throw new UciParseException("movetime value must be greater than 0");

                    break;
                
                case INFINITE:
                    //infinite

                    infinite = true;
                    
                    break;
            } //end switch
        } //end for loop going over each option

        //at this point, we have parsed all the provided options

        return new SearchPacket(searchMoves, ponder, infinite, whiteTime, blackTime, whiteIncrement, blackIncrement,
                                moveWithin, movesToGo, depth, nodes, mateWithin);
    }
}
