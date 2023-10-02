package Vision;


import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Camp.Board;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

    public MainWindow() {
        
    	// Get the number of lines, columns, and mines from the user
        int lines = getInput("Enter the number of lines:", 16); // Default to 16
        int columns = getInput("Enter the number of columns:", 30); // Default to 30
        int mines = getInput("Enter the number of mines:", 50); // Default to 50
    	
        // Create a new game board with specified dimensions and number of mines
        Board board = new Board(lines, columns, mines);
        
        // Add the game board panel to the main window
        add(new BoardPanel(board));
        
        // Set properties for the main window
        setTitle("Minesweeper");
        setSize(690, 480);
        setLocationRelativeTo(null); // Center the window on the screen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Close the window when the "X" button is clicked
        setVisible(true); // Make the window visible
    }
    
    public static void main(String[] args) {
        // Create and display the main window when the program is run
        new MainWindow();
    }
    
    private int getInput(String message, int defaultValue) {
        String result = JOptionPane.showInputDialog(this, message, defaultValue);
        if (result == null || result.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
