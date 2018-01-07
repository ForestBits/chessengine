package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.game.Board;
import com.jacobclarity.chessengine.uci.UciTokenData;

//sent from engine to GUI to print board state
public class BoardStatePacket implements UciPacket
{
    private final Board board;

    public BoardStatePacket(Board board)
    {
        this.board = board;
    }

    public static UciPacket parsePacket(UciTokenData[] tokens)
    {
        throw new RuntimeException("not implemented");
    }

    @Override
    public String toCommandString()
    {
        return board.toDebugString();
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }


}
