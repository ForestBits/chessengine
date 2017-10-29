package com.jacobclarity.chessengine.game;

import java.util.HashMap;
import java.util.Map;

//parses FEN strings to board positions, and vice versa
public class Fen
{
    public static final String START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private static void parseBoard(Board board, String boardString)
    {
        //mapping used to get piece type by letter
        Map<Character, PieceType> pieceLetters = new HashMap<>();

        //as populated by the enum values
        for (PieceType pType : PieceType.values())
            pieceLetters.put(pType.getPieceLetter(), pType);

        int currentLetter = 0;

        //piece lists start from black's home rank
        for (int rank = 7; rank >= 0; --rank, ++currentLetter)
        {
            //get piece on each file
            for (int file = 0; file < 8; ++currentLetter)
            {
                //rank is added an additional time to account for the / separators in the string

                char pieceLetter = boardString.charAt(currentLetter);

                //if it is a number, we skip that many blank spaces. If it is a letter, get the piece type

                int emptySpaces = -1;

                try
                {
                    emptySpaces = Integer.parseInt(pieceLetter + "");
                }

                catch (NumberFormatException ex)
                {

                }

                if (emptySpaces == -1)
                {
                    //not a number, try a letter

                    PieceType type = pieceLetters.get(Character.toLowerCase(pieceLetter));

                    if (type == null)
                        throw new NotationException("Expected piece letter [kqrbnp], got " + pieceLetter);

                    PieceColor color = Character.isUpperCase(pieceLetter) ? PieceColor.WHITE : PieceColor.BLACK;

                    board.setPieceAt(file, rank, new Piece(type, color));

                    ++file;
                }
                else
                {
                    file += emptySpaces;
                }
            }
        }
    }

    private static void parseTurn(Board board, String moveString)
    {
        if (moveString.equalsIgnoreCase("w"))
            board.setTurn(PieceColor.WHITE);
        else if (moveString.equalsIgnoreCase("b"))
            board.setTurn(PieceColor.BLACK);
        else
            throw new NotationException("UCI turn string must be one of w, b, but was " + moveString);
    }

    private static void parseCastleAbility(Board board, String castleString)
    {
        char[] allowedLetters = {'K', 'Q', 'k', 'q', '-'};

        for (int i = 0; i < castleString.length(); ++i)
        {
            char current = castleString.charAt(i);

            boolean contains = false;

            for (int j = 0; j < allowedLetters.length; ++j)
                if (current == allowedLetters[j])
                {
                    contains = true;

                    break;
                }

            if (!contains)
                throw new NotationException("FEN castle string can only contain K,Q,k,q,-, but found " + current);
        }

        if (castleString.length() > 4)
            throw new NotationException("FEN castle string is too long");

        boolean whiteKingside = false;
        boolean whiteQueenside = false;
        boolean blackKingside = false;
        boolean blackQueenside = false;

        if (castleString.contains("K"))
            whiteKingside = true;

        if (castleString.contains("Q"))
            whiteQueenside = true;

        if (castleString.contains("k"))
            blackKingside = true;

        if (castleString.contains("q"))
            blackQueenside = true;
        
        CastleAbility whiteCastleAbility;
        CastleAbility blackCastleAbility;


        if (whiteKingside && whiteQueenside)
            whiteCastleAbility = CastleAbility.BOTH;
        else if (whiteKingside)
            whiteCastleAbility = CastleAbility.KINGSIDE;
        else if (whiteQueenside)
            whiteCastleAbility = CastleAbility.QUEENSIDE;
        else
            whiteCastleAbility = CastleAbility.NONE;

        if (blackKingside && blackQueenside)
            blackCastleAbility = CastleAbility.BOTH;
        else if (blackKingside)
            blackCastleAbility = CastleAbility.KINGSIDE;
        else if (blackQueenside)
            blackCastleAbility = CastleAbility.QUEENSIDE;
        else
            blackCastleAbility = CastleAbility.NONE;

        board.setCastleAbility(PieceColor.WHITE, whiteCastleAbility);
        board.setCastleAbility(PieceColor.BLACK, blackCastleAbility);

    }

    private static void parseEPSquare(Board board, String epString)
    {
        if (epString.equals("-"))
            board.setEnPassantTargetSquare(null);
        else
        {
            try
            {
                board.setEnPassantTargetSquare(AlgebraicNotation.getSquareFromName(epString));
            }

            catch (NotationException ex)
            {
                throw new NotationException("FEN E.P. target square error: " + ex.getMessage());
            }
        }
    }

    private static void parseHalfMove(Board board, String moveString)
    {
        try
        {
            int halfMove = Integer.parseInt(moveString);

            if (halfMove < 0)
                throw new NotationException("FEN string halfmove count must be positive or 0");

            board.setHalfmove(halfMove);
        }

        catch (NumberFormatException ex)
        {
            throw new NotationException("FEN string halfmove count must be number");
        }
    }

    private static void parseFullMove(Board board, String moveString)
    {
        try
        {
            int fullMove = Integer.parseInt(moveString);

            if (fullMove <= 0)
                throw new NotationException("FEN string fullmove count must be 1 or more");

            board.setFullMove(fullMove);
        }

        catch (NumberFormatException ex)
        {
            throw new NotationException("FEN string fullmove count must be number");
        }
    }

    public static Board getBoardFromFen(String fen)
    {
        Board board = new Board();

        String[] parts = fen.split(" ");

        if (parts.length != 6)
            throw new NotationException("FEN string must have 6 space-delimited parts");

        parseBoard(board, parts[0]);
        parseTurn(board, parts[1]);
        parseCastleAbility(board, parts[2]);
        parseEPSquare(board, parts[3]);
        parseHalfMove(board, parts[4]);
        parseFullMove(board, parts[5]);

        return board;
    }

    public static String getFenFromBoard(Board board)
    {
        StringBuilder builder = new StringBuilder();

        int blankSpaceCount = 0;

        for (int rank = 7; rank >= 0; --rank)
        {
            for (int file = 0; file < 8; ++file)
            {
                Piece piece = board.getPieceAt(file, rank);

                if (piece == null)
                    ++blankSpaceCount;
                else
                {
                    if (blankSpaceCount > 0)
                    {
                        builder.append(blankSpaceCount);

                        blankSpaceCount = 0;
                    }

                    builder.append(piece.getPieceLetter());
                }
            }

            if (blankSpaceCount > 0)
            {
                builder.append(blankSpaceCount);

                blankSpaceCount = 0;
            }

            if (rank != 0)
                builder.append('/');
        }

        builder.append(' ');

        PieceColor turn = board.getTurn();

        if (turn == PieceColor.WHITE)
            builder.append("w ");
        else
            builder.append("b ");

        CastleAbility whiteCastleAbility = board.getCastleAbility(PieceColor.WHITE);
        CastleAbility blackCastleAbility = board.getCastleAbility(PieceColor.BLACK);

        if (whiteCastleAbility == CastleAbility.NONE && blackCastleAbility == CastleAbility.NONE)
            builder.append("- ");
        else
        {
            if (whiteCastleAbility.canCastleKingside())
                builder.append('K');

            if (whiteCastleAbility.canCastleQueenside())
                builder.append('Q');

            if (blackCastleAbility.canCastleKingside())
                builder.append('k');

            if (blackCastleAbility.canCastleQueenside())
                builder.append('q');

            builder.append(' ');
        }

        Square epSquare = board.getEnPassantTargetSquare();

        if (epSquare == null)
            builder.append("- ");
        else
            builder.append(epSquare.toNotation()).append(' ');

        builder.append(board.getHalfmove()).append(' ').append(board.getFullMove());

        return builder.toString();
    }
}
