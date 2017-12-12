package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.UciTokenData;

//packet sent to make engine stop searching and deliver bestmove
public class StopSearchPacket implements UciPacket
{
    @Override
    public String toCommandString()
    {
        return "stop";
    }

    public static UciPacket parsePacket(UciTokenData[] tokens)
    {
        return new StopSearchPacket();
    }
}
