// Chess (Board)
// Written By Ethan Rowan
// March 2018
/*
 * RULES TO IMPLEMENT:
 * 	- When a pawn reaches the other side it can become any piece.
 *  - Restrict the king from moving into other pieces' future moves.
 *  	- Implement move insight algorithm into showMoves()
 *  	- Implement move insight algorithm into mouseReleased()
 *  - Castling
 *  - En Pessant
 */
package game;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import game.pieces.Pawn;

public class Board extends JFrame
	implements ActionListener, MouseListener
{
	//Constant. Stores the size of each square (cell) on the board.
	static int CELL_SIZE = 100;
	//Stores whether the black or white kings are in check.
	static boolean blackCheck, whiteCheck;
	//Stores all of the pieces on the board. No order necessary.s
	public static ArrayList<Piece> pieces;
	//(turn) stores the current turn in the game.
	//(win) stores the color of the winning side (or null).
	Color turn, win;
	//(selected) stores the currently selected piece. This will display its moves.
	//(pressed) stores the piece currently pressed by the mouse. This will display an outline.
	//(choosingPiece) stores the piece waiting to be reassigned to a new piece. This piece was
	//previously a pawn, then reached the opposing side.
	Piece selected, pressed, choosingPiece;
	//Graphics buffers for backend drawing to the screen.
	Graphics buffG, buffG2;
	//Image buffers for backend drawing to the screen with buffG and buffG2.
	BufferedImage buffimg, buffimg2;
	//Stores the pieces that have been captured. These are displayed at the
	//top and bottom of the game board.
	Piece[] outBlackPieces;
	Piece[] outWhitePieces;
	//Drawing timer
	Timer timer;
	
	private JPanel contentPane;

	
	public Board()
	{
		initUI();
		
		turn = Color.WHITE;
		pieces = new ArrayList<Piece>();
		createPieces(pieces, Color.BLACK);
		createPieces(pieces, Color.WHITE);
		
		outBlackPieces = new Piece[16];
		outWhitePieces = new Piece[16];
		
		//Create new buffer images capturing the entire window
		buffimg = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);
		buffG = buffimg.getGraphics();
		buffimg2 = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);
		buffG2 = buffimg2.getGraphics();
		
		timer = new Timer(20, this);
		timer.start();
	}
	
	@Override
	public void paint(Graphics g)
	{
		//DInitial background
		buffG2.setColor(Color.BLACK);
		buffG2.fillRect(0, 0, getWidth(), getHeight());
		
		//Overlayed background
		buffG.setColor(Color.BLACK);
		buffG.fillRect(0, 0, getWidth(), getHeight());
		
		drawGameBoard(buffG);
		
		drawOutlines(buffG);
		
		drawPieces(buffG);
		
		drawOutPieces(buffG, buffG2);
		
		drawWinScreen(buffG);
		
		//Draw the buffers to the actual screen
		buffG2.drawImage(buffimg, 61, 95, this);
		g.drawImage(buffimg2, 0, 0, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		checkWin();
		
		checkCheck(Color.BLACK);
		checkCheck(Color.WHITE);
		
		checkPawnAtOtherSide();
		
		repaint();
	}
	
	public void drawGameBoard(Graphics g)
	{
		//Initial background
		//Also acts as the black cells
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//Draw the white cells in a checkerboard pattern
		g.setColor(Color.WHITE);
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x+=2)
			{
				if (y % 2 == 0)
					g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
				else
					g.fillRect(x * CELL_SIZE + CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}
		for (int y = 0; y < 8; y++)
			for (int x = 0; x < 8; x++)
				g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
	}
	
	public void drawPieces(Graphics g)
	{
		for (Piece piece : pieces)
			g.drawImage(piece.image, piece.x, piece.y, CELL_SIZE, CELL_SIZE, this);
	}
	
	//Draw all of the pieces that have been eliminated
	//(found in "outBlackPieces" and "outWhitePieces")
	public void drawOutPieces(Graphics g, Graphics g2)
	{
		for (Piece piece : outBlackPieces)
			if (piece != null)
				g2.drawImage(piece.image, 61 - piece.x, 95 - piece.y, 50, 50, this);
		
		for (Piece piece : outWhitePieces)
			if (piece != null)
				g.drawImage(piece.image, piece.x, piece.y, 50, 50, this);
	}
	
	public void drawWinScreen(Graphics g)
	{
		String winner = "";
		
		if (win == Color.BLACK)
			winner = "BLACK";
		else if (win == Color.WHITE)
			winner = "WHITE";
		
		//Draw an overlayed win message to the screen.
		//The message is drawn multiple times in different
		//colors to make it stand out on the contrasting game board
		if (win != null)
		{
			g.setColor(Color.BLACK);
			g.setFont(new Font("Tahoma", Font.BOLD, 102));
			FontMetrics fm = g.getFontMetrics(new Font("Tahoma", Font.BOLD, 102));
			g.drawString(winner + " wins!",
					((getWidth() - fm.stringWidth(winner + " wins!"))/2) - 61,
					(getHeight()/2) - fm.getHeight());
			g.setColor(Color.BLACK);
			g.setFont(new Font("Tahoma", Font.BOLD, 99));
			fm = g.getFontMetrics(new Font("Tahoma", Font.BOLD, 98));
			g.drawString(winner + " wins!",
					((getWidth() - fm.stringWidth(winner + " wins!"))/2) - 61,
					(getHeight()/2) - fm.getHeight());
			g.setColor(Color.RED);
			g.setFont(new Font("Tahoma", Font.BOLD, 100));
			fm = g.getFontMetrics(new Font("Tahoma", Font.BOLD, 100));
			g.drawString(winner + " wins!",
					((getWidth() - fm.stringWidth(winner + " wins!"))/2) - 61,
					(getHeight()/2) - fm.getHeight());
		}
	}
	
	//Display the moves for the piece that is currently selected by the user
	public void drawOutlines(Graphics g)
	{
		if (selected != null)
		{
			if ((selected.color == Color.BLACK && !blackCheck || (blackCheck && selected.type == Pieces.KING)) ||
					(selected.color == Color.WHITE && !whiteCheck || (whiteCheck && selected.type == Pieces.KING)))
				showMoves(selected, g);
		}
		if (pressed != null)
			pressed.showOutline(g);
	}
	
	public void checkWin()
	{	
		int numBlack = 0, numWhite = 0;
		boolean blackKingExists = false, whiteKingExists = false;
		for (Piece piece : pieces)
		{
			if (piece.color == Color.BLACK)
				numBlack++;
			else if (piece.color == Color.WHITE)
				numWhite++;
			
			if (piece.type == Pieces.KING &&
					piece.color == Color.BLACK)
				blackKingExists = true;
			else if (piece.type == Pieces.KING &&
					piece.color == Color.WHITE)
				whiteKingExists = true;
		}
		
		if (checkCheckmate(Color.BLACK) || numBlack == 0 ||
				!blackKingExists)
		{
			if (win == null)
				showMessage("Checkmate. WHITE wins.", "Checkmate");
			win = Color.WHITE;
		}
		else if (checkCheckmate(Color.WHITE) || numWhite == 0 ||
				!whiteKingExists)
		{
			if (win == null)
				showMessage("Checkmate. BLACK wins.", "Checkmate");
			win = Color.BLACK;
		}
	}
	
	//Add all of the game pieces to the specified list
	//and place them in the correct locations
	public void createPieces(ArrayList<Piece> list, Color color)
	{
		int home_row_height = 0, pawn_row_height = 0;
		if (color == Color.BLACK)
		{
			home_row_height = 7;
			pawn_row_height = 6;
		}
		else if (color == Color.WHITE)
		{
			home_row_height = 0;
			pawn_row_height = 1;
		}
		list.add(new Piece(Pieces.KING, color, 3, home_row_height));
		list.add(new Piece(Pieces.QUEEN, color, 4, home_row_height));
		list.add(new Piece(Pieces.BISHOP, color, 2, home_row_height));
		list.add(new Piece(Pieces.BISHOP, color, 5, home_row_height));
		list.add(new Piece(Pieces.KNIGHT, color, 1, home_row_height));
		list.add(new Piece(Pieces.KNIGHT, color, 6, home_row_height));
		list.add(new Piece(Pieces.ROOK, color, 0, home_row_height));
		list.add(new Piece(Pieces.ROOK, color, 7, home_row_height));
		for (int i = 0; i < 8; i++)
			list.add(new Piece(Pieces.PAWN, color, i, pawn_row_height));
	}
	
	//Returns a VALID moves array based on the implemented rules
	public int[][] getValidMoves(Piece piece, int[][] moves)
	{
		for (int i = 0; i < moves.length; i++)
		{
			if (moves[i] != null)
			{
				int x = moves[i][0];
				int y = moves[i][1];
				for (int j = 0; j < pieces.size(); j++)
				{
					//A move cannot conflict with a piece of the same color
					if (pieces.get(j).color == piece.color &&
							getX(x) == pieces.get(j).x && getY(y) == pieces.get(j).y)
					{
						moves[i] = null;
					}
					//A move cannot be out of bounds
					else if (x < 0 || x > 7 ||
							y < 0 || y > 7)
					{
						moves[i] = null;
					}
				}
			}
		}
		return moves;
	}
	
	public void showMoves(Piece piece, Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(15));
		g2.setColor(Color.BLUE);
		
		int[][] validmoves = getValidMoves(piece, piece.getMoves());
		ArrayList<int[]> otherMoves = new ArrayList<int[]>();
		for (int i = 0; i < pieces.size(); i++)
		{
			Piece otherpiece = pieces.get(i);
			if (piece.color == Color.WHITE && otherpiece.color == Color.BLACK)
			{
				int[][] moves = null;
				if (otherpiece.type == Pieces.PAWN)
					moves = Pawn.getAllPawnMoves(otherpiece);
				else
					moves = getValidMoves(otherpiece, otherpiece.getMoves());
				for (int j = 0; j < moves.length; j++)
					otherMoves.add(moves[j]);
			}
			else if (piece.color == Color.BLACK && otherpiece.color == Color.WHITE)
			{
				int[][] moves = null;
				if (otherpiece.type == Pieces.PAWN)
					moves = Pawn.getAllPawnMoves(otherpiece);
				else
					moves = getValidMoves(otherpiece, otherpiece.getMoves());
				for (int j = 0; j < moves.length; j++)
					otherMoves.add(moves[j]);
			}
		}
		
		for (int i = 0; i < validmoves.length; i++)
		{
			if (piece.type == Pieces.KING)
			{
				if (validmoves[i] != null && !contains(validmoves, i, otherMoves))
				{
					g2.drawRect(getX(validmoves[i][0]), getY(validmoves[i][1]), CELL_SIZE, CELL_SIZE);
				}
			}
			else if (validmoves[i] != null)
				g2.drawRect(getX(validmoves[i][0]), getY(validmoves[i][1]), CELL_SIZE, CELL_SIZE);
		}
		
		g2.setStroke(new BasicStroke(1));
	}
	
	public boolean contains(int[][] move, int index, ArrayList<int[]> arr)
	{
		for (int i = 0; i < arr.size(); i++)
		{
			if (move[index] != null && arr.get(i) != null)
			{
				if (move[index][0] == arr.get(i)[0] && move[index][1] == arr.get(i)[1])
					return true;
			}
		}
		return false;
	}
	
	public void incrementTurn()
	{
		if (turn == Color.WHITE)
		{
			turn = Color.BLACK;
			setTitle("Chess  -  Black's Turn");
		}
		else if (turn == Color.BLACK)
		{
			turn = Color.WHITE;
			setTitle("Chess  -  White's Turn");
		}
	}
	
	public void checkCollision(Piece piece)
	{
		for (int i = 0; i < pieces.size(); i++)
		{
			Piece piece2 = pieces.get(i);
			if (piece != piece2 &&
					piece.cellx == piece2.cellx &&
					piece.celly == piece2.celly)
			{
				eliminatePiece(piece2);
			}
		}
	}
	
	public void eliminatePiece(Piece piece)
	{
		int index = 0;
		
		if (piece.color == Color.BLACK)
		{
			for (int i = 0; i < outBlackPieces.length; i++)
			{
				if (outBlackPieces[i] == null)
				{
					index = i;
					break;
				}
			}
			outBlackPieces[index] = piece;
			piece.x = (realLength(outBlackPieces) * -50) + 55;
			piece.y = 50;
		}
		else if (piece.color == Color.WHITE)
		{
			for (int i = 0; i < outWhitePieces.length; i++)
			{
				if (outWhitePieces[i] == null)
				{
					index = i;
					break;
				}
			}
			piece.x = (realLength(outWhitePieces) * 50) -5;
			outWhitePieces[index] = piece;
			piece.y = (CELL_SIZE * 8) - 3;
		}
		pieces.remove(piece);
		piece.eliminated = true;
	}
	
	public boolean checkCheckmate(Color color)
	{
		Piece king = null;
		Color oppcolor = null;
		ArrayList<int[]> oppMoves = new ArrayList<int[]>();
		int numMovesOnKing = 0;
		
		if (color == Color.BLACK)
			oppcolor = Color.WHITE;
		else if (color == Color.WHITE)
			oppcolor = Color.BLACK;
		
		for (Piece piece : pieces)
			if (piece.type == Pieces.KING && piece.color == color)
				king = piece;
		
		for (Piece piece : pieces)
		{
			if (piece.color == oppcolor)
			{
				int[][] moves = getValidMoves(piece, piece.getMoves());
				for (int i = 0; i < moves.length; i++)
				{
					oppMoves.add(moves[i]);					
					if (moves[i] != null &&
							moves[i][0] == king.cellx && moves[i][1] == king.celly)
						numMovesOnKing++;
				}
			}
		}
		int[][] kingMoves = getValidMoves(king, king.getMoves());
		
		if (realLength(kingMoves) == 0 && numMovesOnKing > 0)
			return true;
		else if (realLength(kingMoves) > 0 && numMovesOnKing > 0)
		{
			for (int i = 0; i < kingMoves.length; i++)
			{
				if (kingMoves[i] != null)
				{
					numMovesOnKing = 0;
					for (int[] moves : oppMoves)
					{
						if (moves != null &&
								moves[0] == kingMoves[i][0] && moves[1] == kingMoves[i][1])
						{
							numMovesOnKing++;
						}
					}
					
					if (numMovesOnKing == 0)
						return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public void checkCheck(Color color)
	{
		Piece king = null;
		Color oppcolor = null;
		ArrayList<int[]> oppMoves = new ArrayList<int[]>();
		int numMovesOnKing = 0;
		
		if (color == Color.BLACK)
			oppcolor = Color.WHITE;
		else if (color == Color.WHITE)
			oppcolor = Color.BLACK;
		
		for (Piece piece : pieces)
			if (piece.type == Pieces.KING && piece.color == color)
				king = piece;
		
		for (Piece piece : pieces)
		{
			if (piece.color == oppcolor)
			{
				int[][] moves = getValidMoves(piece, piece.getMoves());
				for (int i = 0; i < moves.length; i++)
				{
					oppMoves.add(moves[i]);
					if (moves[i] != null &&
							moves[i][0] == king.cellx && moves[i][1] == king.celly)
						numMovesOnKing++;
				}
			}
		}
		
		if (numMovesOnKing > 0 && color == Color.BLACK && !blackCheck)
		{
			blackCheck = true;
			if (win == null)
				showMessage("BLACK is in check.", "Check");
		}
		else if (numMovesOnKing > 0 && color == Color.WHITE && !whiteCheck)
		{
			whiteCheck = true;
			if (win == null)
				showMessage("WHITE is in check.", "Check");
		}
		if (numMovesOnKing == 0 && color == Color.BLACK)
			blackCheck = false;
		else if (numMovesOnKing == 0 && color == Color.WHITE)
			whiteCheck = false;
	}
	
	public void checkPawnAtOtherSide()
	{
		for (Piece piece : pieces)
		{
			if (choosingPiece == null && piece.type == Pieces.PAWN)
			{
				if (piece.color == Color.BLACK && piece.celly == 0)
				{
					showMessage("BLACK may now choose any piece on the board for the pawn to become.", "");
					choosingPiece = piece;
				}
				else if (piece.color == Color.WHITE && piece.celly == 7)
				{
					showMessage("WHITE may now choose any piece on the board for the pawn to become.", "");
					choosingPiece = piece;
				}
			}
		}
	}
	
	public void showMessage(String message, String title)
	{
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.PLAIN_MESSAGE);
	}
	
	public int realLength(Piece[] array)
	{
		int len = 0;
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] != null)
				len++;
		}
		return len;
	}
	
	public int realLength(int[][] array)
	{
		int len = 0;
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] != null)
				len++;
		}
		return len;
	}
	
	public int[] arr(int x, int y)
	{
		return new int[]{x, y};
	}
	
	public int getX(int x)
	{
		return (x * CELL_SIZE);
	}
	
	public int getY(int y)
	{
		return (y * CELL_SIZE);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		for (Piece piece : pieces)
		{
			if (e.getX() - 61 >= piece.x && e.getX() - 61 <= piece.x + CELL_SIZE &&
					e.getY() - 95 >= piece.y && e.getY() - 95 <= piece.y + CELL_SIZE)
			{
				pressed = piece;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		pressed = null;
		
		if (choosingPiece == null)
		{
			for (int i = 0; i < pieces.size(); i++)
			{
				Piece piece = pieces.get(i);
				ArrayList<int[]> otherMoves = new ArrayList<int[]>();
				for (int j = 0; j < pieces.size(); j++)
				{
					Piece otherpiece = pieces.get(j);
					if (piece.color == Color.WHITE && otherpiece.color == Color.BLACK)
					{
						int[][] moves = null;
						if (otherpiece.type == Pieces.PAWN)
							moves = Pawn.getAllPawnMoves(otherpiece);
						else
							moves = getValidMoves(otherpiece, otherpiece.getMoves());
						for (int k = 0; k < moves.length; k++)
							otherMoves.add(moves[k]);
					}
					else if (piece.color == Color.BLACK && otherpiece.color == Color.WHITE)
					{
						int[][] moves = null;
						if (otherpiece.type == Pieces.PAWN)
							moves = Pawn.getAllPawnMoves(otherpiece);
						else
							moves = getValidMoves(otherpiece, otherpiece.getMoves());
						for (int k = 0; k < moves.length; k++)
							otherMoves.add(moves[k]);
					}
				}
				
				if (e.getX() - 61 >= piece.x && e.getX() - 61 <= piece.x + CELL_SIZE &&
						e.getY() - 95 >= piece.y && e.getY() - 95 <= piece.y + CELL_SIZE)
				{
					if (selected != null && 
							selected.equals(piece))
						selected = null;
					else if (piece.color == turn)
						selected = piece;
				}
				else if (selected != null)
				{
					int[][] validmoves = getValidMoves(selected, selected.getMoves());
					for (int j = 0; j < validmoves.length; j++)
					{
						if (validmoves[j] != null)
						{
							if (e.getX() - 61 >= getX(validmoves[j][0]) && e.getX() - 61 <= getX(validmoves[j][0]) + CELL_SIZE &&
									e.getY() - 95 >= getY(validmoves[j][1]) && e.getY() - 95 <= getY(validmoves[j][1]) + CELL_SIZE)
							{
								if ((selected.color == Color.BLACK && !blackCheck || (blackCheck && selected.type == Pieces.KING)) ||
										(selected.color == Color.WHITE && !whiteCheck || (whiteCheck && selected.type == Pieces.KING)))
								{
									if (selected.type == Pieces.KING && !contains(validmoves, j, otherMoves))
									{
										selected.move(getX(validmoves[j][0]), getY(validmoves[j][1]));
										selected.numberOfMoves++;
										incrementTurn();
										checkCollision(selected);
										selected = null;
									}
									else if (selected.type != Pieces.KING)
									{
										selected.move(getX(validmoves[j][0]), getY(validmoves[j][1]));
										selected.numberOfMoves++;
										incrementTurn();
										checkCollision(selected);
										selected = null;
									}
								}
							}
						}
					}
				}
			}
		}
		else if (choosingPiece != null)
		{
			for (int i = 0; i < pieces.size(); i++)
			{
				Piece piece = pieces.get(i);
				if (e.getX() - 61 >= piece.x && e.getX() - 61 <= piece.x + CELL_SIZE &&
						e.getY() - 95 >= piece.y && e.getY() - 95 <= piece.y + CELL_SIZE)
				{
					if (piece.type != Pieces.KING && piece.color == choosingPiece.color)
					{
						choosingPiece.type = piece.type;
						choosingPiece.image = piece.image;
						choosingPiece = null;
						break;
					}
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (Exception e) {}
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Board frame = new Board();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	private void initUI()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, (CELL_SIZE * 8) + 122, (CELL_SIZE * 8) + 155);
		setTitle("Chess  -  White's Turn");
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		addMouseListener(this);
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
