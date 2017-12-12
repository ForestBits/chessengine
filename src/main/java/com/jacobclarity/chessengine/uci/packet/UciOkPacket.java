package com.jacobclarity.chessengine.uci.packet;

//Sent by engine once id and options are sent to tell GUI it is ready for commands
public class UciOkPacket implements UciPacket
{
    @Override
    public String toCommandString()
    {
        return "uciok";
    }
}
