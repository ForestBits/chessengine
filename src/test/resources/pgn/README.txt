Each of these files contains many games in pgn notation. To create the move lists in the lan-games
directory, use the pgn-extract tool like this:

pgn-extract --notags --noresults -Wuci -o output.txt filename.pgn

This automatically gathers the data, converts it into the desired form, and makes a new file
with it, ready to test. 

Some of the files contain invalid data that gets passed through pgn-extract. Normally, PGN games look like this:

[tags]...

move1 move2... moveX 0-1

However, some games in the dataset don't contain moves at all:

[tags]...

0-1

pgn-extract then passes the 0-1 by itself to the output, rather than a movelist.

The fix is to use the script convert-pgn-to-san. The script runs pgn-extract with the same settings as above, but then filters through 
to find the invalid results and removes them. Usage: convert-pgn-to-san input.pgn output.txt
