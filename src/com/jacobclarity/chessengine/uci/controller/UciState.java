package com.jacobclarity.chessengine.uci.controller;

//Current state of the UCI protocol/engine
public enum UciState
{
    STARTUP,   //waiting for initial startup commands
    IDLE,      //engine not currently searching
    SEARCHING  //engine is searching
}
