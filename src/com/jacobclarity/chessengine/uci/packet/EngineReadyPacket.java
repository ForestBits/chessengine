package com.jacobclarity.chessengine.uci.packet;

//sent by engine in response to IsReadyPacket - indicates engine is ready to receive more commands
public class EngineReadyPacket implements UciPacket
{
    @Override
    public String toCommandString()
    {
        return "readyok";
    }
}
