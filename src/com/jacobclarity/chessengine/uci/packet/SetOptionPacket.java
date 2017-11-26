package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.UciTokenData;
import com.jacobclarity.chessengine.uci.option.UciOption;

/*
 * Packet sent by GUI to set particular options offered by engine
 */
public class SetOptionPacket implements UciPacket
{
    private final String name;

    private final Object value;

    public SetOptionPacket(String name, Object value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public Object getValue()
    {
        return value;
    }

    @Override
    public String toCommandString()
    {
        return "setoption " + name + " value " + value;
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }

    public static UciPacket parsePacket(UciTokenData tokens[])
    {
        return UciOption.parsePacket(tokens);
    }
}
