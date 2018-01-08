package com.jacobclarity.chessengine.game;

import java.util.ArrayList;
import java.util.List;

//finds available (but possibly not legal) moves in a given position
public class MoveFinder
{
    private static Move[] getPawnMoves(Board position, Square square)
    {
        List<Move> moves = new ArrayList<Move>();

        Piece pawn = position.getPieceAt(square);

        int increment = pawn.getColor() == PieceColor.WHITE ? 1 : -1;
        int nextRank = square.getRank() + increment;

        boolean promotionPossible = nextRank == 0 || nextRank == 7;

        //directly in front of pawn
        Square normalMoveSquare = new Square(square.getFile(), nextRank);
        Square startRankSpecialMoveSquare = new Square(square.getFile(), square.getRank() + increment*2);

        Square[] captureMoveSquares = new Square[]
        {
            new Square(square.getFile() - 1, nextRank),
            new Square(square.getFile() + 1, nextRank)
        };

        //pawns can only move forward if nothing is there
        if (position.getPieceAt(normalMoveSquare) == null)
            moves.add(new Move(square, normalMoveSquare));

        //pawns can capture diagonally if there is a piece there AND it is an enemy piece
        //or, the special case of en passant
        for (Square captureMoveSquare : captureMoveSquares)
        {
            if (!position.inBounds(captureMoveSquare))
                continue;

            Piece piece = position.getPieceAt(captureMoveSquare);

            if (piece != null && piece.getColor() != pawn.getColor())
                moves.add(new Move(square, captureMoveSquare));
            else if (captureMoveSquare.equals(position.getEnPassantTargetSquare()))
                moves.add(new Move(square, captureMoveSquare));
        }

        //the case of the 2-square moves for pawns on the starting rank
        if (((pawn.getColor() == PieceColor.WHITE && square.getRank() == 1)
                || (pawn.getColor() == PieceColor.BLACK && square.getRank() == 6)) //on starting rank
                && position.getPieceAt(normalMoveSquare) == null
                && position.getPieceAt(startRankSpecialMoveSquare) == null) //both square in front are clear
        {
            moves.add(new Move(square, startRankSpecialMoveSquare));
        }

        //some of the above moves may allow promotion, and for each move that ends on the last rank,
        //we really need to consider every promotion move possible for it
        //we can filter through and update the move list accordingly
        if (promotionPossible)
        {
            //because all possible pawn moves move forward only 1 square,
            //every possible move for the pawn is therefore a promotion move

            List<Move> allMoves = new ArrayList<>();

            for (Move move : moves)
            {
                allMoves.add(new PromotionMove(move.getFrom(), move.getTo(), PieceType.KNIGHT));
                allMoves.add(new PromotionMove(move.getFrom(), move.getTo(), PieceType.BISHOP));
                allMoves.add(new PromotionMove(move.getFrom(), move.getTo(), PieceType.ROOK));
                allMoves.add(new PromotionMove(move.getFrom(), move.getTo(), PieceType.QUEEN));
            }

            return allMoves.toArray(new Move[allMoves.size()]);
        }
        else //no promotion, move list is fine as-is
            return moves.toArray(new Move[moves.size()]);
    }

    private static Move[] getKnightMoves(Board position, Square square)
    {
        List<Move> moves = new ArrayList<Move>();

        Piece knight = position.getPieceAt(square);

        int[] offsets = new int[]
        {
            -2, 1,
            -2, -1,
            2, 1,
            2, -1,
            1, 2,
            1, -2,
            -1, 2,
            -1, -2
        };

        //for each possible square
        for (int i = 0; i < 8; ++i)
        {
            Square s = new Square(square.getFile() + offsets[i * 2], square.getRank() + offsets[i * 2 + 1]);

            //must be inbounds
            if (!position.inBounds(s))
                continue;

            Piece piece = position.getPieceAt(s.getFile(), s.getRank());

            //empty or enemy piece - cannot jump on own piece
            if (piece == null || piece.getColor() != knight.getColor())
                moves.add(new Move(square, s));
        }

        return moves.toArray(new Move[moves.size()]);
    }

    private static Move[] getBishopMoves(Board position, Square square)
    {
        List<Move> moves = new ArrayList<Move>();

        Piece bishop = position.getPieceAt(square);

        int[] offsets = new int[]
        {
            1, 1,
            1, -1,
            -1, 1,
            -1, -1
        };

        //for each direction
        for (int i = 0; i < 4; ++i)
        {
            int offsetFile = offsets[i*2];
            int offsetRank = offsets[i*2 + 1];

            //extend in that direction
            for (int extension = 1;; ++extension)
            {
                Square offsetSquare = new Square(square.getFile() + offsetFile*extension,
                                                 square.getRank() + offsetRank*extension);

                if (!position.inBounds(offsetSquare))
                    break; //stop extension once we go out of bounds

                Piece piece = position.getPieceAt(offsetSquare);

                if (piece == null)
                    moves.add(new Move(square, offsetSquare));
                else if (piece.getColor() == bishop.getColor()) //cannot move past or into team piece
                    break;
                else if (piece.getColor() != bishop.getColor()) //valid, but cannot move past enemy piece
                {
                    moves.add(new Move(square, offsetSquare));

                    break;
                }
            }
        }

        return moves.toArray(new Move[moves.size()]);
    }

    private static Move[] getRookMoves(Board position, Square square)
    {
        List<Move> moves = new ArrayList<Move>();

        Piece rook = position.getPieceAt(square);

        int offsets[] = new int[]
        {
            0, 1,
            0, -1,
            1, 0,
            -1, 0
        };

        //for each direction
        for (int i = 0; i < 4; ++i)
        {
            int offsetFile = offsets[i*2];
            int offsetRank = offsets[i*2 + 1];

            //extend in that direction
            for (int extension = 1;; ++extension)
            {
                Square offsetSquare = new Square(square.getFile() + offsetFile*extension,
                                                 square.getRank() + offsetRank*extension);

                if (!position.inBounds(offsetSquare))
                    break; //stop extension once we go out of bounds

                Piece piece = position.getPieceAt(offsetSquare);

                if (piece == null)
                    moves.add(new Move(square, offsetSquare));
                else if (piece.getColor() == rook.getColor()) //cannot move past or into team piece
                    break;
                else if (piece.getColor() != rook.getColor()) //valid, but cannot move past enemy piece
                {
                    moves.add(new Move(square, offsetSquare));

                    break;
                }
            }
        }

        return moves.toArray(new Move[moves.size()]);
    }

    private static Move[] getQueenMoves(Board position, Square square)
    {
        //a queen is really a piece that can act as a rook or a bishop on any move
        //so its moveset is just the sum of those

        Move[] bishopMoves = getBishopMoves(position, square);
        Move[] rookMoves = getRookMoves(position, square);

        Move[] combined = new Move[bishopMoves.length + rookMoves.length];

        for (int i = 0; i < bishopMoves.length; ++i)
            combined[i] = bishopMoves[i];

        for (int i = 0; i < rookMoves.length; ++i)
            combined[i + bishopMoves.length] = rookMoves[i];

        return combined;
    }

    //doesn't prohibit king moves that move into check; that is checked elsewhere
    private static Move[] getKingMoves(Board position, Square square)
    {
        List<Move> moves = new ArrayList<Move>();

        Piece king = position.getPieceAt(square);

        int[] offsets = new int[]
        {
                1, 1,
                1, 0,
                1, -1,
                0, 1,
                0, 0,
                0, -1,
                -1, 1,
                -1, 0,
                -1, -1
        };

        //for each potential square
        for (int i = 0; i < 9; ++i)
        {
            Square possibleSquare = new Square(square.getFile() + offsets[i*2],
                                               square.getRank() + offsets[i*2 + 1]);

            //must be inbounds
            if (!position.inBounds(possibleSquare))
                continue;

            Piece piece = position.getPieceAt(possibleSquare);

            //if square is empty or enemy piece
            if (piece == null || piece.getColor() != king.getColor())
                moves.add(new Move(square, possibleSquare));

            //else it is a team piece and we cannot move there
        }

        //castling moves may be available

        CastleAbility castleAbility = position.getCastleAbility(king.getColor());

        if (castleAbility.canCastleQueenside())
        {
            //every square we pass through or end on must be empty
            for (int i = 1; i <= 2; ++i)
            {
                Square s = new Square(square.getFile() - i, square.getRank());

                if (position.getPieceAt(s) != null)
                    break;

                if (i == 2) //we are on the last square and everything has been empty
                    moves.add(new Move(square, s));
            }
        }

        if (castleAbility.canCastleKingside())
        {
            //every square we pass through or end on must be empty
            for (int i = 1; i <= 2; ++i)
            {
                Square s = new Square(square.getFile() + i, square.getRank());

                if (position.getPieceAt(s) != null)
                    break;

                if (i == 2) //we are on the last square and everything has been empty
                    moves.add(new Move(square, s));
            }
        }

        return moves.toArray(new Move[moves.size()]);
    }

    public static Move[] getAvailableMoves(Board position, Square square)
    {
        Piece piece = position.getPieceAt(square.getFile(), square.getRank());

        if (piece == null)
            throw new RuntimeException("Cannot get moves for empty square");

        switch (piece.getType())
        {
            case PAWN:
                return getPawnMoves(position, square);

            case KNIGHT:
                return getKnightMoves(position, square);

            case BISHOP:
                return getBishopMoves(position, square);

            case ROOK:
                return getRookMoves(position, square);

            case QUEEN:
                return getQueenMoves(position, square);

            case KING:
                return getKingMoves(position, square);
        }

        throw new RuntimeException("hit unreachable code, bad enum value");
    }
}
