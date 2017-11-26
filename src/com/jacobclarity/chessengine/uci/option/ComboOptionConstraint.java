package com.jacobclarity.chessengine.uci.option;

import java.util.EnumMap;
import java.util.Map;

public class ComboOptionConstraint<T extends Enum<T>> implements UciOptionConstraint
{
    //all combo options must be backed by an enum
    private final Class<T> optionEnum;

    //combo options display text can be different than the enum name itself
    //if no mapping is passed in, it defaults to the enum constant name
    private final Map<T, String> optionText;

    public ComboOptionConstraint(Class<T> optionEnum)
    {
        this.optionEnum = optionEnum;

        //because
        EnumMap<T, String> map = new EnumMap<T, String>(optionEnum);

        T[] values = optionEnum.getEnumConstants();

        for (T value : values)
            map.put(value, value.name());

        optionText = map;
    }

    public ComboOptionConstraint(Class<T> optionEnum, Map<T, String> optionText)
    {
        this.optionEnum = optionEnum;
        this.optionText = optionText;
    }

    public T[] getOptions()
    {
        return optionEnum.getEnumConstants();
    }

    @Override
    public String toCommandString()
    {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<T, String> entry : optionText.entrySet())
            builder.append("var ").append(entry.getValue()).append(' ');

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    @Override
    public boolean constraintMet(String value)
    {
        for (Map.Entry<T, String> entry : optionText.entrySet())
            if (value.equalsIgnoreCase(entry.getValue()))
                return true;

        return false;
    }

    @Override
    public Object getOptionValue(String value)
    {
        for (Map.Entry<T, String> entry : optionText.entrySet())
            if (value.equalsIgnoreCase(entry.getValue()))
                return entry.getKey();

        throw new RuntimeException("unreachable code");
    }
}