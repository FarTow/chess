# Chess
Here's my first attempt at creating an actual program that isn't bubble-sort or anything school related. If you couldn't guess, the project at hand is chess. This project is being made in Java 11 using Swing.

### Description
This project is 50% fun, 25% trying to prove that I'm not awful at programming, and 25% "learning" (which is really pulling my hair out from getting NullPointerException over and over). I've decided to upload to GitHub to practice version control and share my programs with others (for whatever reason). My end goal is to have a chess program with a variety of settings, an easy-to-follow interface with a clean display to match, and generally just a program I can be proud of.

### Installation
1. Clone the repository or download the repository as a .zip file.
   * Extract the files to desired location if downloaded as a .zip file.
2. Run *panels.Main* in your IDE or some other method I'm not aware of.
3. Enjoy!

### Progress
Below I have listed the various features/milestones/objectives/other synonym for goal that I have for this project. This includes both completed and future tasks.

<!-- If the tables looks extra surprisingly ugly, view it with "no wrap" instead of "soft wrap" -->

| Category                     | Incomplete Items                                                                                                                         | Complete Items                                                                                                                                             | Notes                                                                                      |
| ---------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ |
| Piece Movement Functionality |                                                                                                                                          | <p> - All General Piece Movement<br> - Check<br> - En Passant<br> - Castling<br> - Pawn Promotion </p>                                                     |                                                                                            |
| Start Menu                   |                                                                                                                                          | <p> - Start Button<br> - Timer Settings </p>                                                                                                               |                                                                                            |
| General Game Components      | <p> - Player Options<br> &nbsp;&nbsp;&nbsp;&nbsp;> Draw<br> &nbsp;&nbsp;&nbsp;&nbsp;> Resign<br> &nbsp;&nbsp;&nbsp;&nbsp;> Takeback </p> | <p> - Board<br> - Player Info Box<br> &nbsp;&nbsp;&nbsp;&nbsp;> Timer<br> &nbsp;&nbsp;&nbsp;&nbsp;> Piece Difference Display<br> - Move History Table </p> | <p> Player Options are currently planned <br> on being a part of the Player Info Box. </p> |
| Resizing                     |                                                                                                                                          | <p> - Start Menu<br> - General Game Components </p>                                                                                                        |                                                                                            |
| Game Over Condition Logic    | <p> - 3-Fold Repetition<br> - 50-Move Rule </p>                                                                                          | <p> - Checkmate<br> - Stalemate </p>                                                                                                                       |                                                                                            |
| Game Over State              | <p> - Back to Start Menu </p>                                                                                                            | <p> - Rematch </p>                                                                                                                                         |                                                                                            |
| Options                      | <p> - Toggle Move Hints<br> - Auto-Promote Pawn to Queen </p>                                                                            |                                                                                                                                                            | <p> Dialog hasn't been implemented yet, let alone functionality. </p>                      |
| Miscellaneous                | <p> - Chess AI<br> - Network Capabilities<br> - Make Application Executable </p>                                                         | <p> - Custom Made Pieces </p>                                                                                                                              |                                                                                            |

### Improvements
The program (unfortunately) contains many inefficient behaviors. The following table lists a description of all issues I am aware of.

| Class                     | Inefficiencies                                                                                                                                         | Correction                                                                                       | 
| ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------ |
| entities.Player           | <p> 1. updatePieceCount with every update </p>                                                                                                         | <p> 1. Update when a piece is removed or added to board. </p>                                    |
| panels.TimeSettings       | <p> 1. Performs the resize method in actionPerformed. </p>                                                                                             | <p> 1. Resize in panel.StartMenu's componentAdapter. </p>                                        |
| panels.PieceDiffDisplay   | <p> 1. updateDiffCount in actionPerformed. </p>                                                                                                        | <p> 1. Update the piece difference when the old count is different from the new count. </p>      |
| panels.Board              | <p> 1. actionPerformed contains a "check" to see if the board has been initially centered<br> 2. actionPerformed is in charge of formatting time. </p> | <p> 1. Center on initialization<br> 2. Format time when a player's second count crosses 60. </p> |
| panels.MoveHistoryDisplay | <p> 1. Performs the resize method in actionPerformed. </p>                                                                                             | <p> 1. Resize in panel.Game's componentAdapter. </p>                                             |

### Resources
* Piece design by [Yi Jun Yang](https://www.instagram.com/y.yang.art/)
