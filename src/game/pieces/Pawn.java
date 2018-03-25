// Chess (Pawn)
// Written By Ethan Rowan
// March 2018
package game.pieces;

import java.awt.Color;

import game.Board;
import game.Piece;

public class Pawn
{
	public static int[][] getMoves(Piece piece)
	{
		int[][] moves = new int[][]{};
		
		if (piece.color == Color.BLACK)
		{
			moves = new int[4][2];
			
			Piece p1 = getNeighbor(piece, 0, -1);
			if (p1 == null)
				moves[0] = arr(piece.cellx, piece.celly - 1);
			else
				moves[0] = null;
			if (piece.numberOfMoves == 0)
			{
				Piece p2 = getNeighbor(piece, 0, -2);
				if (p1 == null && p2 == null)
					moves[1] = arr(piece.cellx, piece.celly - 2);
				else
					moves[1] = null;
			}
			else
				moves[1] = null;
			Piece p3 = getNeighbor(piece, 1, -1);
			if (p3 != null || (p3 != null && p3.color != piece.color))
				moves[2] = arr(piece.cellx + 1, piece.celly - 1);
			else
				moves[2] = null;
			Piece p4 = getNeighbor(piece, -1, -1);
			if (p4 != null || (p4 != null && p4.color != piece.color))
				moves[3] = arr(piece.cellx - 1, piece.celly - 1);
			else
				moves[3] = null;
			return moves;
		}
		else if (piece.color == Color.WHITE)
		{
			moves = new int[4][2];
			Piece p1 = getNeighbor(piece, 0, 1);
			if (p1 == null)
				moves[0] = arr(piece.cellx, piece.celly + 1);
			else
				moves[0] = null;
			if (piece.numberOfMoves == 0)
			{
				Piece p2 = getNeighbor(piece, 0, 2);
				if (p1 == null && p2 == null)
					moves[1] = arr(piece.cellx, piece.celly + 2);
				else
					moves[1] = null;
			}
			else
				moves[1] = null;
			Piece p3 = getNeighbor(piece, -1, 1);
			if (p3 != null || (p3 != null && p3.color != piece.color))
				moves[2] = arr(piece.cellx - 1, piece.celly + 1);
			else
				moves[2] = null;
			Piece p4 = getNeighbor(piece, 1, 1);
			if (p4 != null || (p4 != null && p4.color != piece.color))
				moves[3] = arr(piece.cellx + 1, piece.celly + 1);
			else
				moves[3] = null;
			return moves;
		}
		return moves;
	}
	
	public static int[][] getAllPawnMoves(Piece piece)
	{
		int[][] moves = new int[][]{};
		
		if (piece.color == Color.BLACK)
		{
			moves = new int[3][2];
			Piece p1 = getNeighbor(piece, 0, -1);
			if (piece.numberOfMoves == 0)
			{
				Piece p2 = getNeighbor(piece, 0, -2);
				if (p1 == null && p2 == null)
					moves[0] = arr(piece.cellx, piece.celly - 2);
				else
					moves[0] = null;
			}
			moves[1] = arr(piece.cellx + 1, piece.celly - 1);
			moves[2] = arr(piece.cellx - 1, piece.celly - 1);
		}
		else if (piece.color == Color.WHITE)
		{
			moves = new int[3][2];
			Piece p1 = getNeighbor(piece, 0, +1);
			if (piece.numberOfMoves == 0)
			{
				Piece p2 = getNeighbor(piece, 0, +2);
				if (p1 == null && p2 == null)
					moves[0] = arr(piece.cellx, piece.celly + 2);
				else
					moves[0] = null;
			}
			moves[1] = arr(piece.cellx - 1, piece.celly + 1);
			moves[2] = arr(piece.cellx + 1, piece.celly + 1);
			return moves;
		}
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
