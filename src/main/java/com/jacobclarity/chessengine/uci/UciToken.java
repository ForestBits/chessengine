package com.jacobclarity.chessengine.uci;

/*
 * Tokens that exist in the UCI protocol, primarily keywords
 */
public enum UciToken
{
    UCI("uci"),
    DEBUG("debug"),
    IS_READY("isready"),
    SET_OPTION("setoption"),
    VALUE("value"),
    REGISTER("register"),
    UCI_NEW_GAME("ucinewgame"),
    POSITION("position"),
    FEN("fen"),
    MOVES("moves"),
    GO("go"),
    SEARCH_MOVES("searchmoves"),
    PONDER("ponder"),
    WHITE_TIME("wtime"),
    BLACK_TIME("btime"),
    WHITE_INCREMENT("winc"),
    BLACK_INCREMENT("binc"),
    MOVES_TO_GO("movestogo"),
    DEPTH("depth"),
    NODES("nodes"),
    MATE("mate"),
    MOVE_TIME("movetime"),
    INFINITE("infinite"),
    STOP("stop"),
    PONDER_HIT("ponderhit"),
    QUIT("quit"),
    ID("id"),
    NAME("name"),
    AUTHOR("author"),
    UCI_OK("uciok"),
    READY_OK("readyok"),
    BEST_MOVE("bestmove"),
    COPY_PROTECTION("copyprotection"),
    REGISTRATION("registration"),
    INFO("info"),
    SELECT_DEPTH("seldepth"),
    TIME("time"),
    PV("pv"),
    MULTI_PV("multipv"),
    SCORE("score"),
    CENTIPAWNS("cp"),
    LOWER_BOUND("lowerbound"),
    UPPER_BOUND("upperbound"),
    CURRENT_MOVE("currmove"),
    CURRENT_MOVE_NUMBER("currmovenumber"),
    HASH_FULL("hashfull"),
    NODES_PER_SECOND("nps"),
    TABLEBASE_HITS("tbhits"),
    CPU_LOAD("cpuload"),
    STRING("string"),
    REFUTATION("refutation"),
    CURRENT_LINE("currline"),
    OPTION("option"),
    TYPE("type"),
    DEFAULT("default"),
    MINIMUM("min"),
    MAXIMUM("max"),
    VAR("var"),
    UNKNOWN(null);



    private final String literalString;

    UciToken(String literalString)
    {
        this.literalString = literalString;
    }


    public String getLiteralString()
    {
        return literalString;
    }
}
