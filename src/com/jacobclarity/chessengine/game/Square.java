package com.jacobclarity.chessengine.game;

import java.util.Objects;

public class Square
{
    //both 0-7
    private final int file;
    private final int rank;

    public Square(int file, int rank)
    {
        this.file = file;
        this.rank = rank;
    }

    public int getFile()
    {
        return file;
    }

    public int getRank()
    {
        return rank;
    }

    public String toNotation()
    {
        return AlgebraicNotation.getSquareName(this);
    }

    @Override
    public String toString()
    {
        return toNotation();
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

        Square other = (Square) object;

        return file == other.file && rank == other.rank;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(file, rank);
    }
}
