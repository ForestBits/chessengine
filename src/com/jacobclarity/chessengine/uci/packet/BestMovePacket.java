package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.game.Move;

//sent by Engine to tell GUI the best move that has been found after searching
public class BestMovePacket implements UciPacket
{
    private final Move bestMove;
    private final Move ponderMove;

    public BestMovePacket(Move bestMove)
    {
        this.bestMove = bestMove;
        this.ponderMove = null;
    }

    public BestMovePacket(Move bestMove, Move ponderMove)
    {
        this.bestMove = bestMove;
        this.ponderMove = ponderMove;
    }

    public Move getBestMove()
    {
        return bestMove;
    }

    public Move getPonderMove()
    {
        return ponderMove;
    }

    @Override
    public String toCommandString()
    {
        if (ponderMove != null)
            return "bestmove " + bestMove.toNotation() + " ponder " + ponderMove.toNotation();
        else
            return "bestmove " + bestMove.toNotation();
    }
}
