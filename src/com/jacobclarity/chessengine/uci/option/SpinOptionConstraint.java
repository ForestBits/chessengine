package com.jacobclarity.chessengine.uci.option;

public class SpinOptionConstraint implements UciOptionConstraint
{
    private int min;

    private int max;

    public SpinOptionConstraint(int min, int max)
    {
        this.min = min;
        this.max = max;
    }

    public int getMin()
    {
        return min;
    }

    public int getMax()
    {
        return max;
    }

    @Override
    public String toCommandString()
    {
        return "min " + min + " max " + max;
    }

    @Override
    public boolean constraintMet(String value)
    {
        int number;

        try
        {
            number = Integer.parseInt(value);
        }

        catch (NumberFormatException ex)
        {
            return false;
        }

        if (number >= min && number <= max)
            return true;
        else
            return false;
    }

    @Override
    public Object getOptionValue(String value)
    {
        return Integer.parseInt(value);
    }
}
