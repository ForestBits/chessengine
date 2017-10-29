package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.UciTokenData;

/*
 * Packet sent from GUI to engine to see if engine is ready
 * for more commands
 */
public class IsReadyPacket implements UciPacket
{
    @Override
    public String toCommandString()
    {
        return "isready";
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }

    public static UciPacket parsePacket(UciTokenData tokens[])
    {
        return new IsReadyPacket();
    }
}
