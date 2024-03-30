package Checkers;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 0;
    public static final int BOARD_WIDTH = 8;
    public static final int[] BLACK_RGB = {181, 136, 99};
    public static final int[] WHITE_RGB = {240, 217, 181};
    public static final float[][][] coloursRGB = new float[][][] {
        //default - white & black
        {
                {WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]},
                {BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]}
        },
        //green
        {
                {105, 138, 76}, //when on white cell
                {105, 138, 76} //when on black cell
        },
        //blue
        {
                {196,224,232},
                {170,210,221}
        }
	};

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    public static final int FPS = 60;

    private Piece[][] board = new Piece[BOARD_WIDTH][BOARD_WIDTH];

    public boolean white_move = true;

    public App() {
        
    }

    /**
     * Initialise the setting of the window size.
    */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    private void initializeBoard() {
        for (int y = 0; y < BOARD_WIDTH; y++) {
            for (int x = 0; x < BOARD_WIDTH; x += 2) {
                if (y < 3) {
                    if ((y % 2 == 0)) {
                        board[x][y] = new Piece(true);
                    } else {
                        board[x + 1][y] = new Piece(true);
                    }
                } else if (y > 4) {
                    if ((y % 2 == 0)) {
                        board[x][y] = new Piece(false);
                    } else {
                        board[x + 1][y] = new Piece(false);
                    }
                }
            }
        }
    }
    @Override
    public void setup() {
        frameRate(FPS);

		//Set up the data structures used for storing data in the game
        initializeBoard();
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
	@Override
    public void keyPressed(){

    }
    
    /**
     * Receive key released signal from the keyboard.
    */
	@Override
    public void keyReleased(){

    }

    Piece selectedPiece = null;
    int selectedX = -1, selectedY = -1;

    @Override
    public void mousePressed(MouseEvent e) {
        //Check if the user clicked on a piece which is theirs - make sure only whoever's current turn it is, can click on pieces
		
		//TODO: Check if user clicked on an available move - move the selected piece there. 
		//TODO: Remove captured pieces from the board
		//TODO: Check if piece should be promoted and promote it
		//TODO: Then it's the other player's turn.

        int x = mouseX / CELLSIZE;
        int y = mouseY / CELLSIZE;

        // Check if the selected cell is within bounds
        if (x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_WIDTH) {
            if (selectedPiece == null && board[x][y] != null) {
                // Select the piece
                // check to see if we're selecting our colour
                Piece possiblePiece = board[x][y];
                if ((white_move && possiblePiece.isBlack) {
                    // shit pants
                    selectedX = -1;
                    selectedY = -1;
                    // do we want to output some kind of error here?
                } else {
                    selectedPiece = possiblePiece;
                    selectedX = x;
                    selectedY = y;
                }
            } else if (selectedPiece != null) {
                // check if we've re-selected another piece of the same colour first

                // Attempt to move the selected piece
                // we want to move to where we have selected.
                if (board[x][y] == null) {
                    processMove(x, y);
                    white_move = !white_move;
                    selectedPiece = null;
                    selectedX = -1;
                    selectedY = -1;
                }
            }
        }
    }

    private void processMove(int toRow, int toCol) {
        board[selectedX][selectedY] = null;
        board[toRow][toCol] = selectedPiece;

        for (int i = 0; i < 8; i++) {
            if (board[0][i].isBlack) {
                board[0][i].isKing = true;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (!board[7][i].isBlack) {
                board[7][i].isKing = true;
            }
        }
    }
    private void highlightValidMoves(int fromRow, int fromCol) {
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_WIDTH; y++) {
                if (isValidMove(fromRow, fromCol, x, y)) {
                    // makey bluey

                }
            }
        }

    }

    /**
     * Checks if a move is valid.
     *
     * @param fromRow the starting row of the move.
     * @param fromCol the starting column of the move.
     * @param toRow   the ending row of the move.
     * @param toCol   the ending column of the move.
     * @return true if the move is legal, false otherwise.
     */
    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Implement this method to check if a move is legal according to the rules of
        // Checkers.
        // check that from and to values are within bounds
//        if (fromRow < 0 ||
//                fromRow > 7 ||
//                fromCol < 0 ||
//                fromCol > 7 ||
//                toRow < 0 ||
//                toRow > 7 ||
//                toCol < 0 ||
//                toCol > 7) {
//            return false;
//        }

        // check that the destination is empty
        if (board[toRow][toCol] != null) {
            return false;
        }
        // can't be same row or col
        if (fromRow == toRow || fromCol == toCol) {
            return false;
        }

        // check that we have a valid piece w/b/W/B
        Piece piece = board[fromRow][fromCol];
        if (piece == null) {
            return false;
        }

        // check correct turn b should be mod 2 = 0
        if (piece.isBlack && (moves % 2) != 0) {
            return false;
        }

        // w can only go to smaller cols, b can only go to bigger cols
        // W or B can go forward or back
        if (piece == 'w' && toRow < fromRow) {
            return false;
        }
        if (piece == 'b' && toRow > fromRow) {
            return false;
        }

        // check that we are one different
        if (Math.abs(fromRow - toRow) > 2 || Math.abs(fromCol - toCol) > 2) {
            return false;
        }

        // check to see if the col/row diff is 2 then we must not have a piece jumped
        if (Math.abs(fromRow - toRow) == 2) {
            if (Math.abs(fromCol - toCol) != 2) {
                // if one is 2 diff then so must the other
                return false;
            }

            int middleRow = fromRow + 1;
            int middleCol = fromCol + 1;
            // check to see if we have a piece jumped
            if (toRow < fromRow) {
                middleRow = toRow + 1;
            }
            if (toCol < fromCol) {
                middleCol = toCol + 1;
            }
            // check for piece
            if (board[middleRow][middleCol] == ' ') {
                return false;
            }
        }

        return true;
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
	@Override
    public void draw() {
        this.noStroke();
        background(180);
		//draw the board
        drawBoard();
		
		//draw highlighted cells
        if (selectedPiece != null) {
            highlightValidMoves(selectedX, selectedY);
        }
        
		//check if the any player has no more pieces. The winner is the player who still has pieces remaining

    }

    private void drawBoard() {
        for (int y = 0; y < BOARD_WIDTH; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                setFill((x + y) % 2, 0);
                rect(x * CELLSIZE, y * CELLSIZE, CELLSIZE, CELLSIZE);
                drawPiece(x, y);
            }
        }
    }

    private void drawPiece(int x, int y) {
        if (board[x][y] != null) {
            // Draw the ring with a different color
            if (board[x][y].isBlack) {
                fill(255);
            } else {
                fill(0);
            }
            ellipse((x + 0.5f) * CELLSIZE, (y + 0.5f) * CELLSIZE, CELLSIZE * 0.9f, CELLSIZE * 0.9f);


            // Draw the original ellipse representing the piece
            if (board[x][y].isBlack) {
                fill(0);
            } else {
                fill(255);
            }
            ellipse((x + 0.5f) * CELLSIZE, (y + 0.5f) * CELLSIZE, CELLSIZE * 0.7f, CELLSIZE * 0.7f);

            if(board[x][y].isBlack() && board[x][y].isKing()){
                fill(255);
            } else if(!board[x][y].isBlack() && board[x][y].isKing()){
                fill(0);
            }
            ellipse((x + 0.5f) * CELLSIZE, (y + 0.5f) * CELLSIZE, CELLSIZE * 0.5f, CELLSIZE * 0.5f);

            if(board[x][y].isBlack() && board[x][y].isKing()){
                fill(255);
            } else if(!board[x][y].isBlack() && board[x][y].isKing()){
                fill(0);
            }
            ellipse((x + 0.5f) * CELLSIZE, (y + 0.5f) * CELLSIZE, CELLSIZE * 0.3f, CELLSIZE * 0.3f);
        }
    }

	/**
     * Set fill colour for cell background
     * @param colourCode The colour to set
     * @param blackOrWhite Depending on if 0 (white) or 1 (black) then the cell may have different shades
     */
	public void setFill(int colourCode, int blackOrWhite) {
		this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1], coloursRGB[colourCode][blackOrWhite][2]);
	}

    public static void main(String[] args) {
        PApplet.main("Checkers.App");
    }


}
