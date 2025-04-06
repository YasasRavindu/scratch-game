package com.scratchgame.model;

import lombok.Getter;

@Getter
public class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }


    public static Position fromString(String position) {
        String[] parts = position.split(":");
        int row = Integer.parseInt(parts[0]);
        int column = Integer.parseInt(parts[1]);
        return new Position(row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return 31 * row + column;
    }
}