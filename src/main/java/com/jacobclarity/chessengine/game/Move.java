package com.jacobclarity.chessengine.game;

import java.util.Objects;

public class Move
{
    protected final Square from;
    protected final Square to;

    public static final Move NULL_MOVE = null;

    public Move(Square from, Square to)
    {
        this.from = from;
        this.to = to;
    }

    public Square getFrom()
    {
        return from;
    }

    public Square getTo()
    {
        return to;
    }

    public String toNotation()
    {
        return AlgebraicNotation.getSquareName(from) + AlgebraicNotation.getSquareName(to);
    }

    public static Move fromNotation(String notation)
    {
        if (notation.length() == 5)
            return PromotionMove.fromPromotionNotation(notation);
        else if (notation.length() == 4)
        {
            //format should be {from}{to}

            Square from = AlgebraicNotation.getSquareFromName(notation.substring(0, 2));
            Square to = AlgebraicNotation.getSquareFromName(notation.substring(2));

            return new Move(from, to);
        }
        else
            throw new NotationException("move notation should be 4 ({from}{to} or 5 {from}{to}{promotionletter} characters");
    }

    @Override
    public boolean equals(Object object)
    {
        //note that this method will return false when comparing against a PromotionMove instance
        //this is correct, because any move that promotes is a PromotionMove, and any other move is not an instance of
        //PromotionMove is not a promotion move, so objects of these respective classes can never be equal
        //the same holds true for the equals method of PromotionMove

        if (this == object)
            return true;

        if (object == null)
            return false;

        if (this.getClass() != object.getClass())
            return false;

        Move other = (Move) object;

        return from.equals(other.from) && to.equals(other.to);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(from, to);
    }

    @Override
    public String toString()
    {
        return toNotation();
    }
}
