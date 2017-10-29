package com.jacobclarity.chessengine.game;

public enum PieceType
{
    KING('k'),
    QUEEN('q'),
    ROOK('r'),
    BISHOP('b'),
    KNIGHT('n'),
    PAWN('p');


    private final char pieceLetter;

    PieceType(char pieceLetter)
    {
        this.pieceLetter = pieceLetter;
    }

    public char getPieceLetter()
    {
        return this.pieceLetter;
    }
}
