package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.uci.UciTokenData;

/*
 * Packet sent by GUI
 */
public class RegisterPacket implements UciPacket
{
    private final boolean later;

    private final String name;
    private final String code;

    public RegisterPacket(boolean later, String name, String code)
    {
        this.later = later;
        this.name = name;
        this.code = code;
    }

    public boolean willRegisterLater()
    {
        return later;
    }

    public String getName()
    {
        return name;
    }

    public String getCode()
    {
        return code;
    }

    @Override
    public String toCommandString()
    {
        return "register " + (later ? "later" : name + " " + code);
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }

    public static UciPacket parsePacket(UciTokenData[] tokens)
    {
        return null;//new RegisterPacket();
    }
}
