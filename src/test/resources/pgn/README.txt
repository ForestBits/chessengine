Each of these files contains many games in pgn notation. To create the move lists in the lan-games
directory, use the pgn-extract tool like this:

pgn-extract --notags --noresults -Wuci -o output.txt filename.pgn

This automatically gathers the data, converts it into the desired form, and makes a new file
with it, ready to test. 
