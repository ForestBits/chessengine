package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.UciTokenData;

/*
 * Packet sent when GUI configures engine to use UCI protocol
 */
public class UseUciPacket implements UciPacket
{
    @Override
    public String toCommandString()
    {
        return "uci";
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }

    public static UciPacket parsePacket(UciTokenData tokens[])
    {
        return new UseUciPacket();
    }
}
