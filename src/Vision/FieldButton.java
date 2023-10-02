package Vision;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import Camp.Field;
import Camp.FieldEvent;

@SuppressWarnings("serial")
public class FieldButton extends JButton 
    implements BiConsumer<Field, FieldEvent>, MouseInputListener { 

    private final Color DEFAULT_BG = new Color(184, 184, 184);
    private final Color OPEN_BG = new Color(8, 179, 247);
    private final Color EXPLODE_BG = new Color(189, 66, 68);
    private final Color GREEN_BG = new Color(0, 100, 0);

    private Field field;
    
    public FieldButton(Field field) {
        this.field = field;
        setBorder(BorderFactory.createBevelBorder(0));
        setBackground(DEFAULT_BG);
        setOpaque(true);
        addMouseListener(this);
        setPreferredSize(new Dimension(30, 30)); // Set both width and height to 30 (or any desired size)
        field.registerObserver(this);
    }
    
    @Override
    public void accept(Field field, FieldEvent event) {
        switch(event) {
            case OPEN:
                applyStyle();
                break;
            case MARK:
                markStyle();
                break;
            case EXPLODE:
                explodeStyle();
                break;
            default:
                defaultStyle();
        }
    }

    private void defaultStyle() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setBackground(DEFAULT_BG);
        setText("");
    }

    private void explodeStyle() {
    	applyStyle();
    }

    private void markStyle() {
        setBackground(GREEN_BG);
        setText("M");
    }

    private void applyStyle() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if(field.isMined()) {
            setBackground(EXPLODE_BG);
            setText("\uD83D\uDCA3"); 
            setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
         // Create a timer for the flashing effect
            Timer timer = new Timer(500, new ActionListener() {
                private boolean isDefaultColor = true;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isDefaultColor) {
                        setBackground(Color.RED); // Change to a different color
                    } else {
                        setBackground(EXPLODE_BG); // Change back to the default explode color
                    }
                    isDefaultColor = !isDefaultColor;
                }
            });

            timer.start();

            // Stop the timer after a certain number of flashes (e.g., 5 times)
            Timer stopTimer = new Timer(500 * 5, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timer.stop();
                }
            });
            stopTimer.setRepeats(false);
            stopTimer.start();
            return;
        }
        
        setBackground(OPEN_BG);
        
        switch((int) field.minesInNeighborhood()) {
            case 1:
                setForeground(GREEN_BG);
                break;
            case 2: 
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default: 
                setForeground(Color.PINK);
                break;
        }
        
        String value = !field.isSafeNeighborhood() ? field.minesInNeighborhood() + "" : ""; 
        setText(value);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == 1) {
            field.open();
        } else {
            field.toggleMark();
        }
    }

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
