package Vision;

import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import Camp.Board;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

    public BoardPanel(Board board) {
        // Set the layout of the panel to a grid layout based on the board's rows and columns
        setLayout(new GridLayout(board.getRows(), board.getColumns()));
        
        // For each field on the board, add a button to represent that field
        board.forEachField(field -> add(new FieldButton(field)));
    
        // Register an observer to the board to handle game win/lose events
        board.registerObserver(result -> {
            // Use SwingUtilities.invokeLater to ensure the UI updates run on the Swing event dispatch thread
            SwingUtilities.invokeLater(() -> {
                if(result) {
                    JOptionPane.showMessageDialog(this, "You won!");
                } else {
                    JOptionPane.showMessageDialog(this, "You lost!");
                }
                board.restart();
            });
        });
    }
}
