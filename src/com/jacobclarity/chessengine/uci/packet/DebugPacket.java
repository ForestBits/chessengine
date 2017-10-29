package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.UciParseException;
import com.jacobclarity.chessengine.uci.UciTokenData;

/*
 * Packet sent to configure debug mode
 */
public class DebugPacket implements UciPacket
{
    private final boolean shouldDebug;

    public DebugPacket(boolean shouldDebug)
    {
        this.shouldDebug = shouldDebug;
    }

    public boolean shouldDebug()
    {
        return shouldDebug;
    }

    @Override
    public String toCommandString()
    {
        return "debug " + (shouldDebug ? "on" : "off");
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }

    public static UciPacket parsePacket(UciTokenData[] tokens)
    {
        boolean debugMode;

        String debugValue = null;

        String[] values = tokens[0].getValues();

        if (values.length == 0)
            throw new UciParseException("no value provided, must be one of on, off");

        debugValue = values[0].toLowerCase();

        if (debugValue.equals("on"))
            debugMode = true;
        else if (debugValue.equals("off"))
            debugMode = false;
        else
            throw new UciParseException("invalid debug value, must be one of on, off");

        return new DebugPacket(debugMode);
    }
}
