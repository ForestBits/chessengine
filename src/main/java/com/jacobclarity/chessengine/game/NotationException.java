package com.jacobclarity.chessengine.game;

//thrown when any move notation is invalid
public class NotationException extends RuntimeException
{
    public NotationException(String message)
    {
        super(message);
    }
}
