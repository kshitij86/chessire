package com.chessire.engine.board;

// The Piece class is in another package, so import it.
import com.chessire.engine.pieces.Piece;
import org.carrot2.shaded.guava.common.collect.ImmutableMap;

import java.util.*;

// Cannot make Tile objects.
public abstract class Tile {

    // Once it is set by constructor, it cannot be changed.
    protected final int tileCoordinate;

    // If someone tries to mutate the map, they get exception, as it is not really accessible.
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    // Makes the chess board, with empty tiles, I think.
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles(){
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0 ; i < BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i , new EmptyTile(i));
        }

        // Return its copy, because if we return the original one, its values can be easily modified.
        // ImmutableMap is present in 'Guava'.
        // Added it to dependencies and imported it.
        return ImmutableMap.copyOf(emptyTileMap);
    }

    // Cannot use the constructor of Tile, only this createTile() method.
    // Return an occupied tile if piece is not null, otherwise the empty tile at a given coordinate.
    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    // The class for an empty tile.
    public static final class EmptyTile extends Tile{

        // Constructor just calls the super class constructor and gives it the coordinate.
        private EmptyTile(final int coordinate){
            super(coordinate);
        }

        // Tile is not occupied, as it is empty. Return false.
        @Override
        public boolean isTileOccupied() { return false; }

        // Return null, as there will not be pieces on empty tiles.
        @Override
        public Piece getPiece(){
            return null;
        }
    }

    // Class for occupied tiles.
    public static final class OccupiedTile extends Tile{

        // Piece object to denote what is there on this tile.
        // It is set to private, so its value can only be set by the getPiece().
        private final Piece pieceOnTile;

        private OccupiedTile(final int coordinate, final Piece pieceOnTile){
            super(coordinate);
            this.pieceOnTile = pieceOnTile;
        }

        // Yes, there is a piece on this tile.
        public boolean isTileOccupied() { return true; }

        // Return the piece object.
        public Piece getPiece() { return this.pieceOnTile; }
    }
}
