package com.jacobclarity.chessengine.game;

public class PromotionMove extends Move
{
    private final PieceType promotionPiece;

    public PromotionMove(Square from, Square to, PieceType promotionPiece)
    {
        super(from, to);

        this.promotionPiece = promotionPiece;
    }

    public PieceType getPromotionPiece()
    {
        return promotionPiece;
    }

    @Override
    public String toNotation()
    {
        return super.toNotation() + promotionPiece.getPieceLetter();
    }

    public static PromotionMove fromPromotionNotation(String notation)
    {
        //format is {from}{to}{promotionletter}

        if (notation.length() != 5)
            throw new NotationException("invalid promotion move format: must be 5 characters {from}{to}{promotionletter}");

        Square from = AlgebraicNotation.getSquareFromName(notation.substring(0, 2));
        Square to = AlgebraicNotation.getSquareFromName(notation.substring(2, 4));

        char promotionLetter = notation.toLowerCase().charAt(4);

        for (PieceType type : PieceType.values())
            if (type.getPieceLetter() == promotionLetter)
            {
                return new PromotionMove(from, to, type);
            }

        //if we fall through the loop, the piece letter is unknown
        throw new NotationException("Unknown piece letter in move, expected k,q,r,b,n, got " + promotionLetter);
    }
}
