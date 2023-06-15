import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Game {

    public static void main(String[] args) {

        Ctrl.startGame();
    }

}

class Puzzle {

    // 4x4 GameBoard board, nums Helper vector, and Game Completion Status, numMoves
    public static byte[][] board = new byte[4][4];
    static Vector<Byte> nums = new Vector<>();
    public static boolean complete = false;
    public static int movesCount = 0;

    public static void initializeBoard() {

        // fill board
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                do {
                    board[i][j] = (byte) (rand.nextInt(16) + 1);
                } while (nums.contains(board[i][j]));

                nums.add(board[i][j]);
            }
        }
    }

    // checks completion status (Ascending order)
    public static boolean checkBoard(byte board[][]) {
        byte prev = board[0][0];

        for (byte[] row : board) {
            for (byte tile : row) {
                if (tile < prev) {
                    return false;
                }
                prev = tile;
            }
        }
        complete = true;
        return true;
    }

    // validate move (Checks input length, and bounds)
    public static boolean validTile(String move) {
        if (move.length() != 2) {
            System.out.println("Invalid input, please enter a valid coordinate");
            return false;
        } else {
            char col = move.charAt(0);
            char row = move.charAt(1);
            if (!Character.isLetterOrDigit(col) || (col < 'A' || col > 'D')) {
            System.out.println("Invalid input, please enter a valid coordinate");
            return false;
        }
        if (!Character.isDigit(row) || (row < '1' || row > '4')) {
            System.out.println("Invalid input, please enter a valid coordinate");
            return false;
        }
            return true;

        }
    }

    public static byte[] findBlank(byte[][] board) {
        byte[] blank = new byte[2];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 16) {
                    blank[0] = (byte) i;
                    blank[1] = (byte) j;
                }
            }
        }
        return blank;
    }

    public static boolean validMove(byte[][] board, byte[] coordinates) {
        byte[] blank = findBlank(board);
        byte rowBlank = blank[0];
        byte colBlank = blank[1];
        byte rowMove = coordinates[0];
        byte colMove = coordinates[1];

        if (rowMove == rowBlank && colMove == colBlank) {
            System.out.println("Invalid move, please enter another coordinate");
            return false;
        }

        if (rowMove == rowBlank && (colMove == colBlank + 1 || colMove == colBlank - 1)) {
            return true;
        }

        if (colMove == colBlank && (rowMove == rowBlank + 1 || rowMove == rowBlank - 1)) {
            return true;
        }

        System.out.println("Invalid move, please enter another coordinate");
        return false;
    }

    public static void updateBoard(byte[][] board, byte[] blank, byte[] coordinates) {
        byte rowBlank = blank[0];
        byte colBlank = blank[1];
        byte rowMove = coordinates[0];
        byte colMove = coordinates[1];

        byte temp = board[rowBlank][colBlank];
        board[rowBlank][colBlank] = board[rowMove][colMove];
        board[rowMove][colMove] = temp;

        View.printBoard(board);
    }
}

class View {

    // Welcome Message and Objective
    public static void startScreen() {
        System.out.println("Get ready to test your puzzle-solving skills with the classic 15 Puzzle!");
        System.out.println("To play, enter the Coordinates of the tile you wish to move (e.g A1 or D4)");
        System.out.println("Your objective is to sort the board in ascending order, with the blank tile in the bottom right corner");
        System.out.println("with tile A1 being the smallest number and tile D3 the largest.");
        System.out.println("Best of luck!");
        System.out.println();
    }

    // Prints board to screen
  public static void printBoard(byte[][] board) {
    char colBoard = 'A';
    System.out.println("    1    2    3    4");
    System.out.println("  ╔════╦════╦════╦════╗");
    for (int i = 0; i < 4; i++) {
        System.out.print(colBoard + " ║ ");
        colBoard++;
        for (int j = 0; j < 4; j++) {
            if (board[i][j] == 16) {
                System.out.print("   ");
            } else {
                if (board[i][j] < 10) {
                    System.out.print(" ");
                }
                System.out.print(board[i][j] + " ");
            }
            if (j % 4 != 3) {
                System.out.print("│ ");
            }
        }
        System.out.println("║");
        if (i % 4 != 3) {
            System.out.println("  ╠════╬════╬════╬════╣");
        }
    }
    System.out.println("  ╚════╩════╩════╩════╝");
}


    // User input for move
    public static String getMove() {
        String move;
        Scanner in = new Scanner(System.in);
        move = in.nextLine();
        return move;

    }

    //Win Screen
    public static void winScreen() {
        System.out.println("Game Completed! You solved the puzzle in only " + Puzzle.movesCount + " moves!");
        System.out.println("Would you like to play again? (Y/N)");
    }

}

class Ctrl {

    public static void startGame() {
        View.startScreen();
        Puzzle.initializeBoard();
        View.printBoard(Puzzle.board);
        while (!Puzzle.complete) {
            String move = View.getMove();
            Puzzle.movesCount++; // increment moves counter
            move = move.toUpperCase();

            if (Puzzle.validTile(move)) {
                // locate blank tile
                byte[] blank = Puzzle.findBlank(Puzzle.board);

                // convert move to coordinates
                byte[] coordinates = new byte[2];
                coordinates[0] = (byte) (move.charAt(0) - 'A');
                coordinates[1] = (byte) (move.charAt(1) - '1');

                // check if move is valid
                if (Puzzle.validMove(Puzzle.board, coordinates)) {
                    // update and print board
                    Puzzle.updateBoard(Puzzle.board, blank, coordinates);
                }

            }
        }
        // Game Over screen with movesCount
        View.winScreen();
        
    }

}