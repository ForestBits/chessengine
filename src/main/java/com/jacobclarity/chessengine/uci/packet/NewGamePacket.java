package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.UciTokenData;

/*
 * Packet sent by GUI to indicate previous game state
 * should be cleared, as the next position is for a different game
 */
public class NewGamePacket implements UciPacket
{
    @Override
    public String toCommandString()
    {
        return "ucinewgame";
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }

    public static UciPacket parsePacket(UciTokenData tokens[])
    {
        return new NewGamePacket();
    }
}
