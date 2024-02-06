package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

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
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : potentialMoves) {
            if (simulateMove(move)) {
                validMoves.add(move);
            }
        }

        return validMoves;
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
            throw new InvalidMoveException("No piece at the starting position and/or not your turn");
        }

        // Check if the move is valid
        Collection<ChessMove> validMoves = piece.pieceMoves(board, startPosition);
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        // Execute the move
        board.addPiece(endPosition, piece);
        board.addPiece(startPosition, null);

        // Pawn promotion logic
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && (endPosition.getRow() == 1 || endPosition.getRow() == 8)) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            board.addPiece(endPosition, piece);
        }

        // Update the current team's turn
        currTeamColor = (currTeamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Find the king's position
        ChessPosition kingPosition = findKingPosition(teamColor);

        // Check if any of the opponent's pieces can move to the king's position
        TeamColor opponentColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece != null && currentPiece.getTeamColor() == opponentColor) {
                    Collection<ChessMove> moves = currentPiece.pieceMoves(board, currentPosition);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private ChessPosition findKingPosition(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false; // Not in checkmate if not in check
        }

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    if (canPieceEscapeCheck(currentPosition, currentPiece)) {
                        return false; // Found at least one move to escape check
                    }
                }
            }
        }
        return true;
    }

    private boolean canPieceEscapeCheck(ChessPosition currentPosition, ChessPiece currentPiece) {
        Collection<ChessMove> possibleMoves = currentPiece.pieceMoves(board, currentPosition);
        for (ChessMove move : possibleMoves) {
            if (simulateMove(move)) {
                return true;
            }
        }
        return false;
    }

    private boolean simulateMove(ChessMove move) {
            // Save current state
            ChessPiece originalPieceAtEnd = board.getPiece(move.getEndPosition());
            ChessPiece originalPieceAtStart = board.getPiece(move.getStartPosition());

            // Perform the move
            board.addPiece(move.getEndPosition(), originalPieceAtStart);
            board.addPiece(move.getStartPosition(), null);

            // Check if move puts own king in check
            boolean isInCheckAfterMove = isInCheck(currTeamColor);

            // Revert the move
            board.addPiece(move.getStartPosition(), originalPieceAtStart);
            board.addPiece(move.getEndPosition(), originalPieceAtEnd);

            // Return true if move does not put own king in check
            return !isInCheckAfterMove;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // Can't be stalemate if someone is in check.
        if (isInCheck(teamColor)) {
            return false;
        }

        // Iterate over all pieces of the current player's team in order to find a possible move.
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(currentPosition);

                // Skip if there's no piece or if the piece is not from the current player's team.
                if (piece == null || piece.getTeamColor() != teamColor) {
                    continue;
                }

                // Get all possible moves for this piece.
                Collection<ChessMove> possibleMoves = piece.pieceMoves(board, currentPosition);

                for (ChessMove move : possibleMoves) {
                    // Simulate each move.
                    ChessPiece targetPiece = board.getPiece(move.getEndPosition());
                    // Execute the move temporarily.
                    board.addPiece(move.getEndPosition(), piece);
                    board.addPiece(currentPosition, null);

                    boolean stillInCheckAfterMove = isInCheck(teamColor);

                    // Undo the move.
                    board.addPiece(currentPosition, piece);
                    board.addPiece(move.getEndPosition(), targetPiece);

                    // If, after any move, the player is not in check, it's not a stalemate.
                    if (!stillInCheckAfterMove) {
                        return false;
                    }
                }
            }
        }
        return true;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ChessGame chessGame = (ChessGame) obj;
        return Objects.equals(board, chessGame.board) && currTeamColor == chessGame.currTeamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currTeamColor);
    }
}
