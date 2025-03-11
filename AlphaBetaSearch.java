package awele.bot.competitor.ItachiBot;

import awele.core.Board;
import awele.core.InvalidBotException;
import java.util.*;

public class AlphaBetaSearch {
    private int maxDepth;
    private final TranspositionTable transpositionTable;

    public AlphaBetaSearch(int maxDepth) {
        this.maxDepth = maxDepth;
        this.transpositionTable = new TranspositionTable();
    }

    public double[] getBestMove(Board board) {
        // System.out.println("Taille : " + transpositionTable.size());
        transpositionTable.clear();
        double[] bestMove = new double[Board.NB_HOLES];
        double alpha = -Double.MAX_VALUE;
        double beta = Double.MAX_VALUE;

        for(int i=0; i<Board.NB_HOLES; i++) {
            if(isValidMove(board, i)) {
                double[] moveArray = createMoveArray(i);
                Board simulatedBoard = null;
                try {
                    simulatedBoard = board.playMoveSimulationBoard(board.getCurrentPlayer(), moveArray);
                } catch (InvalidBotException e) {
                    throw new RuntimeException(e);
                }
                bestMove[i] = minValue(simulatedBoard, 1, alpha, beta);
            }
        }

        return bestMove;
    }

    private double maxValue(Board board, int depth, double alpha, double beta) {

        if (isTerminal(board, depth)) return BoardEvaluator.evaluateBoard(board);

        long hash = computeBoardHash(board);
        if (transpositionTable.contains(hash)) {
            TranspositionTable.TranspositionEntry entry = transpositionTable.get(hash);
            if (entry.depth >= depth) {
                if (entry.flag == TranspositionTable.TranspositionEntry.EXACT) return entry.value;
                if (entry.flag == TranspositionTable.TranspositionEntry.LOWERBOUND) alpha = Math.max(alpha, entry.value);
                else if (entry.flag == TranspositionTable.TranspositionEntry.UPPERBOUND) beta = Math.min(beta, entry.value);
                if (alpha >= beta) return entry.value;
            }
        }

        double value = -Double.MAX_VALUE;

        for (int move : getValidMoves(board)) {
            try {
                double[] moveArray = createMoveArray(move);
                Board simulatedBoard = board.playMoveSimulationBoard(board.getCurrentPlayer(), moveArray);
                value = Math.max(value, minValue(simulatedBoard, depth + 1, alpha, beta));
                alpha = Math.max(alpha, value);
                if (alpha >= beta) break;
            } catch (InvalidBotException ignored) {}
        }

        int flag = (value <= alpha) ? TranspositionTable.TranspositionEntry.UPPERBOUND
                : (value >= beta) ? TranspositionTable.TranspositionEntry.LOWERBOUND
                : TranspositionTable.TranspositionEntry.EXACT;
        transpositionTable.put(hash, value, depth, flag);

        return value;
    }

    private double minValue(Board board, int depth, double alpha, double beta) {

        if (isTerminal(board, depth)) return BoardEvaluator.evaluateBoard(board);

        long hash = computeBoardHash(board);
        if (transpositionTable.contains(hash)) {
            TranspositionTable.TranspositionEntry entry = transpositionTable.get(hash);
            if (entry.depth >= depth) {
                if (entry.flag == TranspositionTable.TranspositionEntry.EXACT) return entry.value;
                if (entry.flag == TranspositionTable.TranspositionEntry.LOWERBOUND) alpha = Math.max(alpha, entry.value);
                else if (entry.flag == TranspositionTable.TranspositionEntry.UPPERBOUND) beta = Math.min(beta, entry.value);
                if (alpha >= beta) return entry.value;
            }
        }

        double value = Double.MAX_VALUE;

        for (int move : getValidMoves(board)) {
            try {
                double[] moveArray = createMoveArray(move);
                Board simulatedBoard = board.playMoveSimulationBoard(board.getCurrentPlayer(), moveArray);
                value = Math.min(value, maxValue(simulatedBoard, depth + 1, alpha, beta));
                beta = Math.min(beta, value);
                if (alpha >= beta) break;
            } catch (InvalidBotException ignored) {}
        }

        int flag = (value <= alpha) ? TranspositionTable.TranspositionEntry.UPPERBOUND
                : (value >= beta) ? TranspositionTable.TranspositionEntry.LOWERBOUND
                : TranspositionTable.TranspositionEntry.EXACT;
        transpositionTable.put(hash, value, depth, flag);

        return value;
    }

    private boolean isTerminal(Board board, int depth) {
        return depth >= maxDepth || board.getNbSeeds() <= 1 || board.getScore(Board.otherPlayer(board.getCurrentPlayer())) >= 25;
    }

    private boolean isValidMove(Board board, int holeIndex) {
        return board.getPlayerHoles()[holeIndex] > 0;
    }

    private List<Integer> getValidMoves(Board board) {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < Board.NB_HOLES; i++) {
            //if (board.getPlayerHoles()[i] > 0) moves.add(i);
            moves.add(i);
        }
        return moves;
    }

    private double[] createMoveArray(int index) {
        double[] move = new double[Board.NB_HOLES];
        move[index] = 1;
        return move;
    }

    private long computeBoardHash(Board board) {
        long hash = 0;
        for (int i = 0; i < Board.NB_HOLES; i++) {
            hash = hash * 31 + board.getPlayerHoles()[i];
        }
        for (int i = 0; i < Board.NB_HOLES; i++) {
            hash = hash * 31 + board.getOpponentHoles()[i];
        }
        return hash;
    }
}
