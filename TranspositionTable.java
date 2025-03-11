package awele.bot.competitor.ItachiBot;

import java.util.HashMap;
import java.util.Map;

public class TranspositionTable {
    private final Map<Long, TranspositionEntry> table;

    public TranspositionTable() {
        this.table = new HashMap<>();
    }

    public void clear() {
        table.clear();
    }

    public int size() {
        return table.size();
    }

    public boolean contains(long hash) {
        return table.containsKey(hash);
    }

    public TranspositionEntry get(long hash) {
        return table.get(hash);
    }

    public void put(long hash, double value, int depth, int flag) {
        table.put(hash, new TranspositionEntry(value, depth, flag));
    }

    public static class TranspositionEntry {
        static final int EXACT = 0, LOWERBOUND = 1, UPPERBOUND = 2;
        final double value;
        final int depth;
        final int flag;

        public TranspositionEntry(double value, int depth, int flag) {
            this.value = value;
            this.depth = depth;
            this.flag = flag;
        }
    }
}
