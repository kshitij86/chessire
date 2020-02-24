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


// Class for the Knight piece.
public class Knight extends Piece {

    /* Fixed offsets for the Knight to move to, depending upon current position.
     * These are the count of tiles to be moved continuously (wrapped across the board),
     * to reach a legal Knight position from its current tile.
     * There are 8 moves possible, if the Knight is in somewhat center, and has enough space to
     * move to any legal position desired. If there are invalid blocks, we skip them (the method checks this).
     */
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    // Constructor matching parent class.
    // Instantiate new Knight object with its Alliance and current position.
    Knight(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        // List of legal moves from the current tile.
        final List<Move> legalMoves = new ArrayList<>();

        // Loop through the coordinate offsets, in each case determining whether its a valid move.
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){

            int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                // Check if we are on the edge and which cases will break the rule.
                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEigthColumnExclusion(this.piecePosition, currentCandidateOffset)) { continue; }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if(candidateDestinationTile.isTileOccupied()){
                    // Add a new move to the list, if the destination tile is not occupied.
                    // Not attacking, simply move to this legal location.
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {

                    final Piece pieceAtLocation = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtLocation.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance){
                        // If their Alliance is different, piece at destination can be captured, so add a new move.
                        legalMoves.add(new Move.AttackMove(board, this,
                                candidateDestinationCoordinate, pieceAtLocation));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    // Edge cases, for when using the 'CANDIDATE_MOVE_COORDINATES' will not work.
    private  static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        // If the piece is in the first column, the offsets specified break the rule and thus we do not apply them.
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10
                || candidateOffset == 6 || candidateOffset == 15);
    }

    private  static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        // If the piece is in the specified column, the offsets specified break the rule and thus we do not apply them.
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private  static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        // If the piece is in the specified column, the offsets specified break the rule and thus we do not apply them.
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private  static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset){
        // If the piece is in the column, the offsets specified break the rule and thus we do not apply them.
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == -15
        || candidateOffset == 10 || candidateOffset == 17);
    }

}
