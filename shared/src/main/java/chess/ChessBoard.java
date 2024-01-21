package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board;
    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Clear the board first
        for (int i = 0; i < 8; i++) {
            Arrays.fill(this.board[i], null);
        }

        final int[] MAJOR_PIECE_POSITIONS = {1, 2, 3, 4, 5, 6, 7, 8};

        setupMajorPieces(1, ChessGame.TeamColor.WHITE, MAJOR_PIECE_POSITIONS);
        setupMajorPieces(8, ChessGame.TeamColor.BLACK, MAJOR_PIECE_POSITIONS);
        setupPawns(2, ChessGame.TeamColor.WHITE);
        setupPawns(7, ChessGame.TeamColor.BLACK);
    }

    private void setupMajorPieces(int row, ChessGame.TeamColor teamColor, int[] positions) {
        ChessPiece.PieceType[] types = {
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK
        };
        for (int j = 0; j < positions.length; j++) {
            addPiece(new ChessPosition(row, positions[j]), new ChessPiece(teamColor, types[j]));
        }
    }

    private void setupPawns(int row, ChessGame.TeamColor color) {
        for (int col = 1; col <= 8; col++) {
            addPiece(new ChessPosition(row, col), new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChessBoard temp)) return false;

        return Arrays.deepEquals(this.board, temp.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
