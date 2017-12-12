package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.option.UciOption;

//packet sent from engine to give available configuration options
public class OptionPacket implements UciPacket
{
    private final UciOption option;

    public OptionPacket(UciOption option)
    {
        this.option = option;
    }

    public UciOption getOption()
    {
        return option;
    }

    @Override
    public String toCommandString()
    {
        return option.toCommandString();
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }
}
