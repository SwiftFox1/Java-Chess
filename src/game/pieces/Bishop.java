// Chess (Bishop)
// Written By Ethan Rowan
// March 2018
package game.pieces;

import java.util.ArrayList;

import game.Board;
import game.Piece;

public class Bishop
{
	public static int[][] getMoves(Piece piece)
	{
		ArrayList<int[]> moveslist = new ArrayList<int[]>();
		int[][] moves;
		
		//Right-Down
		int x = piece.cellx, y = piece.celly;
		while (x <= 7 || y <= 7)
		{
			Piece p = getNeighbor(piece, x-piece.cellx, y-piece.celly);
			moveslist.add(arr(x++, y++));
			if (p != piece && p != null)
				break;
		}
		//Left-Up
		x = piece.cellx;
		y = piece.celly;
		while (x >= 0 || y >= 0)
		{
			Piece p = getNeighbor(piece, -(piece.cellx-x), -(piece.celly-y));
			moveslist.add(arr(x--, y--));
			if (p != piece && p != null)
				break;
		}
		//Right-Up
		x = piece.cellx;
		y = piece.celly;
		while (x <= 7 || y >= 0)
		{
			Piece p = getNeighbor(piece, x-piece.cellx, -(piece.celly-y));
			moveslist.add(arr(x++, y--));
			if (p != piece && p != null)
				break;
		}
		//Left-Down
		x = piece.cellx;
		y = piece.celly;
		while (x >= 0 || y <= 7)
		{
			Piece p = getNeighbor(piece, -(piece.cellx-x), y-piece.celly);
			moveslist.add(arr(x--, y++));
			if (p != piece && p != null)
				break;
		}
		
		moves = new int[moveslist.size()][2];
		for (int i = 0; i < moveslist.size(); i++)
			moves[i] = moveslist.get(i);
		
		return moves;
	}
	
	public static Piece getNeighbor(Piece piece, int xoff, int yoff)
	{
		for (int j = 0; j < Board.pieces.size(); j++)
		{
			Piece piece2 = Board.pieces.get(j);
			if (piece.cellx + xoff == piece2.cellx &&
					piece.celly + yoff == piece2.celly)
				return piece2;
		}
		return null;
	}
	
	private static int[] arr(int x, int y)
	{
		return new int[]{x, y};
	}
}
