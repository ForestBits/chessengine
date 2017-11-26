package com.jacobclarity.chessengine.uci.option;

import com.jacobclarity.chessengine.uci.UciParseException;
import com.jacobclarity.chessengine.uci.UciToken;
import com.jacobclarity.chessengine.uci.UciTokenData;
import com.jacobclarity.chessengine.uci.packet.SetOptionPacket;

import java.util.HashMap;
import java.util.Map;

//instances of this class represent possible options supported by the engine
public class UciOption
{
    private final String name;

    private final UciOptionType type;

    private final UciOptionConstraint constraint;

    private final Object defaultValue; //null for settings with no default

    private static final UciOption[] options;

    //option name -> option
    private static final Map<String, UciOption> nameOptionMap = new HashMap<>();

    static
    {
        options = new UciOption[]
        {

        };

        for (UciOption option : options)
            nameOptionMap.put(option.getName(), option);
    }


    public UciOption(String name, UciOptionType type, UciOptionConstraint constraint, Object defaultValue)
    {
        this.name = name;
        this.type = type;
        this.constraint = constraint;
        this.defaultValue = defaultValue;
    }

    public String getName()
    {
        return name;
    }

    public UciOptionType getType()
    {
        return type;
    }

    public UciOptionConstraint getConstraint()
    {
        return constraint;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public static UciOption[] getOptions()
    {
        return options;
    }

    public static SetOptionPacket parsePacket(UciTokenData[] tokens)
    {
        //format is "setoption name <name> value <value1> <value2> <...>
        //however, if option type is button, value token is omitted

        if (tokens.length != 2 && tokens.length != 3)
            throw new UciParseException("Format of setoption is \"setoption name <name> value <value1> <value2> <...>\" or \"setoption name <name>\"");

        //for parsing to reach this function, setoption is the first token. We can check the other tokens.
        if (tokens[1].getToken() != UciToken.NAME)
            throw new UciParseException("Token in position 2 must be name");

        String optionName;

        //option name must be included. option value may or may not be, depending on the option type
        //as it is specific to each option name (the tokens given don't contain the type)

        //make sure name is actually supplied. It can be multiple words
        if (tokens[1].getValues().length == 0)
            throw new UciParseException("No option name found");

        optionName = String.join(" ", tokens[1].getValues());

        //now find the value. First, the button type has no value, so check that first
        UciOption option = nameOptionMap.get(optionName);

        if (option == null)
            throw new UciParseException("unknown option name " + optionName);

        if (option.getType() == UciOptionType.BUTTON)
        {
            //no more information is needed, but there could be data/tokens that follow after but shouldn't,
            //so this ensures strict compliance
            if (tokens.length != 2)
                throw new UciParseException("Setting button type option, unexpected token count (don't provide a value)");

            return new SetOptionPacket(optionName, null);
        }

        //otherwise, it has a value

        if (tokens.length != 3) //must have value, so it will contain 3 tokens
            throw new UciParseException("Format of setoption is \"setoption name <name> value <value1> <value2> <...>\" or \"setoption name <name>\"");

        if (tokens[2].getToken() != UciToken.VALUE)
            throw new UciParseException("Expected token value, got " + tokens[2].getToken().getLiteralString());

        String valueString = String.join(" ", tokens[2].getValues());

        if (valueString.isEmpty())
            throw new UciParseException("Value not provided for option " + optionName);

        //validate it against constraint
        UciOptionConstraint constraint = option.getConstraint();

        if (!constraint.constraintMet(valueString))
            throw new UciParseException("Value for option " + optionName + " does not meet constraints");

        //constraint is met, so parse the actual value
        Object optionValue = constraint.getOptionValue(valueString);

        return new SetOptionPacket(optionName, optionValue);
    }

    public String toCommandString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("option name ").append(name).append(" type ").append(type.getLiteralString());

        if (defaultValue != null)
        {
            builder.append(" default ");

            //there is a special case with option type string where am empty default should be "<empty>"

            if (type == UciOptionType.STRING && ((String) defaultValue).isEmpty())
                builder.append("<empty> ");
            else
                builder.append(defaultValue).append(' ');
        }

        builder.append(constraint.toCommandString());

        return builder.toString();
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }
}
