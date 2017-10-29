package com.jacobclarity.chessengine.uci.packet;

import com.jacobclarity.chessengine.game.Board;
import com.jacobclarity.chessengine.game.Fen;
import com.jacobclarity.chessengine.game.Move;
import com.jacobclarity.chessengine.game.NotationException;
import com.jacobclarity.chessengine.uci.UciParseException;
import com.jacobclarity.chessengine.uci.UciToken;
import com.jacobclarity.chessengine.uci.UciTokenData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Packet sent by GUI to set board position
 */
public class PositionPacket implements UciPacket
{
    private final Board board;

    private final Move[] moves;

    public PositionPacket(Board board, Move[] moves)
    {
        this.board = board;
        this.moves = moves;
    }

    public Board getBoard()
    {
        return board;
    }

    public Move[] getMoves()
    {
        return Arrays.copyOf(moves, moves.length);
    }

    @Override
    public String toCommandString()
    {
        StringBuilder commandString = new StringBuilder();

        if (board.equals(Board.START_POSITION))
            commandString.append("position startpos ");
        else
            commandString.append("position fen ").append(board.toFen()).append(' ');

        if (moves != null && moves.length > 0)
        {
            StringBuilder movesString = new StringBuilder();

            movesString.append("moves ");

            for (Move move : moves)
                movesString.append(move.toNotation()).append(' ');

            commandString.append(movesString.toString());
        }

        //either case ends with an extra space
        commandString.deleteCharAt(commandString.length() - 1);

        return commandString.toString();
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }
    
    public static UciPacket parsePacket(UciTokenData tokens[])
    {
        if (tokens.length == 1)
        {
            //the first token is "position". There is no "fen" or "moves", so the only
            //possibility is "startpos"

            String[] values = tokens[0].getValues();

            if (values.length != 1 || !values[0].equalsIgnoreCase("startpos"))
                throw new NotationException("expected \"startpos\"");

            return new PositionPacket(new Board(Board.START_POSITION), null);
        }
        else if (tokens.length == 2)
        {
            //either position startpos moves {moves...} or position fen {fen}

            if (tokens[0].getValues().length > 0)
            {
                //must be position startpos moves {moves...}

                String value = tokens[0].getValues()[0];

                if (!value.equalsIgnoreCase("startpos"))
                    throw new UciParseException("expected startpos, got " + value);

                //board is startpos, now get moves
                String[] moveStrings = tokens[1].getValues();

                if (moveStrings.length == 0)
                    throw new UciParseException("move list was empty");

                List<Move> moves = new ArrayList<Move>();

                for (String moveString : moveStrings)
                {
                    try
                    {
                        Move move = Move.fromNotation(moveString);

                        moves.add(move);
                    }

                    catch (NotationException ex)
                    {
                        throw new UciParseException("Move notation error in movelist: " + ex.getMessage());
                    }
                }

                return new PositionPacket(new Board(Board.START_POSITION), moves.toArray(new Move[moves.size()]));
            }
            else
            {
                //must be position fen {fen}

                if (tokens[1].getToken() != UciToken.FEN)
                    throw new UciParseException("expected token fen, got " + tokens[1].getToken().getLiteralString());

                String[] values = tokens[1].getValues();

                if (values.length == 0)
                    throw new UciParseException("expected {fen}, but no value occurred");

                String fenString = String.join(" ", values);

                try
                {
                    Board board = Fen.getBoardFromFen(fenString);

                    return new PositionPacket(board, null);
                }

                catch (NotationException ex)
                {
                    throw new UciParseException("failed to parse fen string: " + ex.getMessage());
                }
            }
        }
        else if (tokens.length == 3)
        {
            //must be position fen {fen} moves {moves...}

            if (tokens[1].getToken() != UciToken.FEN)
                throw new UciParseException("expected token fen, got " + tokens[1].getToken().getLiteralString());

            String[] values = tokens[1].getValues();

            if (values.length == 0)
                throw new UciParseException("expected {fen}, but no value occurred");

            String fenString = String.join(" ", values);

            Board board;

            try
            {
                 board = Fen.getBoardFromFen(fenString);
            }

            catch (NotationException ex)
            {
                throw new UciParseException("failed to parse fen string: " + ex.getMessage());
            }

            String[] moveStrings = tokens[2].getValues();

            if (moveStrings.length == 0)
                throw new UciParseException("move list was empty");

            List<Move> moves = new ArrayList<Move>();

            for (String moveString : moveStrings)
            {
                try
                {
                    Move move = Move.fromNotation(moveString);

                    moves.add(move);
                }

                catch (NotationException ex)
                {
                    throw new UciParseException("Move notation error in movelist: " + ex.getMessage());
                }
            }

            return new PositionPacket(board, moves.toArray(new Move[moves.size()]));
        }
        else
        {
            throw new UciParseException("Invalid number of tokens; position startpos [moves {moves...}], position fen {fen} [moves {moves...}");
        }
    }
}
