package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private static final int[][] KING_DIRECTIONS = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
    private static final int[][] QUEEN_DIRECTIONS = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
    private static final int[][] BISHOP_DIRECTIONS = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    private static final int[][] KNIGHT_DIRECTIONS = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};
    private static final int[][] ROOK_DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>(); // Using Set to prevent duplicates

        switch (this.getPieceType()) {
            case KING:
                addKingMoves(moves, board, myPosition);
                break;
            case QUEEN:
                addQueenMoves(moves, board, myPosition);
                break;
            case BISHOP:
                addBishopMoves(moves, board, myPosition);
                break;
            case KNIGHT:
                addKnightMoves(moves, board, myPosition);
                break;
            case ROOK:
                addRookMoves(moves, board, myPosition);
                break;
            case PAWN:
                addPawnMoves(moves, board, myPosition);
                break;
        }

        return moves;
    }

    private boolean isPositionValid(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private boolean isValidMove(ChessPosition position, ChessBoard board) {
        ChessPiece pieceAtPosition = board.getPiece(position);
        return pieceAtPosition == null || pieceAtPosition.getTeamColor() != this.pieceColor;
    }

    private void addKingMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        for (int i = 0; i < KING_DIRECTIONS.length; i++) {
            int newRow = myPosition.getRow() + KING_DIRECTIONS[i][0];
            int newCol = myPosition.getColumn() + KING_DIRECTIONS[i][1];

            // Add the move if it's valid (either an empty square or capturing an opponent's piece)
            if (isPositionValid(newRow, newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if (isValidMove(newPosition, board)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    private void addQueenMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        for (int[] direction : QUEEN_DIRECTIONS) {
            int newRow = myPosition.getRow();
            int newCol = myPosition.getColumn();

            while (isPositionValid(newRow += direction[0], newCol += direction[1])) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                // Add the move if it's valid (either an empty square or capturing an opponent's piece)
                if (isValidMove(newPosition, board)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }

                // Break out of the loop if the square is not empty
                if (pieceAtNewPosition != null) {
                    break;
                }
            }
        }
    }

    private void addBishopMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        for (int[] direction : BISHOP_DIRECTIONS) {
            int newRow = myPosition.getRow();
            int newCol = myPosition.getColumn();

            while (isPositionValid(newRow += direction[0], newCol += direction[1])) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                // Add the move if it's valid (either an empty square or capturing an opponent's piece)
                if (isValidMove(newPosition, board)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }

                // Break out of the loop if the square is not empty
                if (pieceAtNewPosition != null) {
                    break;
                }
            }
        }
    }

    private void addKnightMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        for (int[] direction : KNIGHT_DIRECTIONS) {
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];

            // Add the move if it's valid (either an empty square or capturing an opponent's piece)
            if (isPositionValid(newRow, newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if (isValidMove(newPosition, board)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    private void addRookMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        for (int[] direction : ROOK_DIRECTIONS) {
            int newRow = myPosition.getRow();
            int newCol = myPosition.getColumn();

            while (isPositionValid(newRow += direction[0], newCol += direction[1])) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                // Add the move if it's valid (either an empty square or capturing an opponent's piece)
                if (isValidMove(newPosition, board)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }

                // Break out of the loop if the square is not empty
                if (pieceAtNewPosition != null) {
                    break;
                }
            }
        }
    }

    private void addPawnMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int direction = this.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = this.getTeamColor() == ChessGame.TeamColor.WHITE ? 2 : 7;
        int promotionRow = this.getTeamColor() == ChessGame.TeamColor.WHITE ? 8 : 1;

        // Single forward move
        handleForwardMove(moves, board, myPosition, direction, startRow, promotionRow);

        // Capture moves
        handleCaptureMoves(moves, board, myPosition, direction, promotionRow);
    }

    private void handleForwardMove(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, int direction, int startRow, int promotionRow) {
        int forwardRow = myPosition.getRow() + direction;
        if (isPositionValid(forwardRow, myPosition.getColumn()) && board.getPiece(new ChessPosition(forwardRow, myPosition.getColumn())) == null) {
            addMoveWithPromotion(moves, myPosition, forwardRow, myPosition.getColumn(), promotionRow);

            // Two-square move from starting position
            if (myPosition.getRow() == startRow) {
                int twoForwardRow = forwardRow + direction;
                if (isPositionValid(twoForwardRow, myPosition.getColumn()) && board.getPiece(new ChessPosition(twoForwardRow, myPosition.getColumn())) == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(twoForwardRow, myPosition.getColumn()), null));
                }
            }
        }
    }

    private void handleCaptureMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, int direction, int promotionRow) {
        int forwardRow = myPosition.getRow() + direction;
        int[] captureCols = {myPosition.getColumn() - 1, myPosition.getColumn() + 1};
        for (int captureCol : captureCols) {
            if (isPositionValid(forwardRow, captureCol)) {
                ChessPiece capturePiece = board.getPiece(new ChessPosition(forwardRow, captureCol));
                if (capturePiece != null && capturePiece.getTeamColor() != this.getTeamColor()) {
                    addMoveWithPromotion(moves, myPosition, forwardRow, captureCol, promotionRow);
                }
            }
        }
    }

    private void addMoveWithPromotion(Collection<ChessMove> moves, ChessPosition from, int toRow, int toCol, int promotionRow) {
        ChessPosition toPosition = new ChessPosition(toRow, toCol);
        if (toRow == promotionRow) {
            // Promotion moves
            for (PieceType promotionPiece : new PieceType[]{PieceType.QUEEN, PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP}) {
                moves.add(new ChessMove(from, toPosition, promotionPiece));
            }
        } else {
            // Regular move
            moves.add(new ChessMove(from, toPosition, null));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChessPiece temp)) return false;

        return (Objects.equals(pieceColor, temp.pieceColor)) && (Objects.equals(type, temp.type));
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }
}
