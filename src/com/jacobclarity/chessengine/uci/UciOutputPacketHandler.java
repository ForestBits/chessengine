package com.jacobclarity.chessengine.uci;

import com.jacobclarity.chessengine.uci.packet.UciPacket;

//once UCI outputs have been decided, they are sent to a UciOutputPacketHandler to actually be output
public interface UciOutputPacketHandler
{
    void handleOutputPacket(UciPacket packet);
}
