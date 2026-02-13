package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XYWing implements SolvingTechnique {

    private static final Logger LOG = LoggerFactory.getLogger(XYWing.class);

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.ADVANCED;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        PuzzleType type = grid.getPuzzleType();

        // Find all cells with exactly 2 candidates
        List<int[]> biValueCells = new ArrayList<>();
        for (int r = 0; r < type.getRows(); r++) {
            for (int c = 0; c < type.getColumns(); c++) {
                if (grid.isEmpty(r, c) && grid.getCandidates(r, c).size() == 2) {
                    biValueCells.add(new int[]{r, c});
                }
            }
        }

        for (int[] pivot : biValueCells) {
            Set<Integer> pivotCands = grid.getCandidates(pivot[0], pivot[1]);
            Integer[] pv = pivotCands.toArray(new Integer[0]);
            int x = pv[0], y = pv[1];

            // Find wing1: peer of pivot with candidates {x, z} for some z != y
            List<int[]> peers = getPeers(grid, pivot[0], pivot[1]);

            for (int[] w1 : peers) {
                Set<Integer> w1Cands = grid.getCandidates(w1[0], w1[1]);
                if (w1Cands.size() != 2 || !w1Cands.contains(x)) continue;

                int z = -1;
                for (int v : w1Cands) {
                    if (v != x) z = v;
                }
                if (z == y) continue; // would make this a naked pair, not XY-Wing

                // Find wing2: peer of pivot with candidates {y, z}
                Set<Integer> needed = Set.of(y, z);
                for (int[] w2 : peers) {
                    if (w2[0] == w1[0] && w2[1] == w1[1]) continue;
                    Set<Integer> w2Cands = grid.getCandidates(w2[0], w2[1]);
                    if (!w2Cands.equals(needed)) continue;

                    // XY-Wing found! Eliminate z from cells that see both wings
                    boolean removed = false;
                    Set<Long> w1Peers = peerSet(grid, w1[0], w1[1]);
                    Set<Long> w2Peers = peerSet(grid, w2[0], w2[1]);

                    for (long key : w1Peers) {
                        if (w2Peers.contains(key)) {
                            int r = (int) (key >> 32);
                            int c = (int) key;
                            if (r == pivot[0] && c == pivot[1]) continue;
                            if (grid.isEmpty(r, c) && grid.removeCandidate(r, c, z)) {
                                removed = true;
                            }
                        }
                    }
                    if (removed) {
                        LOG.debug("XY-Wing: pivot [{},{}] wings [{},{}] [{},{}] — eliminated {}",
                                pivot[0], pivot[1], w1[0], w1[1], w2[0], w2[1], z);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<int[]> getPeers(CandidateGrid grid, int r, int c) {
        Set<Long> seen = new HashSet<>();
        List<int[]> peers = new ArrayList<>();

        for (int[] cell : grid.getEmptyCellsInRow(r)) {
            if (cell[1] != c) addUnique(seen, peers, cell);
        }
        for (int[] cell : grid.getEmptyCellsInCol(c)) {
            if (cell[0] != r) addUnique(seen, peers, cell);
        }
        for (int[] cell : grid.getEmptyCellsInBox(r, c)) {
            if (cell[0] != r || cell[1] != c) addUnique(seen, peers, cell);
        }
        return peers;
    }

    private Set<Long> peerSet(CandidateGrid grid, int r, int c) {
        Set<Long> peers = new HashSet<>();
        for (int[] cell : grid.getEmptyCellsInRow(r)) {
            if (cell[1] != c) peers.add(key(cell[0], cell[1]));
        }
        for (int[] cell : grid.getEmptyCellsInCol(c)) {
            if (cell[0] != r) peers.add(key(cell[0], cell[1]));
        }
        for (int[] cell : grid.getEmptyCellsInBox(r, c)) {
            if (cell[0] != r || cell[1] != c) peers.add(key(cell[0], cell[1]));
        }
        return peers;
    }

    private void addUnique(Set<Long> seen, List<int[]> list, int[] cell) {
        long key = key(cell[0], cell[1]);
        if (seen.add(key)) {
            list.add(cell);
        }
    }

    private long key(int r, int c) {
        return ((long) r << 32) | (c & 0xFFFFFFFFL);
    }

    @Override
    public String getName() {
        return "XY-Wing";
    }
}
