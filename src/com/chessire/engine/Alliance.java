package com.chessire.engine;

// Not much clue about enums.
// Tells whether a piece is white or black.
public enum Alliance {
    // The methods give the directionality for pieces (most importantly Pawns).

    // Returns true for isWhite().
    WHITE{
        @Override
        public int getDirection() { return -1; }

        @Override
        public boolean isBlack() { return false; }

        @Override
        public boolean isWhite() { return true; }
    },

    // Returns true for isBlack().
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }
    };

    public abstract int getDirection();
    public abstract boolean isBlack();
    public abstract boolean isWhite();

}
