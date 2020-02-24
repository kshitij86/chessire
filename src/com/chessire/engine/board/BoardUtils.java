package com.chessire.engine.board;

public class BoardUtils {

    // These arrays handle the edge cases where the Knight movement using the offsets fails.
    public static final boolean[] FIRST_COLUMN = initializeColumns(0);
    public static final boolean[] SECOND_COLUMN = initializeColumns(1);
    public static final boolean[] SEVENTH_COLUMN = initializeColumns(6);
    public static final boolean[] EIGHTH_COLUMN = initializeColumns(7);

    // TODO add initializeRows() method here.
    public static final boolean[] SECOND_ROW = null;
    public static final boolean[] SEVENTH_ROW = null;

    // Number of tiles, and number of tiles per row.
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    private BoardUtils(){
        // Throw a runtime exception if this class is instantiated, as it is only a helper class.
        throw new RuntimeException("Class 'BoardUtils' cannot be instantiated.");
    }

    // Initialize the column with true value.
    private static boolean[] initializeColumns(int columnNumber) {

        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        }
        while (columnNumber < NUM_TILES);
        return column;
    }

    // This method is useful to all pieces, not only a Knight so moved it here.
    // Checks if given coordinate is within the chess (0 - 63) board or not.
    public static boolean isValidTileCoordinate(final int candidateDestinationCoordinate) {
        return candidateDestinationCoordinate >= 0 && candidateDestinationCoordinate < NUM_TILES;
    }
}
