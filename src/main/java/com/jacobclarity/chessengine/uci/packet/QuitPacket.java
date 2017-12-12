package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.UciTokenData;

//Packet sent when the engine should shut down
public class QuitPacket implements UciPacket
{
    @Override
    public String toCommandString()
    {
        return "quit";
    }

    public static UciPacket parsePacket(UciTokenData[] tokens)
    {
        return new QuitPacket();
    }
}
