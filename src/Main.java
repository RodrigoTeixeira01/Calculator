import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.FontMetrics;

import javax.swing.JWindow;

/**
 * A calculator application<br>
 * It can be initialized by calling the constructor (new Main)<br>
 * Or by running the classic public static void main (String[])<br><br>
 * Button organization based on windows default calculator
 * @author Rodrigo Teixeira
 * @see Mode
 */
public class Main extends JWindow implements MouseListener {
	private static final long serialVersionUID = 1L; // ECLIPSE IS BORRING
	private static final int CELL_COUNT_X = 4; // AMOUNT OF HORISONTAL CELLS DO NOT CHANGE
	private static final int CELL_COUNT_Y = 5; // AMOUNT OF VERTICAL CELLS DO NOT CHANGE
	private static final int WINDOW_WIDTH = 400; // AUTO DESCRIPTIVE
	private static final int CELL_SIZE = WINDOW_WIDTH/CELL_COUNT_X; // SIZE (WIDTH) OF EACH CELL
	private static final int WINDOW_HEIGHT = WINDOW_WIDTH + CELL_SIZE<<1; // WINDOW HEIGHT
	private static final Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24); // FONT USED TO WRITE TEXT. FEEL ABLE TO CHANGE
	private FontMetrics FONT_METRICS; // CACHE FONT METRICS
	private static boolean FIRST_PAINT = true; // VARIABLE WITH RENDER EFFICIENCY REASONS
	
	double value = 0; // ACTUAL VALUE
	double old = 0; // LAST VALUE BEFORE THIS ONE
	Mode mode = Mode.NONE; // ACTUAL MODE (SUM, SUB, MUL, etc...)
	boolean comma = false; // STORES IF THE USER HAS PREESED THE COMMA BUTTON
	int digits = 0; // STORES HOW MANY DIGITS THERE ARE AFTER THE COMMA
	// GRID WITH THE TEXT IN EACH CELL
	String[][] textGrid = {{"","1/x","7","4","1","+/-"},{"","^","8","5","2","0"},{"","sqrt","9","6","3","."},{"","/","*","-","+","="}}; // CONGRATS YOU SCROLLED THE PAGE
	
	/**
	 * IT'S LITERRALY JUST THE CONSTRUCTOR
	 */
	public Main() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // SETS THE SIZE OF THE WINDOW
		setLocationRelativeTo(null); // CENTRALIZES THE WINDOW
		addMouseListener(this); // ADDS THE MOUSE LISTENERS
		setVisible(true); // MAKES THE WINDOWS VISIBLE
	}

	/**
	 * PUBLIC STATIC VOID MAIN<br>
	 * JUST CALLS THE CONSTRUCTOR
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();

	}
	
	/**
	 * RENDER FUNCTION<br>
	 * CALLED ALWAYS THAT THE CODE REFERS "repaint()"
	 * 
	 * REQUIRES AN ARGUMENT g OF THE TYPE java.awt.Graphics
	 * Graphics IS AN ABSTRACT CLASS
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
		// CLEAR PREVIOUS RESULT
		g.clearRect(0, 0, WINDOW_WIDTH, CELL_SIZE);
		// I DONT THINK I HAVE TO EXPLAIN THIS ONE
		g.setColor(Color.BLACK);
		// CLOSE BUTTON
		g.drawRect(0, 0, 20, 20);
		g.drawLine(0, 0, 20, 20);
		g.drawLine(0, 20, 20, 0);
		// WRITE RESULT
		g.setFont(FONT);
		try{drawString(""+value, WINDOW_WIDTH>>>1, CELL_SIZE>>>1, g);}
		catch(NullPointerException npe) {FONT_METRICS=g.getFontMetrics(FONT);}
		// DONT DRAW BUTTONS IF ALVERY DRAWNED IT
		if(!FIRST_PAINT) {
			return;
		}
		// DRAW BUTTONS
		for(int i=1; i<=CELL_COUNT_Y; i++) {
			g.drawLine(i*CELL_SIZE, CELL_SIZE, i*CELL_SIZE, WINDOW_HEIGHT);
			g.drawLine(0, i*CELL_SIZE, WINDOW_WIDTH, i*CELL_SIZE);
		}
		// WRITE VALUES INSIDE BUTTONS
		for(int x=0; x<CELL_COUNT_X; x++) {
			for(int y=0; y<=CELL_COUNT_Y; y++) {
				drawString(textGrid[x][y], (int) ((x+0.5d)*CELL_SIZE), (int) ((y+0.5d)*CELL_SIZE),g);
			}
		}
		// END OF FIRST PAINT
		FIRST_PAINT = false;
	}

	/**
	 * HELPER FUNCTION TO DRAW THE STRING [string] IN CENTER ALIGN MODE AT POSITION [x] [y] IN THE GRAPHICS [g]
	 * @param string
	 * @param x
	 * @param y
	 * @param g
	 */
	private void drawString(String string, int x, int y, Graphics g) {
		// DRAW THE STRING
		g.drawString(string, x - (FONT_METRICS.stringWidth(string)>>>1),y + (FONT_METRICS.getHeight()>>>2));
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	
	@Override
	public void mousePressed(MouseEvent e) {
		// VERIFY CLOSE BUTTON
		if(e.getX() < 20 && e.getY() < 20) {
			System.exit(0);
		}
		// GET X AND Y AT GRID
		int x = e.getX()/CELL_SIZE;
		int y = e.getY()/CELL_SIZE;
		if(y==0) {
			//IF Y IS ZERO (CLICK IN THE RESULT AREA) CLEAR THE RESULT AND OLD RESULT
			value = 0; old = 0;comma = false; digits = 0;
		}else if(x==0) {
			if(y==1) {
				// 1/x FUNCTIONALITY
				value=(double)1/value;
			}else if(y==2) {
				// 7 BUTTON
				enter(7);
			}else if(y==3) {
				// 4 BUTTON
				enter(4);
			}else if(y==4) {
				// 1 BUTTON
				enter(1);
			}else if(y==5) {
				// +/- FUNCTIONALITY
				value=-value;
			}
		}else if(x==1) {
			if(y==1) {
				// EXPONENTIAL / POWER BUTTON "^"
				setMode(Mode.EXP);
			}else if(y==2) {
				// 8 BUTTON
				enter(8);
			}else if(y==3) {
				// 5 BUTTON
				enter(5);
			}else if(y==4) {
				// 2 BUTTON
				enter(2);
			}else if(y==5) {
				// 0 BUTTON
				enter(0);
			}
		}else if(x==2) {
			if(y==1) {
				// SQRT (SQUARE ROOT) OPTION
				value = Math.sqrt(value);
			}else if(y==2) {
				// 9 BUTTON
				enter(9);
			}else if(y==3) {
				// 6 BUTTON
				enter(6);
			}else if(y==4) {
				// 3 BUTTON
				enter(3);
			}else if(y==5 && !comma) {
				// IF THE USER HASN'T ALREADY PRESSED COMMA ACTIVATE COMMA
				comma = true;
				digits = 0;
			}
		}else if(x==3) {
			if(y==1) {
				// DIVISION BUTTON "/"
				setMode(Mode.DIV);
			}else if(y==2) {
				// MULTIPLICATION BUTTON "*"
				setMode(Mode.MUL);
			}else if(y==3) {
				// SUBTRACTION BUTTON "-"
				setMode(Mode.SUB);
			}else if(y==4) {
				// ADDITION / SUM BUTTON "+"
				setMode(Mode.SUM);
			}else if(y==5) {
				// EQUALS / EVALUATE BUTTON "="
				eval();
			}
		}
		repaint();
	}

	/**
	 * HELPER FUNCTION TO SET MODE TO A SPECIFIC MODE
	 * @param mode
	 */
	private void setMode(Mode mode) {
		this.mode = mode;
		comma = false;
		digits = 0;
		old = value;
		value = 0;
		
	}

	/**
	 * HELPER FUNCTION FOR THE EQUALS "=" BUTTON
	 */
	private void eval() {
		switch(mode) {
			case NONE: // NO MODE
				break;
			case DIV: // DIVision
				value = old / value;
				old = 0;
				mode = Mode.NONE;
				break;
			case EXP: // EXPonentiation
				value = Math.pow(old, value);
				old = 0;
				mode = Mode.NONE;
				break;
			case MUL: // MULtiplication
				value = old * value;
				old = 0;
				mode = Mode.NONE;
				break;
			case SUB: // SUBtraction
				value = old - value;
				old = 0;
				mode = Mode.NONE;
				break;
			case SUM: // SUM / ADDTION
				value = old + value;
				old = 0;
				mode = Mode.NONE;
				break;
			default: // I THINK YOUR NEVER GONNA GET HERE HAHAHA
				throw new RuntimeException("Hmm... SOMETHING's MISSING...");
		}
		
	}

	/**
	 * HELPER FUNCTION TO ENTER NUMBERS FROM NUMBER BUTTONS
	 * ENTERS THE NUMBER [i] TO THE RESULT
	 * MAY HAVE BUGS IF THE RESULT IS FROM A SQRT, 1/x, DIV OR EXP 
	 * @param i
	 */
	private void enter(int i) {
		if(comma) {
			value += i * Math.pow(0.1d, ++digits);
		}else {
			value = value * 10 + i;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {
		//System.exit(0); // I GUESS YOU CAN UN-COMMENT THIS LINE...
	}

}
