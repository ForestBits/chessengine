package com.jacobclarity.chessengine.uci;

import com.jacobclarity.chessengine.uci.packet.UciPacket;

//once UCI packets are parsed, they are sent to a UciInputPacketHandler to be logically processed
public interface UciInputPacketHandler
{
    void handleInputPacket(UciPacket packet);
}
