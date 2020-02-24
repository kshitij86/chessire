package com.chessire.engine.pieces;

import com.chessire.engine.Alliance;
import com.chessire.engine.board.Board;
import com.chessire.engine.board.BoardUtils;
import com.chessire.engine.board.Move;
import com.chessire.engine.board.Tile;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends Piece {

    // Adding or subtracting '7' or '9' from current position gives us the upper and lower diagonals for a bishop.
    private final static int[] CANDIDATE_COORDINATE_MOVE_VECTOR_COORDINATES = {-7, -9, 7, 9};

    Bishop(final int piecePosition,
           final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        // Loop through the offsets to determine whether a move is valid or not.
        for(final int candidateCoordinateOffset : CANDIDATE_COORDINATE_MOVE_VECTOR_COORDINATES){

            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                        isEigthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                }

                candidateDestinationCoordinate += candidateCoordinateOffset;

                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if(!candidateDestinationTile.isTileOccupied()){

                        // If tile is empty, add a move object to legal moves.
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));

                    } else {
                        // Piece on a candidate tile can be captured, if their Alliance is different.
                        final Piece pieceOnDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceOnDestinationAlliance = pieceOnDestination.getPieceAlliance();
                        if(this.pieceAlliance != pieceOnDestinationAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this,
                                    candidateDestinationCoordinate, pieceOnDestination));
                        }
                        // Break out of the loop, once we deal with an occupied tile.
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }


    // Check whether or not bishop is on first or last column. These are the edge cases for a bishop.
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.isValidTileCoordinate(currentPosition) && (candidateOffset == -9 || candidateOffset == 7);
    }
    private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.isValidTileCoordinate(currentPosition) && (candidateOffset == -7 || candidateOffset == 9);
    }
}
