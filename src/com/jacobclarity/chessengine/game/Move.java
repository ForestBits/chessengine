package com.jacobclarity.chessengine.game;

public class Move
{
    private final Square from;
    private final Square to;

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
}
