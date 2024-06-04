import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;

@SuppressWarnings("serial")
public class Square extends JComponent {
    private Board b; // Reference to the board this square belongs to

    private final int color; // Color of the square (0 for black, 1 for white)
    private Piece occupyingPiece;// Piece currently occupying this square (null if empty)
    private boolean dispPiece;// Flag to determine if the piece should be displayed

    private int xNum;
    private int yNum;

    public Square(Board b, int c, int xNum, int yNum) {

        this.b = b;
        this.color = c;
        this.dispPiece = true;// Initially set to true, pieces will be displayed
        this.xNum = xNum;
        this.yNum = yNum;


        this.setBorder(BorderFactory.createEmptyBorder());// Removes border from the square
        //BorderFactory: A utility class in Swing that provides factory methods for creating borders.
        //createEmptyBorder(): This method creates a border with no space around the edges of the component (essentially an empty border).
        //setBorder(): This method sets the border of the current component (this) to the one provided.
    }
    // Getter methods
    public int getColor() {
        return this.color;
    }

    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }

    public boolean isOccupied() {
        return (this.occupyingPiece != null);
    }

    public int getXNum() {
        return this.xNum;
    }

    public int getYNum() {
        return this.yNum;
    }
    // Setter method to toggle display of occupying piece
    public void setDisplay(boolean v) {
        this.dispPiece = v;
    }
    // Place a piece on this square
    public void put(Piece p) {
        this.occupyingPiece = p;
        p.setPosition(this); // Set the piece's position to this square
    }
    // Remove and return the occupying piece
    public Piece removePiece() {
        Piece p = this.occupyingPiece;
        this.occupyingPiece = null;
        return p;
    }

    // Capture a piece (replace with a new piece)
    public void capture(Piece p) {
        Piece k = getOccupyingPiece();
        if (k.getColor() == 0) b.Bpieces.remove(k);
        if (k.getColor() == 1) b.Wpieces.remove(k);
        this.occupyingPiece = p;
    }
    // Paint the square component
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.color == 1) {
            g.setColor(new Color(221,192,127)); // Light color
        } else {
            g.setColor(new Color(101,67,33));  // Dark color
        }
// Fill the square with the determined color
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        // Draw the occupying piece if present and should be displayed
        if(occupyingPiece != null && dispPiece) {
            occupyingPiece.draw(g);// Utilizes the draw method of the Piece class
        }
    }
    // Hash code method to generate unique hash code for each square
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + xNum;
        result = prime * result + yNum;
        return result;
    }

}