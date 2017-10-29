package com.jacobclarity.chessengine.uci;

import com.jacobclarity.chessengine.uci.packet.UciPacket;

//called to convert token data into UCI packet
public interface UciParseFunction
{
    //Returns valid packet or throws UciParseException on error
    UciPacket parsePacket(UciTokenData[] tokens);
}
