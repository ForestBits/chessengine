package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.UciToken;
import com.jacobclarity.chessengine.uci.UciTokenData;

//sent by GUI to signal that a representation of the current board state should be printed
public class PrintBoardPacket implements UciPacket
{
    public static UciPacket parsePacket(UciTokenData[] tokens)
    {
        return new PrintBoardPacket();
    }

    @Override
    public String toCommandString()
    {
        return UciToken.PRINT_BOARD.getLiteralString();
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }
}
