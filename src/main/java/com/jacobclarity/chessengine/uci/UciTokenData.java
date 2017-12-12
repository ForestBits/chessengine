package com.jacobclarity.chessengine.uci;

import java.util.Arrays;

public class UciTokenData
{
    private final UciToken token;

    private final String[] values;

    public UciTokenData(UciToken token, String[] values)
    {
        this.token = token;
        this.values = values;
    }

    public UciToken getToken()
    {
        return token;
    }

    public String[] getValues()
    {
        return Arrays.copyOf(values, values.length);
    }
}
