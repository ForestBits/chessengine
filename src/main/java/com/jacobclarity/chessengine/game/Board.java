package com.jacobclarity.chessengine.game;

import java.util.*;

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

    public Piece getPieceAt(Square square)
    {
        return getPieceAt(square.getFile(), square.getRank());
    }

    public void setPieceAt(int file, int rank, Piece piece)
    {
        int index = rank*width + file;

        board[index] = piece;
    }

    public void setPieceAt(Square square, Piece piece)
    {
        setPieceAt(square.getFile(), square.getRank(), piece);
    }

    public boolean inBounds(int file, int rank)
    {
        return file >= 0 && rank >= 0 && file < 8 && rank < 8;
    }

    public boolean inBounds(Square square)
    {
        return inBounds(square.getFile(), square.getRank());
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

    //get all possible moves a player can make on a given ply
    //this includes some moves that aren't legal, those that would result in
    //the king being left in or moved into check
    public Move[] getMovesForColor(PieceColor color)
    {
        List<Move> moves = new ArrayList<>();

        //for all pieces of that color, sum up their possible moves
        for (int file = 0; file < 8; ++file)
            for (int rank = 0; rank < 8; ++rank)
            {
                Piece p = getPieceAt(file, rank);

                if (p == null || p.getColor() != color) //empty or enemy piece
                    continue;

                //else it is a piece of the correct color and we add its moves
                moves.addAll(Arrays.asList(MoveFinder.getAvailableMoves(this, new Square(file, rank))));
            }

        return moves.toArray(new Move[moves.size()]);
    }

    public Move[] getLegalMovesForColor(PieceColor color)
    {
        Move[] possibleMoves = getMovesForColor(color);

        List<Move> legalMoves = new ArrayList<>();

        for (Move move : possibleMoves)
            if (isLegal(move))
                legalMoves.add(move);

        return legalMoves.toArray(new Move[legalMoves.size()]);
    }

    //discover if a square is covered by other pieces, i.e. the king cannot legally move to this square
    //forColor is the side which can or cannot move into the square,
    //opponentMoves is moves available for the opposite color, e.g. from a call to getMovesForColor
    public boolean checkOnSquare(PieceColor forColor, Square square, Move[] opponentMoves)
    {
        //a square is "in check" if an enemy piece can move to it, so we can just go through all the moves
        //to see if that is the case

        for (Move move : opponentMoves)
            if (move.getTo().equals(square))
                return true;

        return false;
    }

    //checks if move is legal. This performs all needed checking, such as moving into or through check
    //this must be called before performMove is called
    public boolean isLegal(Move move)
    {
        //any non-castling move is legal if the king isn't in check after the move
        //however, castling has the special condition that you not only cannot move into check,
        //but also not through it, so this must be checked as it is a special case


        //we divide by this case in a bit, partially for efficiency, and because
        //we need to use the most updated position of the king, which is not the current position
        //if the king moved
        boolean kingMove = getPieceAt(move.getFrom()).getType() == PieceType.KING;

        //castling is denoted in the Move object as a king move that is two squares, so we check for that
        //it is the only king move where abs(from-file - to-file) > 1
        boolean castling = kingMove && Math.abs(move.getFrom().getFile() - move.getTo().getFile()) > 1;

        if (!castling)
        {
            //to see if the king is in check, we need to find the king
            //the king will be the same color as the current turn

            Square kingPos = null;

            if (kingMove)
                kingPos = move.getTo();
            else
                loop:
                for (int file = 0; file < 8; ++file)
                    for (int rank = 0; rank < 8; ++rank)
                    {
                        Piece piece = getPieceAt(file, rank);

                        if (piece == null)
                            continue;

                        if (piece.getType() == PieceType.KING && piece.getColor() == turn)
                        {
                            kingPos = new Square(file, rank);

                            break loop;
                        }
                    }

            //now that we know where the king is, find the enemy moves
            PieceColor enemyColor = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

            //the enemy moves should be calculated as if this move actually occurred already
            Board copy = new Board(this);

            copy.performMove(move);

            Move[] enemyMoves = copy.getMovesForColor(enemyColor);

            //if any enemy move ends on the king's square, the king was left in check and it is illegal
            for (Move m : enemyMoves)
                if (m.getTo().equals(kingPos))
                    return false;

            return true; //no pieces could reach the king, so the king isn't in check and it is a legal move
        }
        else
        {
            //for the castling special case, we must check and see if all the squares the king
            //travels through or ends on are free of check

            Piece king = getPieceAt(move.getFrom());

            Square from = move.getFrom();
            Square to  = move.getTo();

            List<Square> movementSquares = new ArrayList<>();

            int castleOffsetDirection = Integer.signum(to.getFile() - from.getFile());
            int moveLength = castleOffsetDirection > 0 ? 2 : 3; //going to the right is kingside, else queenside

            for (int i = 1; i <= moveLength; ++i)
                movementSquares.add(new Square(from.getFile() + castleOffsetDirection*i, from.getRank()));

            //now check each square and see if it is under check
            PieceColor enemyColor = king.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

            Move[] enemyMoves = getMovesForColor(enemyColor);

            for (Square s : movementSquares)
                if (checkOnSquare(king.getColor(), s, enemyMoves))
                    return false;

            return true; //none of the square were under check, so it is legal
        }
    }

    //determine if a player has won, or the game is otherwise completed (i.e. drawn)
    public GameResult getBoardState()
    {
        Move[] whiteMoves = getMovesForColor(PieceColor.WHITE);
        Move[] blackMoves = getMovesForColor(PieceColor.BLACK);

        Square whiteKingSquare = null;
        Square blackKingSquare = null;

        loop:
        for (int file = 0; file < 8; ++file)
            for (int rank = 0; rank < 8; ++rank)
            {
                Piece p = getPieceAt(file, rank);

                if (p.getType() == PieceType.KING)
                {
                    if (p.getColor() == PieceColor.WHITE)
                        whiteKingSquare = new Square(file, rank);
                    else
                        blackKingSquare = new Square(file, rank);

                    if (whiteKingSquare != null && blackKingSquare != null) //found both, early exit
                        break loop;
                }
            }

        if (turn == PieceColor.WHITE)
        {
            boolean inCheck = checkOnSquare(PieceColor.WHITE, whiteKingSquare, blackMoves);

            boolean hasLegalMove = false;

            for (Move m : whiteMoves)
                if (isLegal(m))
                {
                    hasLegalMove = true;

                    break;
                }

            if (inCheck && !hasLegalMove)
                return GameResult.BLACK_WIN;
            else if (!inCheck && !hasLegalMove)
                return GameResult.STALEMATE;
            else
                return GameResult.IN_PROGRESS;
        }
        else
        {
            boolean inCheck = checkOnSquare(PieceColor.BLACK, blackKingSquare, whiteMoves);

            boolean hasLegalMove = false;

            for (Move m : whiteMoves)
                if (isLegal(m))
                {
                    hasLegalMove = true;

                    break;
                }

            if (inCheck && !hasLegalMove)
                return GameResult.WHITE_WIN;
            else if (!inCheck && !hasLegalMove)
                return GameResult.STALEMATE;
            else
                return GameResult.IN_PROGRESS;
        }
    }

    //update the board state as it would be after a piece is moved
    //does not check legality of moves, call isLegal first
    public void performMove(Move move)
    {
        //updating board state is simple for any move that isn't castling or en passant

        Piece piece = getPieceAt(move.getFrom());
        Piece pieceAtMoveEnd = getPieceAt(move.getTo());
        
        boolean kingMove = piece.getType() == PieceType.KING;
        boolean pawnMove = piece.getType() == PieceType.PAWN;
        boolean rookMove = piece.getType() == PieceType.ROOK;

        //castling is denoted in the Move object as a king move that is two squares, so we check for that
        //it is the only king move where abs(from-file - to-file) > 1
        boolean castling = kingMove && Math.abs(move.getFrom().getFile() - move.getTo().getFile()) > 1;

        //en passant is the only diagonal pawn move that lands on an empty square
        boolean enPassant = pawnMove && move.getFrom().getFile() != move.getTo().getFile() && pieceAtMoveEnd == null;

        boolean captureMove = false;

        //used for removing castling ability when we capture a rook
        boolean rookCaptureMove = pieceAtMoveEnd != null && pieceAtMoveEnd.getType() == PieceType.ROOK;

        if (castling)
        {
            //we must move the king as well as the rook
            setPieceAt(move.getFrom(), null);
            setPieceAt(move.getTo(), piece);

            //the rook must be on the other side of the king
            int rookOffset = -Integer.signum(move.getTo().getFile() - move.getFrom().getFile());

            //if the rook ends to the king's right (white perspective), then it must have come from the left
            int rookStartingFile = rookOffset == 1 ? 0 : 7;

            Square rookStartingPosition = new Square(rookStartingFile, move.getFrom().getRank());

            Piece rook = getPieceAt(rookStartingPosition);

            setPieceAt(rookStartingPosition, null);
            setPieceAt(move.getTo().getFile() + rookOffset, move.getFrom().getRank(), rook);
        }
        else
        {
            //if a move is a promotion move, the piece in the to square is different from the moving piece
            Piece finalPiece = piece;

            if (move instanceof PromotionMove)
            {
                PromotionMove promotionMove = (PromotionMove) move;

                finalPiece = new Piece(promotionMove.getPromotionPiece(), piece.getColor());
            }

            if (enPassant)
            {
                //first, the normal portion of the move
                setPieceAt(move.getFrom(), null);
                setPieceAt(move.getTo(), finalPiece);

                //we must remove the captured pawn, which is next to the ep target square
                int offset = piece.getColor() == PieceColor.WHITE ? -1 : 1;

                setPieceAt(move.getTo().getFile(), move.getTo().getRank() + offset, null);

                captureMove = true;
            }
            else
            {
                //normal move

                if (getPieceAt(move.getTo()) != null)
                    captureMove = true;

                setPieceAt(move.getFrom(), null);
                setPieceAt(move.getTo(), finalPiece);
            }
        }

        //update rest of board state

        if (kingMove) //if the king moves for any reason, including castling, we can no longer do it
            setCastleAbility(turn, CastleAbility.NONE);

        if (rookMove) //if a rook moved, we cannot castle on that side anymore
        {
            boolean kingside = move.getFrom().getFile() == 7;

            CastleAbility ability = getCastleAbility(turn);

            //while we can no longer castle on the respective side, we might be able to
            //on the other side, so preserve it if it exists

            if (kingside)
                setCastleAbility(turn,
                        ability.canCastleQueenside() ? CastleAbility.QUEENSIDE : CastleAbility.NONE);
            else //moved queenside rook
                setCastleAbility(turn,
                        ability.canCastleKingside() ? CastleAbility.KINGSIDE : CastleAbility.NONE);
        }

        if (rookCaptureMove)
        {
            //same as the rook move case, more or less, just captured instead of moved

            boolean kingside = move.getTo().getFile() == 7;

            CastleAbility ability = getCastleAbility(turn);

            //while we can no longer castle on the respective side, we might be able to
            //on the other side, so preserve it if it exists

            if (kingside)
                setCastleAbility(turn,
                        ability.canCastleQueenside() ? CastleAbility.QUEENSIDE : CastleAbility.NONE);
            else //moved queenside rook
                setCastleAbility(turn,
                        ability.canCastleKingside() ? CastleAbility.KINGSIDE : CastleAbility.NONE);
        }

        //if a pawn double-move was made, we need to set the e.p. target square
        boolean doubleMove = piece.getType() == PieceType.PAWN
                && Math.abs(move.getTo().getRank() - move.getFrom().getRank()) == 2;

        if (doubleMove)
        {
            int offset = turn == PieceColor.WHITE ? -1 : 1;
            int targetSquareRank = move.getTo().getRank() + offset;

            enPassantTargetSquare = new Square(move.getFrom().getFile(), targetSquareRank);
        }
        else
            enPassantTargetSquare = null; //clear state if it wasn't

        ++halfMove;

        if (pawnMove || captureMove)
            halfMove = 0; //reset 50-move-rule counter

        if (turn == PieceColor.BLACK)
            ++fullMove;

        //switch turns
        turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
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
