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

public class King extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    King(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<Move>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (candidateDestinationTile.isTileOccupied()) {
                    // Add a new move to the list, if the destination tile is not occupied.
                    // Not attacking, simply move to this legal location.
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {

                    final Piece pieceAtLocation = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtLocation.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) {
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
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1
                || candidateOffset == 7);
    }

    private  static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        // If the piece is in the specified column, the offsets specified break the rule and thus we do not apply them.
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||
                candidateOffset == 9);
    }



}
