// Chess (Knight)
// Written By Ethan Rowan
// March 2018
package game.pieces;

import game.Piece;

public class Knight
{
	public static int[][] getMoves(Piece piece)
	{
		int [][] moves = new int[8][2];
		
		//1 Right + 2 Down
		if (piece.cellx + 1 <= 7 && piece.celly + 2 <= 7)
			moves[0] = arr(piece.cellx + 1, piece.celly + 2);
		else
			moves[0] = null;
		//1 Left + 2 Down
		if (piece.cellx - 1 >= 0 && piece.celly + 2 <= 7)
			moves[1] = arr(piece.cellx - 1, piece.celly + 2);
		else
			moves[1] = null;
		//2 Right + 1 Up
		if (piece.cellx + 1 <= 7 && piece.celly - 2 >= 0)
			moves[2] = arr(piece.cellx + 1, piece.celly - 2);
		else
			moves[2] =  null;
		//2 Left + 1 Up
		if (piece.cellx - 1 >= 0 && piece.celly - 2 >= 0)
			moves[3] = arr(piece.cellx - 1, piece.celly - 2);
		else
			moves[3] = null;
		
		//2 Right + 1 Up
		if (piece.cellx + 2 <= 7 && piece.celly - 1 >= 0)
			moves[4] = arr(piece.cellx + 2, piece.celly - 1);
		else
			moves[4] = null;
		//2 Right + 1 Down
		if (piece.cellx + 2 <= 7 && piece.celly + 1 <= 7)
			moves[5] = arr(piece.cellx + 2, piece.celly + 1);
		else
			moves[5] = null;
		//2 Left + 1 Up
		if (piece.cellx - 2 >= 0 && piece.celly - 1 >= 0)
			moves[6] = arr(piece.cellx - 2, piece.celly - 1);
		else
			moves[6] = null;
		//2 Left + 1 Down
		if (piece.cellx - 2 >= 0 && piece.celly + 1 <= 7)
			moves[7] = arr(piece.cellx - 2, piece.celly + 1);
		else
			moves[7] = null;
		return moves;
	}
	
	private static int[] arr(int x, int y)
	{
		return new int[]{x, y};
	}
}
