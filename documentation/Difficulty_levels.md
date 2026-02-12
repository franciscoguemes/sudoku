Difficulty levels
=============================================


Here's my recommendation, from simplest to most robust:

  ---
Option 1: Clue Count (Simple, quick to implement)

Just vary the percentage of given cells per difficulty. For a standard 9x9 (81 cells):
┌────────────┬───────┬────────┐
│ Difficulty │ Clues │ Fill % │
├────────────┼───────┼────────┤
│ Easy       │ 38-45 │ ~50%   │
├────────────┼───────┼────────┤
│ Medium     │ 30-37 │ ~40%   │
├────────────┼───────┼────────┤
│ Hard       │ 26-29 │ ~34%   │
├────────────┼───────┼────────┤
│ Expert     │ 22-25 │ ~29%   │
├────────────┼───────┼────────┤
│ Master     │ 19-21 │ ~25%   │
├────────────┼───────┼────────┤
│ Extreme    │ 17-18 │ ~21%   │
└────────────┴───────┴────────┘
Pros: Trivial to implement — just create a Difficulty enum with fill ratios and pass it to generateRandomSudoku.
Cons: Clue count alone doesn't guarantee perceived difficulty. A 25-clue puzzle could be trivially solvable with naked singles if clues are well-placed, while a 35-clue puzzle could require advanced techniques if
clues are poorly distributed.

Option 2: Clue Count + Uniqueness Check (Recommended starting point)

Same as Option 1, but after removing cells, verify the puzzle has exactly one solution. This is important because:
- Multiple-solution puzzles feel unfair to players
- Fewer clues dramatically increase the chance of non-unique solutions
- The check uses your existing backtracking solver (modified to count solutions, stopping at 2)

This is what I'd recommend implementing first — it gives you meaningful difficulty tiers with a proper guarantee that puzzles are solvable.

Option 3: Technique-Based Grading (Most accurate, most complex)

Instead of just counting clues, you'd build a human-like solver that applies techniques in order of difficulty:

1. Easy: Naked singles (only one value fits a cell)
2. Medium: Hidden singles (value can only go in one place in a row/col/box)
3. Hard: Naked pairs/triples, pointing pairs
4. Expert: X-wing, swordfish, box/line reduction
5. Master: XY-wing, unique rectangles
6. Extreme: Forcing chains, backtracking required

The generation process becomes:
1. Generate a full solution
2. Remove cells one at a time in random order
3. After each removal, solve using only techniques up to the target difficulty
4. If solvable → keep the removal. If not → put the cell back
5. Stop when you can't remove any more cells

Pros: Truly accurate difficulty grading — matches what players experience.
Cons: Requires implementing a constraint-based solver with individual technique detectors. Significant effort (each technique is its own algorithm).

  ---
My Recommendation

Start with Option 2 (clue count + uniqueness). It gives you a real Difficulty enum that maps to your UI's 6 levels, is proportional in fill rates across all puzzle sizes (6x6 through 16x16), and guarantees valid
puzzles. The implementation would be:

1. Create a Difficulty enum with fill ratios for each level
2. Modify generateRandomSudoku(PuzzleType, Difficulty)
3. Add a hasUniqueSolution() method (modify backtracker to count up to 2 solutions)
4. If removing a cell creates multiple solutions, put it back and try another

Later, if you want more accurate grading, you can layer Option 3 on top as a validation/scoring step — generate with clue counts, then score the result and regenerate if it doesn't match the target difficulty.


---

Solver Foundation (com.franciscoguemes.sudoku.solver):
- TechniqueLevel.java — Enum with 6 difficulty tiers (NAKED_SINGLE through BACKTRACKING)
- SolvingTechnique.java — Interface for all techniques
- SolveResult.java — Record holding solve outcome and hardest technique used
- CandidateGrid.java — Core data structure wrapping a Puzzle with per-cell candidate sets
- TechniqueSolver.java — Orchestrator that applies techniques in order, always restarting from easiest after progress

10 Solving Techniques (com.franciscoguemes.sudoku.solver.techniques):
┌──────────────────┬────────┬──────────────────────────────────────────────┐
│    Technique     │ Level  │                   Strategy                   │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ NakedSingle      │ EASY   │ Cell with exactly 1 candidate                │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ HiddenSingle     │ MEDIUM │ Value that fits in only 1 cell in a region   │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ NakedPair        │ HARD   │ 2 cells sharing same 2-candidate set         │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ NakedTriple      │ HARD   │ 3 cells whose union = 3 candidates           │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ PointingPair     │ HARD   │ Box candidates confined to one row/col       │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ BoxLineReduction │ EXPERT │ Row/col candidates confined to one box       │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ XWing            │ EXPERT │ 2 rows with candidate in same 2 columns      │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ Swordfish        │ EXPERT │ 3-row extension of X-Wing                    │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ XYWing           │ MASTER │ Pivot + 2 wings eliminating shared candidate │
├──────────────────┼────────┼──────────────────────────────────────────────┤
│ UniqueRectangle  │ MASTER │ Type 1 deadly pattern avoidance              │
└──────────────────┴────────┴──────────────────────────────────────────────┘
