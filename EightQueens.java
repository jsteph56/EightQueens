import java.util.*;

public class EightQueens {
    
    int[][] board = new int[8][8];
    int[][] boardCopy = new int[8][8];
    int heuristic = 0;
    int neighborStates = 0;
    int stateChanges = 0;
    int resets = 0;

    /**
     * @param args the command line arguments
     */
    public EightQueens() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.board[i][j] = 0;
            }
        }

        this.heuristic = heuristic(this.board);
    }

    public static void main(String[] args) {
        EightQueens init = new EightQueens();
        init.randomize();
        init.hillClimbing();
    }
    
    public int heuristic(int[][] board) {
        int count = 0;
        int inc;
        
        
        for(inc = 0; inc < 7; inc++){   
            for(int i = 0; i < 8; i++) {
                if(board[i][inc] == 1) {
                    if(searchRow(board, i, inc)) {
                        count++;
                    }
                    if(searchDiag(board, i, inc)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static boolean searchRow(int[][] board, int row, int column) {
        for(int i = column + 1; i < 8; i++) {
            if(board[row][i] == 1) {
                return true;
            }
        }

        return false;
    }
    
    public boolean searchDiag(int[][] board, int row, int column) {
        boolean conflict = false;
        
        if(column == 7) {
            return conflict;
        }else if(row == 0) {
            conflict = searchLowerDiag(board, row, column);
        }else if(row == 7) {
            conflict = searchUpperDiag(board, row, column);
        }else {
            if(searchUpperDiag(board, row, column) || searchLowerDiag(board, row, column)) conflict = true;
        }
        
        return conflict;
    }
    
    public boolean searchUpperDiag(int[][] board, int row, int column) {
        
        while(column != 6) {
            for(int i = row; i > 0; i--) {
                if(board[i - 1][column + 1] == 1) {
                    return true;
                }
                
                column++;
                if (column > 6){
                    return false;
                }
            }
        }
        
        return false;
    }
    
    public boolean searchLowerDiag(int[][] board, int row, int column) {
        
        for(int i = row; i < 7; i++) {
            if(board[i + 1][column + 1] == 1) {
                return true;
            }
            
            column++;
            if (column > 6){
                return false;
            }
        }
        
        return false;
    }
    
    public void hillClimbing() {
        int[][] hVals = new int[8][8];
        heuristic = heuristic(this.board);
        int prevQueen = 0;

        while(true) {
            int column = 0;
            this.boardCopy = copyBoard(this.board, this.boardCopy);

            while(column < 8) {
                for(int i = 0; i < 8; i++) {
                    this.boardCopy[i][column] = 0;
                }

                for(int i = 0; i < 8; i++) {
                    if(this.board[i][column] == 1) {
                        prevQueen = i;
                    }

                    this.boardCopy[i][column] = 1;
                    hVals[i][column] = heuristic(this.boardCopy);
                    this.boardCopy[i][column] = 0;
                }

                this.boardCopy[prevQueen][column] = 1;
                column++;
            }

            if(ifReset(hVals)) {
                this.board = reset(this.board);
                randomize();
                System.out.println("Restart");
                this.resets++;
            }

            int bestRow = bestRow(hVals);
            int bestColumn = bestColumn(hVals);
             
            for(int i = 0; i < 8; i++) {
                this.board[i][bestColumn] = 0;
            }

            this.board[bestRow][bestColumn] = 1;
            heuristic = heuristic(this.board);
            this.stateChanges++;

            if(heuristic(this.board) == 0) {
                System.out.print(toString(this.board));
                System.out.println("Solution Found!");
                System.out.println("State changes: " + this.stateChanges);
                System.out.println("Restarts " + this.resets);
                break;
            }

            System.out.println("Current h: " + this.heuristic);
            System.out.print(toString(this.board));
            System.out.println("Neighbors found with lower h: " + this.neighborStates);
            System.out.println("Setting new current state");
            System.out.println();
        }
    }
    
    /**
     * randomizes current board state
     */
    public void randomize() {
        Random rand = new Random();
        
        for(int i = 0; i < 8; i++) {
            int temp = rand.nextInt(8);
            this.board[temp][i] = 1;
        }

        this.heuristic = heuristic(this.board);
    }

    public int[][] copyBoard(int[][] startBoard, int[][] newBoard) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                newBoard[i][j] = startBoard[i][j];
            }
        }
        
        return newBoard;
    }

    public int[][] reset(int[][] board) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }

        return board;
    }

    public int bestColumn(int[][] hVals) {
        int count = 0;
        int bestHeuristic = 0;
        int minHeuristic = 8;
        
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(hVals[i][j] < minHeuristic) {
                    minHeuristic = hVals[i][j];
                    bestHeuristic = j;
                }

                if(hVals[i][j] < heuristic) {
                    count++;
                }
            }
        }
        
        this.neighborStates = count;
        return bestHeuristic;
    }

    public int bestRow(int[][] hVals) {
        int bestHeuristic = 0;
        int minHeuristic = 8;
        
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(hVals[i][j] < minHeuristic) {
                    minHeuristic = hVals[i][j];
                    bestHeuristic = i;
                }
            }
        }
        return bestHeuristic;
    }

    public boolean ifReset(int[][] board) {
        int minHeuristic = 8;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] < minHeuristic) {
                    minHeuristic = board[i][j];
                }
            }
        }

        if(this.neighborStates == 0) {
            return true;
        }

        return false;
    }

    public String toString(int[][] board) {
        String s = "Current State: \r\n";

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                s += " " + board[i][j];
            }
            s += "\r\n";
        }

        return s;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }
    
    public int getHeuristic() {
        return this.heuristic;
    }
    
    public int getNeighborStates() {
        return this.neighborStates;
    }

    public int[][] getBoard() {
        this.boardCopy = copyBoard(this.board, this.boardCopy);
        return this.boardCopy;
    }
    
}
