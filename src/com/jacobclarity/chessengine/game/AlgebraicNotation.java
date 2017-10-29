package com.jacobclarity.chessengine.game;

public class AlgebraicNotation
{
    private static char[] fileLetters = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    //return notation representing particular square
    public static String getSquareName(Square square)
    {
        return fileLetters[square.getFile()] + "" + (square.getRank() + 1);
    }

    public static Square getSquareFromName(String squareName)
    {
        if (squareName.length() != 2)
            throw new NotationException("Square notation should only contain two characters");

        char fileLetter = squareName.charAt(0);
        char rankNumber = squareName.charAt(1);

        int file = -1;
        int rank = -1;

        for (int i = 0; i < fileLetters.length; ++i)
            if (fileLetters[i] == fileLetter)
            {
                file = i;

                break;
            }

        if (file == -1) //letter didn't match
            throw new NotationException("'" + fileLetter + "' is not a valid file letter");

        try
        {
            rank = Integer.parseInt(rankNumber + "") - 1;
        }

        catch (NumberFormatException ex)
        {
            throw new NotationException("could not parse '" + rankNumber + "' as rank number");
        }

        if (rank < 0 || rank > 7)
            throw new NotationException("rank number out of range");

        return new Square(file, rank);
    }
}
