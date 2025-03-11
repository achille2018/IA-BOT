package awele.bot.competitor.ItachiBot;

import awele.core.Board;

public class BoardEvaluator {
    public static double evaluateBoard(Board board) {
        int myScore = board.getScore(board.getCurrentPlayer());
        int opponentScore = board.getScore(Board.otherPlayer(board.getCurrentPlayer()));
        return (myScore - opponentScore) + evaluatePosition(board);
    }

    public static double evaluateMove(Board board, int moveIndex) {
        if (board.getPlayerHoles()[moveIndex] == 0) return -Double.MAX_VALUE;
        return evaluateBoard(board);
    }

    private static double evaluatePosition(Board board) {
        double positionValue = 0;
        for (int i = 0; i < Board.NB_HOLES; i++) {
            if (board.getPlayerHoles()[i] > 0) {
                positionValue += evaluateHole(i, board);
            }
        }
        return positionValue;
    }

    private static double evaluateHole(int holeIndex, Board board) {
        int left = (holeIndex == 0) ? Board.NB_HOLES - 1 : holeIndex - 1;
        int right = (holeIndex == Board.NB_HOLES - 1) ? 0 : holeIndex + 1;
        return (board.getPlayerHoles()[left] <= 2 || board.getPlayerHoles()[right] <= 2) ? -0.5 : 0.5;
    }
}
