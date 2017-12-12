package com.jacobclarity.chessengine.uci.option;

public class StringOptionConstraint implements UciOptionConstraint
{
    @Override
    public String toCommandString()
    {
        return "";
    }

    @Override
    public boolean constraintMet(String value)
    {
        return true;
    }

    @Override
    public Object getOptionValue(String value)
    {
        return value;
    }
}
