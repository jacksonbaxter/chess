package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition start;
    private final ChessPosition end;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.start = Objects.requireNonNull(startPosition);
        this.end = Objects.requireNonNull(endPosition);
        this.promotionPiece = promotionPiece;
    }

    /**
     * Applies this move to the chessboard. Does not check for validity.
     * @param board The board to make the move on.
     */
    public void apply(ChessBoard board) {
        ChessPiece startPiece = board.getPiece(this.start);

        // Handle special moves
        if (isCastlingMove(startPiece)) {
            applyCastlingMove(board, startPiece);
        } else if (isEnPassantMove(startPiece)) {
            applyEnPassantMove(board, startPiece);
        } else {
            // Standard move or pawn promotion
            ChessPiece movedPiece = this.promotionPiece == null ? startPiece : new ChessPiece(startPiece.getTeamColor(), this.promotionPiece);
            board.addPiece(this.end, movedPiece);
            board.addPiece(this.start, null);

            // Additional logic for rook and king moves
            updateCastlePrivileges(board, startPiece);
        }
    }

    private boolean isCastlingMove(ChessPiece piece) {
        // Castling only possible with the king
        if(piece.getPieceType() != ChessPiece.PieceType.KING) {
            return false;
        }

        //
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        throw new RuntimeException("Not implemented");
    }
}
