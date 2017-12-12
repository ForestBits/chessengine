package com.jacobclarity.chessengine.uci;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Tokenize UCI input commands
 */
public class UciTokenizer
{
    private static final Map<String, UciToken> literalMap = new HashMap<>();

    static
    {
        UciToken[] tokens = UciToken.values();

        for (UciToken token : tokens)
            literalMap.put(token.getLiteralString(), token);
    }

    public UciTokenizer()
    {

    }

    /*
     * Gets whitespace-separated strings in a line
     */
    private String[] getTokenLiteralsFromLine(String line)
    {
        List<String> literals = new ArrayList<>();

        boolean inToken = false;

        int tokenStart = -1;

        for (int i = 0; i < line.length(); ++i)
        {
            boolean whitespace = Character.isWhitespace(line.charAt(i));

            if (whitespace)
            {
                if (inToken)
                {
                    literals.add(line.substring(tokenStart, i));

                    inToken = false;
                }
            }
            else
            {
                if (!inToken)
                {
                    tokenStart = i;

                    inToken = true;
                }
            }
        }

        if (inToken)
            literals.add(line.substring(tokenStart));

        return literals.toArray(new String[literals.size()]);
    }

    /*
     * Given a line of text, converts it to UCI tokens and associated metadata
     */
    public UciTokenData[] getTokensFromLine(String line)
    {
        List<UciTokenData> tokens = new ArrayList<>();

        String[] literals = getTokenLiteralsFromLine(line);

        UciToken lastToken = null;

        List<String> currentTokenLiterals = new ArrayList<>();

        //the goal is to collect a list of each token and any literals that follow it untill the next token
        for (String literal : literals)
        {
            UciToken token = literalMap.get(literal);

            if (token == null)
            {
                //on a non-token literal

                currentTokenLiterals.add(literal);
            }
            else
            {
                //on a new token, add last one

                String[] tokenLiterals = currentTokenLiterals.toArray(new String[currentTokenLiterals.size()]);

                if (lastToken != null) //if we have come across a token yet
                    tokens.add(new UciTokenData(lastToken, tokenLiterals));
                else if (currentTokenLiterals.size() > 0) //we have never seen a token, but we have literals at the start of the string
                    tokens.add(new UciTokenData(UciToken.UNKNOWN, tokenLiterals));

                lastToken = token;

                currentTokenLiterals.clear();
            }
        }

        //the last token literal will not be added in the above loop, so the logic is duplicated here
        String[] tokenLiterals = currentTokenLiterals.toArray(new String[currentTokenLiterals.size()]);

        if (lastToken != null) //if we have come across a token yet
            tokens.add(new UciTokenData(lastToken, tokenLiterals));
        else if (currentTokenLiterals.size() > 0) //we have never seen a token, but we have literals at the start of the string
            tokens.add(new UciTokenData(UciToken.UNKNOWN, tokenLiterals));

        return tokens.toArray(new UciTokenData[tokens.size()]);
    }
}
