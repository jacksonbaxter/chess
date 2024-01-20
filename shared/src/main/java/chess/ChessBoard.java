package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public ChessPiece[][] board;
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
        this.board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        final int[] MAJOR_PIECE_POSITIONS = {0 , 7, 1, 6, 2, 5, 3, 4}; // Rook, Knight, Bishop, Queen, King

        setupMajorPieces(0, ChessGame.TeamColor.WHITE, MAJOR_PIECE_POSITIONS);
        setupMajorPieces(7, ChessGame.TeamColor.BLACK, MAJOR_PIECE_POSITIONS);
        setupPawns(1, ChessGame.TeamColor.WHITE);
        setupPawns(6, ChessGame.TeamColor.BLACK);
    }

    private void setupMajorPieces(int row, ChessGame.TeamColor teamColor, int[] positions) {
        ChessPiece.PieceType[] types = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING};
        for (int j = 0; j < positions.length; j++) {
            this.board[row][positions[j]] = new ChessPiece(teamColor, types[j / 2]);
        }
    }

    private void setupPawns(int row, ChessGame.TeamColor color) {
        for (int col = 0; col < 8; col++) {
            this.board[row][col] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
        }
    }
}
