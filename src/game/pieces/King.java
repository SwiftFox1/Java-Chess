// Chess (King)
// Written By Ethan Rowan
// March 2018
package game.pieces;

import game.Piece;

public class King
{
	public static int[][] getMoves(Piece piece)
	{
		int[][] moves = new int[8][2];
		
		//Up
		if (piece.celly - 1 >= 0)
			moves[0] = arr(piece.cellx, piece.celly - 1);
		else
			moves[0] = null;
		//Right
		if (piece.cellx + 1 <= 7)
			moves[1] = arr(piece.cellx + 1, piece.celly);
		else
			moves[1] = null;
		//Down
		if (piece.celly + 1 <= 7)
			moves[2] = arr(piece.cellx, piece.celly + 1);
		else
			moves[2] = null;
		//Left
		if (piece.cellx - 1 >= 0)
			moves[3] = arr(piece.cellx - 1, piece.celly);
		else
			moves[3] = null;
		
		//Right-Down
		if (piece.cellx + 1 <= 7 && piece.celly + 1 <= 7)
			moves[4] = arr(piece.cellx + 1, piece.celly + 1);
		else
			moves[4] = null;
		//Left-Down
		if (piece.cellx - 1 >= 0 && piece.celly + 1 <= 7)
			moves[5] = arr(piece.cellx - 1, piece.celly + 1);
		else
			moves[5] = null;
		//Left-Up
		if (piece.cellx - 1 >= 0 && piece.celly - 1 >= 0)
			moves[6] = arr(piece.cellx - 1, piece.celly - 1);
		else
			moves[6] = null;
		//Right-Up
		if (piece.cellx + 1 <= 7 && piece.celly - 1 >= 0)
			moves[7] = arr(piece.cellx + 1, piece.celly - 1);
		else
			moves[7] = null;
		return moves;
	}
	
	private static int[] arr(int x, int y)
	{
		return new int[]{x, y};
	}
}
