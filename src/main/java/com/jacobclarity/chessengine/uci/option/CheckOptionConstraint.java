package com.jacobclarity.chessengine.uci.option;

public class CheckOptionConstraint implements UciOptionConstraint
{
    @Override
    public String toCommandString()
    {
        //the UCI protocol dictates the value is true or false, so it isn't specified in the option list
        return "";
    }

    @Override
    public boolean constraintMet(String value)
    {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
            return true;
        else
            return false;
    }

    @Override
    public Object getOptionValue(String value)
    {
        if (value.equalsIgnoreCase("true"))
            return true;
        else
            return false;
    }
}
