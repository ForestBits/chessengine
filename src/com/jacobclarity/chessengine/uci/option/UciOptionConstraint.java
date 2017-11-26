package com.jacobclarity.chessengine.uci.option;

//any given option has certain constraints, like a min and max value for an integer,
//or the list of allowed values for a uci combo option
//this interface represents the defined constraints for each type, and allows validation
//the constraint object is also responsible for parsing the option value
public interface UciOptionConstraint
{
    //gets the substring representing the constraints, e.g. "min 1 max 10"
    String toCommandString();

    //true if value is acceptable given the constraints, else false
    boolean constraintMet(String value);

    //get value of option in a useful format (e.g. convert from "true" to boolean true
    //only called when constraintMet returns true
    Object getOptionValue(String value);
}
