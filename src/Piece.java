

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public abstract class Piece {
    private final int color;
    private Square currentSquare;
    private BufferedImage img;// The image representing the piece
    //initSq The initial position of the piece.
    public Piece(int color, Square initSq, String img_file) {
        this.color = color;
        this.currentSquare = initSq;
        
        try {
            if (this.img == null) {
              this.img = ImageIO.read(getClass().getResource(img_file));
            }
          } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
          }
    }
    /**
     * Moves the piece to the specified square if the move is legal.
     * @param fin The target square to move the piece to.
     * @return True if the move is successful, false otherwise.
     */
    public boolean move(Square fin) {
        Piece occup = fin.getOccupyingPiece();
        // Check if the target square is occupied by a piece of the same color
        if (occup != null) {
            if (occup.getColor() == this.color) return false;
            else fin.capture(this);// Capture the opponent's piece
        }
        
        currentSquare.removePiece();// Remove the piece from its current square
        this.currentSquare = fin;// Update the piece's position
        currentSquare.put(this);// Place the piece on the new square
        return true;
    }
    
    public Square getPosition() {
        return currentSquare;
    }
    /**
     * Sets the current position of the piece.
     * @param sq The new square to set as the piece's position.
     */
    public void setPosition(Square sq) {
        this.currentSquare = sq;
    }
    
    public int getColor() {
        return color;
    }
    
    public Image getImage() {
        return img;
    }
    /**
     * Draws the piece on the board at its current position.
     * @param g The graphics context to use for drawing.
     */
    public void draw(Graphics g) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();
        
        g.drawImage(this.img, x, y, null);
    }

    // calculates legal moves in horizontal and vertical directions
    //* @return An array containing the limits of the piece's movement in each direction.
    public int[] getLinearOccupations(Square[][] board, int x, int y) {
        int lastYabove = 0; // The last y-coordinate the piece can move to above its current position
        int lastXright = 7; // The last x-coordinate the piece can move to to the right of its current position
        int lastYbelow = 7; // The last y-coordinate the piece can move to below its current position
        int lastXleft = 0; // The last x-coordinate the piece can move to to the left of its current position

        // Check vertical movement upwards
        for (int i = 0; i < y; i++) {
            if (board[i][x].isOccupied()) {
                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
                    lastYabove = i;
                } else lastYabove = i + 1;
            }
        }
// Check vertical movement downwards
        for (int i = 7; i > y; i--) {
            if (board[i][x].isOccupied()) {
                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
                    lastYbelow = i;
                } else lastYbelow = i - 1;
            }
        }
// Check horizontal movement to the left
        for (int i = 0; i < x; i++) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
                    lastXleft = i;
                } else lastXleft = i + 1;
            }
        }
// Check horizontal movement to the right
        for (int i = 7; i > x; i--) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
                    lastXright = i;
                } else lastXright = i - 1;
            }
        }
        
        int[] occups = {lastYabove, lastYbelow, lastXleft, lastXright};
        
        return occups;
    }
    // @return A list of squares representing the legal moves in diagonal directions.
    public List<Square> getDiagonalOccupations(Square[][] board, int x, int y) {
        // calculates legal moves in diagonal directions
        LinkedList<Square> diagOccup = new LinkedList<Square>();

        int xNW = x - 1; // x-coordinate for northwest movement
        int xSW = x - 1; // x-coordinate for southwest movement
        int xNE = x + 1; // x-coordinate for northeast movement
        int xSE = x + 1; // x-coordinate for southeast movement
        int yNW = y - 1; // y-coordinate for northwest movement
        int ySW = y + 1; // y-coordinate for southwest movement
        int yNE = y - 1; // y-coordinate for northeast movement
        int ySE = y + 1; // y-coordinate for southeast movement
        // Check northwest diagonal movement
        while (xNW >= 0 && yNW >= 0) {
            if (board[yNW][xNW].isOccupied()) {
                if (board[yNW][xNW].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[yNW][xNW]);
                    break;
                }
            } else {
                diagOccup.add(board[yNW][xNW]);
                yNW--;
                xNW--;
            }
        }
        // Check southwest diagonal movement
        while (xSW >= 0 && ySW < 8) {
            if (board[ySW][xSW].isOccupied()) {
                if (board[ySW][xSW].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[ySW][xSW]);
                    break;
                }
            } else {
                diagOccup.add(board[ySW][xSW]);
                ySW++;
                xSW--;
            }
        }
        // Check southeast diagonal movement
        while (xSE < 8 && ySE < 8) {
            if (board[ySE][xSE].isOccupied()) {
                if (board[ySE][xSE].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[ySE][xSE]);
                    break;
                }
            } else {
                diagOccup.add(board[ySE][xSE]);
                ySE++;
                xSE++;
            }
        }
        // Check northeast diagonal movement
        while (xNE < 8 && yNE >= 0) {
            if (board[yNE][xNE].isOccupied()) {
                if (board[yNE][xNE].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[yNE][xNE]);
                    break;
                }
            } else {
                diagOccup.add(board[yNE][xNE]);
                yNE--;
                xNE++;
            }
        }
        
        return diagOccup;
    }
    
    // No implementation, to be implemented by each subclass
    /**
     * Abstract method to be implemented by each specific piece subclass.
     * Determines the legal moves for the piece based on its specific movement rules.
     * @param b The chess board.
     * @return A list of squares representing the legal moves for the piece.
     */
    public abstract List<Square> getLegalMoves(Board b);
}