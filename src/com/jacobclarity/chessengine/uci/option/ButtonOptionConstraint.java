package com.jacobclarity.chessengine.uci.option;

public class ButtonOptionConstraint implements UciOptionConstraint
{
    @Override
    public String toCommandString()
    {
        return "";
    }

    @Override
    public boolean constraintMet(String value)
    {
        //no value is specified for a button's value, so the constraint mandates emptiness
        if (value.length() != 0)
            return false;
        else
            return true;
    }

    @Override
    public Object getOptionValue(String value)
    {
        return null; //buttons have no value to pass on
    }
}
