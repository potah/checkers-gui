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
            } else if (selectedPiece != null) {
                // Attempt to move the selected piece
            }
        }
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
