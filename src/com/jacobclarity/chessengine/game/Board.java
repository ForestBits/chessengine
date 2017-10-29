package com.jacobclarity.chessengine.game;

import java.util.Arrays;
import java.util.Objects;

public class Board
{
    private final int width = 8;
    private final int height = 8;

    //null represents empty square, indexed 0-7 on axes
    private final Piece[] board = new Piece[width*height];

    private PieceColor turn;

    private CastleAbility whiteCastleAbility;
    private CastleAbility blackCastleAbility;

    //populated as square "behind" pawn double-move on next turn, else null
    private Square enPassantTargetSquare;

    //plies since last capture or pawn move
    int halfMove;

    //incremented after each black move
    int fullMove;

    public static final Board START_POSITION = Fen.getBoardFromFen(Fen.START_POSITION);

    public Board()
    {
        turn = PieceColor.WHITE;
        whiteCastleAbility = CastleAbility.BOTH;
        blackCastleAbility = CastleAbility.BOTH;
        enPassantTargetSquare = null;
        halfMove = 0;
        fullMove = 1;
    }

    public Board(Board other)
    {
        for (int i = 0; i < board.length; ++i)
            board[i] = other.board[i];

        turn = other.turn;
        whiteCastleAbility = other.whiteCastleAbility;
        blackCastleAbility = other.blackCastleAbility;
        enPassantTargetSquare = other.enPassantTargetSquare;
        halfMove = other.halfMove;
        fullMove = other.fullMove;
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

        Board other = (Board) object;

        boolean boardEqual = Arrays.equals(board, other.board);

        return boardEqual && turn == other.turn && whiteCastleAbility == other.whiteCastleAbility
            && blackCastleAbility == other.blackCastleAbility && halfMove == other.halfMove && fullMove == other.fullMove
            && (enPassantTargetSquare == null ? other.enPassantTargetSquare == null : enPassantTargetSquare.equals(other.enPassantTargetSquare));
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(board, turn, whiteCastleAbility, blackCastleAbility, enPassantTargetSquare, halfMove, fullMove);
    }

    public Piece getPieceAt(int file, int rank)
    {
        int index = rank*width + file;


        return board[index];
    }

    public void setPieceAt(int file, int rank, Piece piece)
    {
        int index = rank*width + file;

        board[index] = piece;
    }

    public PieceColor getTurn()
    {
        return turn;
    }

    public void setTurn(PieceColor color)
    {
        this.turn = color;
    }

    public CastleAbility getCastleAbility(PieceColor color)
    {
        if (color == PieceColor.WHITE)
            return whiteCastleAbility;
        else
            return blackCastleAbility;
    }

    public void setCastleAbility(PieceColor color, CastleAbility ability)
    {
        if (color == PieceColor.WHITE)
            whiteCastleAbility = ability;
        else
            blackCastleAbility = ability;
    }

    public Square getEnPassantTargetSquare()
    {
        return enPassantTargetSquare;
    }

    public void setEnPassantTargetSquare(Square enPassantTargetSquare)
    {
        this.enPassantTargetSquare = enPassantTargetSquare;
    }

    public int getHalfmove()
    {
        return halfMove;
    }

    public void setHalfmove(int halfMove)
    {
        this.halfMove = halfMove;
    }

    public int getFullMove()
    {
        return fullMove;
    }

    public void setFullMove(int fullMove)
    {
        this.fullMove = fullMove;
    }


    //convenience method
    public String toFen()
    {
        return Fen.getFenFromBoard(this);
    }

    public String getBoardDebugString()
    {
        StringBuilder builder = new StringBuilder();

        for (int rank = 7; rank >= 0; --rank)
        {
            builder.append("+---+---+---+---+---+---+---+---+\n|");

            for (int file = 0; file < 8; ++file)
            {
                Piece piece = getPieceAt(file, rank);

                char pieceLetter;

                if (piece == null)
                    pieceLetter = ' ';
                else
                    pieceLetter = piece.getPieceLetter();

                builder.append(' ');
                builder.append(pieceLetter);
                builder.append(" |");
            }

            builder.append(System.getProperty("line.separator"));
        }

        builder.append("+---+---+---+---+---+---+---+---+");

        return builder.toString();
    }

    public String toDebugString()
    {
       StringBuilder builder = new StringBuilder();

        builder.append(getBoardDebugString()).append(System.getProperty("line.separator"));
        builder.append("Move: ").append(turn).append(System.getProperty("line.separator"));
        builder.append("White Castle: ").append(whiteCastleAbility).append("  Black Castle: ").append(blackCastleAbility).append(System.getProperty("line.separator"));
        builder.append("E.P.: ").append(enPassantTargetSquare).append(System.getProperty("line.separator"));
        builder.append("FM: ").append(fullMove).append("  50-move counter: ").append(halfMove);

        return builder.toString();
    }

    @Override
    public String toString()
    {
        return toDebugString();
    }
}
