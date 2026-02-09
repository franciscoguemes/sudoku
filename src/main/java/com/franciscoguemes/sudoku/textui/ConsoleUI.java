package com.franciscoguemes.sudoku.textui;

import com.franciscoguemes.sudoku.io.CsvPuzzleReader;
import com.franciscoguemes.sudoku.io.PuzzleReader;
import com.franciscoguemes.sudoku.io.SudokuFormatPuzzleReader;
import com.franciscoguemes.sudoku.model.Generator;
import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ConsoleUI {

    private final PuzzlePrinter printer = new PuzzlePrinter();

    public void run(String[] args) {
        if (args.length == 0) {
            runInteractive();
        } else {
            runWithFile(Path.of(args[0]));
        }
    }

    private void runInteractive() {
        PuzzleType type = promptPuzzleType();
        System.out.println("Generating " + type.getDescription() + "...");
        Generator generator = new Generator();
        Puzzle puzzle = generator.generateRandomSudoku(type);
        System.out.println();
        printer.print(puzzle);
    }

    private void runWithFile(Path filePath) {
        if (!Files.exists(filePath)) {
            System.err.println("Error: File not found: " + filePath);
            return;
        }

        PuzzleReader reader = getReaderForFile(filePath);
        if (reader == null) {
            System.err.println("Error: Unsupported file format: " + filePath.getFileName());
            System.err.println("Supported formats: .csv, .sudoku");
            return;
        }

        try {
            Puzzle puzzle = reader.read(filePath);
            System.out.println("Puzzle loaded from: " + filePath.getFileName());
            System.out.println();
            printer.print(puzzle);
        } catch (IOException e) {
            System.err.println("Error reading puzzle file: " + e.getMessage());
        }
    }

    private PuzzleType promptPuzzleType() {
        Scanner scanner = new Scanner(System.in);
        PuzzleType[] types = PuzzleType.values();

        System.out.println("Select puzzle type:");
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  %d) %s%n", i + 1, types[i].getDescription());
        }

        while (true) {
            System.out.printf("Enter choice [1-%d]: ", types.length);
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= types.length) {
                    return types[choice - 1];
                }
            } catch (NumberFormatException e) {
                // fall through to error message
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }

    private PuzzleReader getReaderForFile(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".csv")) {
            return new CsvPuzzleReader();
        } else if (fileName.endsWith(".sudoku")) {
            return new SudokuFormatPuzzleReader();
        }
        return null;
    }
}
