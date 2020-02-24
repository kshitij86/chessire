package com.chessire.engine.pieces;

import com.chessire.engine.Alliance;
import com.chessire.engine.board.Board;
import com.chessire.engine.board.BoardUtils;
import com.chessire.engine.board.Move;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {
    // '8' for a simple move in one step, '16' for jumping two tiles.
    private final static int[] CANDIDATE_MOVE_COORDINATES = {8, 16, 9 , 7};

    Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            // Apply -8 offset for White (as they move up) and 8 offset for Black (they move down).
            int candidateDestinationCoordinate = this.piecePosition +
                    (this.getPieceAlliance().getDirection() * currentCandidateOffset);

            // If not a valid tile, skip it.
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }

            // If tile is empty, move Pawn one step in corresponding direction (specified by its offset).
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                // TODO  add the class for Pawn movement, they move differently.
                // TODO also deal with promotions.A
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()) ){

                // WTF.
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else if(currentCandidateOffset == 7 &&
                        !((BoardUtils.EIGHTH_COLUMN[this.piecePosition]) && this.getPieceAlliance().isWhite()) ||
                        (BoardUtils.FIRST_COLUMN[this.piecePosition]) && this.getPieceAlliance().isBlack()){

                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance())
                            // TODO attacking and pawn promotion
                            legalMoves.add(new Move.MajorMove(board, this,  candidateDestinationCoordinate));
                    }

                } else if(currentCandidateOffset == 9 &&
                         !((BoardUtils.FIRST_COLUMN[this.piecePosition]) && this.getPieceAlliance().isWhite()) ||
                         (BoardUtils.EIGHTH_COLUMN[this.piecePosition]) && this.getPieceAlliance().isBlack()){

                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance())
                            // TODO attacking and pawn promotion
                            legalMoves.add(new Move.MajorMove(board, this,  candidateDestinationCoordinate));
                    }
                }
            }
        }


        return ImmutableList.copyOf(legalMoves);
    }
}
