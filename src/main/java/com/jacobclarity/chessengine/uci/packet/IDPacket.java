package com.jacobclarity.chessengine.uci.packet;

//Packet sent by engine to give ID info
public class IDPacket implements UciPacket
{
    private final String param;
    private final String value;

    public IDPacket(String param, String value)
    {
        this.param = param;
        this.value = value;
    }

    public String getParam()
    {
        return param;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public String toCommandString()
    {
        return "id " + param + ' ' + value;
    }
}
