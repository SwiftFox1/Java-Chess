// Chess (Queen)
// Written By Ethan Rowan
// March 2018
package game.pieces;

import game.Piece;

public class Queen
{
	public static int[][] getMoves(Piece piece)
	{
		int[][] rookmoves = Rook.getMoves(piece);
		int[][] bishopmoves = Bishop.getMoves(piece);
		int[][] moves = new int[rookmoves.length + bishopmoves.length][2];
		
		for (int i = 0; i < rookmoves.length; i++)
			moves[i] = rookmoves[i];
		for (int i = 0; i < bishopmoves.length; i++)
			moves[rookmoves.length + i] = bishopmoves[i];
		return moves;
	}
}
