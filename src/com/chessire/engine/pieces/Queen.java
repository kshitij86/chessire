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

// A Queen's legal moves are the union of a Bishop's and a Rook's.
public class Queen extends Piece {

    // Has the Rook's offsets and the Bishop's also.
    private static final int[] CANDIDATE_COORDINATE_MOVE_VECTOR_COORDINATES = {-7, -9, 7, 9, -8, -1, 1, 8};

    Queen(int piecePosition, Alliance pieceAlliance) {
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
                    // Break out, there are column exclusions.
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


    // Check whether or not Queen is on first or last column. These are the edge cases for a Queen.
    // They include the column exclusions for a Rook and the Bishop.
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.isValidTileCoordinate(currentPosition) &&
                (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
    }
    private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.isValidTileCoordinate(currentPosition) &&
                (candidateOffset == 1 || candidateOffset == -7 || candidateOffset == 9);
    }
}
