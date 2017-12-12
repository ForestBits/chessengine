package com.jacobclarity.chessengine.uci.option;

//possible type values for option/setoption command
public enum UciOptionType
{
    CHECK("check"),    //a true or false value
    SPIN("spin"),      //integer value in certain range
    COMBO("combo"),    //predefined string value (like an enum)
    BUTTON("button"),  //has no value, but can be "pressed" - e.g. button to "clear hashes"
    STRING("string");  //a string value


    private final String literalString;

    UciOptionType(String literalString)
    {
        this.literalString = literalString;
    }

    public String getLiteralString()
    {
        return literalString;
    }
}
