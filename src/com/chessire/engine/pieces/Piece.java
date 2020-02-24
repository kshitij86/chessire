package com.chessire.engine.pieces;

import com.chessire.engine.Alliance;
import com.chessire.engine.board.Board;
import com.chessire.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int piecePosition;
    // Whether the piece is white or black, determines its Alliance.
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;


    Piece(final int piecePosition, final Alliance pieceAlliance){
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        // TODO still need to do for isFirstMove
        this.isFirstMove = false;
    }

    // Get the Piece Alliance.
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    // Checks if the current move is final.
    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    // Return a collection of possible moves.
    // Every piece that extends Piece needs to implement this method.
    public abstract Collection<Move> calculateLegalMoves(final Board board);
}
