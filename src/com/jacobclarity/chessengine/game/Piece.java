package com.jacobclarity.chessengine.game;

import java.util.Objects;

public class Piece
{
    private final PieceType type;
    private final PieceColor color;

    public Piece(PieceType type, PieceColor color)
    {
        this.type = type;
        this.color = color;
    }

    public PieceType getType()
    {
        return type;
    }

    public PieceColor getColor()
    {
        return color;
    }

    //returns letter representing piece, where uppercase is white and lowercase is black
    public char getPieceLetter()
    {
        if (color == PieceColor.WHITE)
            return Character.toUpperCase(type.getPieceLetter());
        else
            return type.getPieceLetter();
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
            return true;

        if (object == null)
            return false;

        if (this.getClass() != object.getClass())
            return false;

        Piece other = (Piece) object;

        return type == other.type && color == other.color;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, color);
    }
}
