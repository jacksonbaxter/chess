package chess;

import java.util.Collection;
import java.util.Collections;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor currTeamColor;

    public ChessGame() {
        this.board = new ChessBoard();
        this.currTeamColor = TeamColor.WHITE;
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currTeamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currTeamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Check if there's a piece at the given position
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null || piece.getTeamColor() != currTeamColor) {
            // Return empty collection if no piece at startPosition or if the piece does not belong to the current team
            return Collections.emptySet();
        }

        // Get valid moves for the piece
        return piece.pieceMoves(board, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        // Get the piece at the startPosition
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null || piece.getTeamColor() != currTeamColor) {
            throw new InvalidMoveException("No piece at the starting position or not your turn");
        }

        // Check if the move is valid
        Collection<ChessMove> validMoves = piece.pieceMoves(board, startPosition);
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        // Execute the move
        board.addPiece(endPosition, piece);
        board.addPiece(startPosition, null);

        // Handle capture if there's an opposing piece at the toPosition
        ChessPiece capturedPiece = board.getPiece(endPosition);
        if (capturedPiece != null && capturedPiece.getTeamColor() != currTeamColor) {
            // Implement capture logic (e.g., removing the captured piece)
        }

        // Pawn promotion logic (if applicable)
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && (endPosition.getRow() == 1 || endPosition.getRow() == 8)) {
            // Implement pawn promotion logic
        }

        // Update the current team's turn
        currTeamColor = (currTeamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        // Additional checks for check/checkmate/stalemate can be added here
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
