import com.jacobclarity.chessengine.game.Board;
import com.jacobclarity.chessengine.game.Pgn;
import com.jacobclarity.chessengine.game.Move;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MovegenTest
{
    //useful test data tips:
    //pgn-extract (https://www.cs.kent.ac.uk/people/staff/djb/pgn-extract/) is a useful tool for scraping relevant data
    //from PGN files - in this case, converting a PGN file into long algebraic notation instead of standard notation
    //buildmystring.com makes creating multiline string literals of things such as PGN files or move lists much easier


    //tests the move generation by comparing against actual, legal games
    //all moves made in the legal game should be found as a legal move by the engine
    @Test
    public void existingGamesShouldBeLegal()
    {
        //load in test data

        //each entry is a list of moves in LAN format for a complete game
        List<String> moveLists = new ArrayList<>();

        try
        {
            File directory = new File("src/test/resources/lan-games");

            //the contents of this directory should only contain the desired files and no other files or directories
            File[] files = directory.listFiles();

            for (File f : files)
            {
                String fileContents = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));

                //every line in the file is either blank, or contains a full game

                BufferedReader reader = new BufferedReader(new StringReader(fileContents));

                while (true)
                {
                    String line = reader.readLine();

                    if (line == null) //end of file
                        break;

                    if (!line.isEmpty())
                        moveLists.add(line);
                }
            }
        }

        catch (IOException ex)
        {
            throw new RuntimeException("Failed to read test cases from file", ex);
        }

        for (String moveList : moveLists)
        {
            //for each game, play it out and ensure that the move next played is also found by the engine

            Board board = new Board(Board.START_POSITION);

            String[] moves = moveList.split(" ");

            for (String moveString : moves)
            {
                //for each move in each game, see if the engine found it
                Move move = Move.fromNotation(moveString);

                Move[] legalMoves = board.getLegalMovesForColor(board.getTurn());

                boolean contains = false;

                for (Move legalMove : legalMoves)
                    if (legalMove.equals(move))
                    {
                        contains = true;

                        break;
                    }

                Assert.assertTrue("Engine didn't generate the matching legal move", contains);

                //advance board state to prepare for next move
                board.performMove(move);
            }
        }
    }
}
