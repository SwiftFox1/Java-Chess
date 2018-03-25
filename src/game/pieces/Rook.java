// Chess (Rook)
// Written By Ethan Rowan
// March 2018
package game.pieces;

import java.util.ArrayList;

import game.Board;
import game.Piece;

public class Rook
{
	public static int[][] getMoves(Piece piece)
	{
		ArrayList<int[]> moveslist = new ArrayList<int[]>();
		int[][] moves;
		
		//Above
		for (int i = piece.celly; i >= 0; i--)
		{
			Piece p = getNeighbor(piece, 0, -(piece.celly-i));
			moveslist.add(arr(piece.cellx, i));
			if (p != piece && p != null)
				break;
		}
		//Below
		for (int i = piece.celly; i <= 7; i++)
		{
			Piece p = getNeighbor(piece, 0, i-piece.celly);
			moveslist.add(arr(piece.cellx, i));
			if (p != piece && p != null)
				break;
		}
		
		//Right
		for (int i = piece.cellx; i <= 7; i++)
		{
			Piece p = getNeighbor(piece, i-piece.cellx, 0);
			moveslist.add(arr(i, piece.celly));
			if (p != piece && p != null)
				break;
		}
		//Left
		for (int i = piece.cellx; i >= 0; i--)
		{
			Piece p = getNeighbor(piece, -(piece.cellx-i), 0);
			moveslist.add(arr(i, piece.celly));
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
	
	public static int[] arr(int x, int y)
	{
		return new int[]{x, y};
	}
}
