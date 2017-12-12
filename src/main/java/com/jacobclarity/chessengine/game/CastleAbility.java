package com.jacobclarity.chessengine.game;

//represents castling rights
public enum CastleAbility
{
    KINGSIDE(true, false),
    QUEENSIDE(false, true),
    BOTH(true, true),
    NONE(false, false);

    private boolean kingside;
    private boolean queenside;

    CastleAbility(boolean kingside, boolean queenside)
    {
        this.kingside = kingside;
        this.queenside = queenside;
    }

    public boolean canCastleKingside()
    {
        return kingside;
    }

    public boolean canCastleQueenside()
    {
        return queenside;
    }
}
