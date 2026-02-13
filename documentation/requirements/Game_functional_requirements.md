Game Functional Requirements
================================================


I have decided to split my application into two: A Sudoku editor (EditorApp) and a Sudoku game. I would like you to
build the Sudoku game GUI based on JavaFx. In order to do that please implement a new class named `GameApp.java` in the
package `gui`. The GUI of the game must be similar to the one in the image `resources/UI.png`. If you need to create any
sub component for the UI of the game, please do it in the package `game`.
At the moment my game engine has no different levels of difficulty, but you can place the text labels (like in the
image) anyway since I plan to add the levels in the future.
Above the labels for the difficulty levels, please add another row of labels with the different types of Sudoku's that
my
engine can generate.(See the class `PuzzleType` to see the different types of Sudoku's).
The functionality of the game must be the following:

- By default the project must start the game when executed in the console `mvn javafx:run`
- When the game app is executed, by default it will create and show on the screen a new 9x9 Sudoku so the user can start
  directly playing. The difficulty label (on the top) with the text "Extreme" will be highlited. And the label
  corresponding to the 9x9 Sudoku game will be highlighted.
- If the user clicks on the "New Game" button a new Sudoku puzzle will be shown on the screen. The puzzle must match the
  selection of the labels (difficulty and Sudoku type).
- If the user clicks the top labels (difficulty labels, or Sudoku type labels), the application will generate a new
  puzzle according to the selection and it will highlight the button that the user clicked.

At the moment when the user introduce a wrong number in any square of the Sudoku board nothing happens. I would like to
change this behaviour by the following: When the user inputs a wrong number in any square of the Sudoku. The number will
appear in the selected square in red color and the mistakes counter will be increased by 1. When the user reaches 3
mistakes then the game is over. A dialog will be shown notifying the user that the game is over. The dialog will contain
two buttons one that says "Second chance" and another that says "New game".
If the user clicks on the button "Second chance" the user will be able to continue playing the same game and the
mistakes counter will be decreased by one mistake. If the user clicks on "New game" a new Sudoku game will be generated.

Now I would like you to add the "notes mode" functionality. When the user click on the notes button then the notes mode
functionality enables. While the notes mode functionality is enabled, the input of the user will not be taken into
account for the Sudoku game. The input will be store in the cell as a note for the user. The way notes are displayed are
different than the actual values of the cell. You can see how notes are displayed in the UI in the pictures
`UI_notes_mode_on_full.png` to see a board full of notes. While the notes mode is enabled, the user's input will be
displayed as a note. The user can note as many numbers (or numbers and letters if the Sudoku is 12x12 or 16x16) as he
liks in the cell. The noted numbers will be displayed in hierarchical order (as in the image, each noted number will
always take the same place in the cell).
When the user clicks again in the notes button the "notes mode" will be disabled and the user will be playing normally
the Sudoku.
Please take into account that the "notes mode" is only relevant if the cell is empty, no value has been yet assigned to
the cell. Otherwise the notes will be ignored and the value of the cell will remain.

Now I would like you to implement the undo functionality. When the user press the button "Undo" this is the expected
behavior.

- If the last value that the user input was a note, that specific note will be deleted.
- If the last value that the user input was a value in the Sudoku square, the value will be deleted and the square will
  be restored, that means if the square was previously empty (no value & no notes) then the square will be empty again,
  but if the square had previously notes, then the notes will be restored.
  Please note that the undo functionality does not affect at all the mistakes counter. If the user input a wrong value
  in a cell and then press the "Undo" button, the behavior will be the same as in any other cell, but the mistake will
  remain in the counter.

When the game starts the cell in the upper left corner of the board must be selected.
At the moment the dashboard only supports input through the mouse. I would like to add support for the keyboard. The
player must be able to move across the Sudoku board using the arrows in the keyboard. And then when the user press a key
that corresponds to a value for that type of Sudoku, the value will be inputted in the Sudoku (or taken as a note in
case
of being in the notes mode).

I would like you to add now the erase functionality. When the user press the "Erase" button the cell is completely
empty.

- If the cell is immutable (the game started with a value in that cell), then the erase functionality is ignored, that
  cell must never change.
- If the cell contained a value that the user input, then the cell will be empty.
- If the cell contained notes, all notes will be deleted at once.

Please note that the changes performed by the erase functionality must be undone with the "Undo" button:

- If the cell contained a value that the user input, then the value will be restored in the cell.
- If the cell contained notes, all notes will be restored at once.

---

# Number exhaustion

I would like you to add the following feature to the GameApp. Once a number has been filled in all possible areas in the
board (row, columns, boxes) the button corresponding to that number must become unavailable. E.g. In the picture below
![number exhaustion picture](/documentation/images/UI_disable_number_after_exhaustion.png), the number "4" has been
filled everywhere
possible in the
board therefore in the right panel the button corresponding to the number "4" must be disabled.

---

# Update notes on peers

Once a correct number has been filled in the board (this does not apply for wrong numbers) the notes for all cells in
the same row, column and box must be updated. E.g. You can see in the image below
![update notes after input](/documentation/images/UI_update_notes_after_input.png) that the number "4" has been inserted
in the cell with coordinates (4,8), therefore the notes in all the highlighted cells must be updated and any annotated "
4" in any of those cells must be deleted. In the picture you can see that the cell with coordinates (5,5) contains an
annotated "4" that must be deleted.

Please bear in mind that if the user then clicks the "Undo" button, all the notes automatically updated will not be
restored, only the previous values/notes for that cell will be restored.

---

# Timer

In the UI there is a timer. I would like you to implement the timer functionality. When the game starts the timer must
start and start counting like a clock. The timer will only stop when the game finishes because the board is full.

When the user starts a new game the timer will restart again with the new game.

If the user exhausts the mistakes counter, the timer will not stop. If the user chose to continue the game the timer
will simply continue.

I want you to place a "Pause" button (if possible with the pause icon ("\u23F8" - ⏸ )) next to the timer (at the right).
If the user clicks the pause button:
- The timer will pause and the button will then will take the form of "Resume" button ( if possible with the resume
  icon ("\u25B6"; // ▶  )).
- The board will be hidden.

When the user resume the timer (after the timer has been paused):
- The timer will continue counting the time from its previous instant.
- The board will be displayed again.


  Behavior summary
  ┌────────────────────────┬───────────────────────────────────┐
  │         Event          │           Timer action            │
  ├────────────────────────┼───────────────────────────────────┤
  │ New game               │ Reset to 00:00, start             │
  ├────────────────────────┼───────────────────────────────────┤
  │ Board full (win)       │ Stop                              │
  ├────────────────────────┼───────────────────────────────────┤
  │ 3 mistakes (game over) │ Keeps running                     │
  ├────────────────────────┼───────────────────────────────────┤
  │ Pause button clicked   │ Pause, hide board                 │
  ├────────────────────────┼───────────────────────────────────┤
  │ Resume button clicked  │ Resume from same time, show board │
  └────────────────────────┴───────────────────────────────────┘