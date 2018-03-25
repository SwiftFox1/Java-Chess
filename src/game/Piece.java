// Chess (Piece)
// Written By Ethan Rowan
// March 2018
package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.pieces.*;

public class Piece
{
	public Pieces type;
	public Color color;
	public BufferedImage image;
	public int x, y, cellx, celly;
	public int numberOfMoves;
	public boolean eliminated;
	
	public Piece(Pieces type, Color color, int cellx, int celly)
	{
		this.type = type;
		this.color = color;
		this.cellx = cellx;
		this.celly = celly;
		image = getImage();
		x = getX(cellx);
		y = getY(celly);
	}
	
	public int[][] getMoves()
	{
		switch (type)
		{
			case KING:
				return King.getMoves(this);
			case QUEEN:
				return Queen.getMoves(this);
			case BISHOP:
				return Bishop.getMoves(this);
			case KNIGHT:
				return Knight.getMoves(this);
			case ROOK:
				return Rook.getMoves(this);
			case PAWN:
				return Pawn.getMoves(this);
			default:
				return new int[][]{};
		}
	}
	
	public void move(int x, int y)
	{
		this.x = x;
		this.y = y;
		cellx = x/Board.CELL_SIZE;
		celly = y/Board.CELL_SIZE;
	}
	
	public void showOutline(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(15));
		g2.setColor(Color.BLUE);
		g2.drawRect(getX(cellx),
				getY(celly), Board.CELL_SIZE, Board.CELL_SIZE);
		g2.setStroke(new BasicStroke(1));
	}
	
	private BufferedImage getImage()
	{
		try
		{
			BufferedImage spritesheet = ImageIO.read(new File("chess_sprites.png"));
			
			int height = 0;
			if (color == Color.BLACK)
				height = 334;
			
			switch (type)
			{
				case KING:
					return spritesheet.getSubimage(0, height, 334, 334);
				case QUEEN:
					return spritesheet.getSubimage(334, height, 334, 334);
				case BISHOP:
					return spritesheet.getSubimage(668, height, 334, 334);
				case KNIGHT:
					return spritesheet.getSubimage(1002, height, 334, 334);
				case ROOK:
					return spritesheet.getSubimage(1336, height, 334, 334);
				case PAWN:
					return spritesheet.getSubimage(1670, height, 334, 334);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private int getX(int x)
	{
		return (x * Board.CELL_SIZE);
	}
	
	private int getY(int y)
	{
		return (y * Board.CELL_SIZE);
	}
}
