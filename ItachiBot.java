package awele.bot.competitor.ItachiBot;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

public class ItachiBot extends CompetitorBot {
    private static final int MAX_DEPTH = 10;
    private final AlphaBetaSearch alphaBetaSearch;

    public ItachiBot() throws InvalidBotException {
        this.setBotName("Itachi AI");
        this.addAuthor("Thierno Hamidou DIALLO");
        this.addAuthor("Achile");
        this.alphaBetaSearch = new AlphaBetaSearch(MAX_DEPTH);
    }

    @Override
    public void initialize() {}

    @Override
    public void finish() {}

    @Override
    public void learn() {}

    @Override
    public double[] getDecision(Board board) {
        return alphaBetaSearch.getBestMove(board);
    }
}
