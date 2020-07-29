package shapes;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractShape implements Shape {
    protected int[][][] positions;
    protected List<Pair<Integer, Integer>> currCoordinates;
    protected int[][] currPosition;
    protected int currPositionPointer;
    protected Pair<Integer, Integer> currCenter;

    public void rotate() {
        currPositionPointer += 1;

        if (currPositionPointer >= positions.length) {
            currPositionPointer = 0;
        }

        currPosition = positions[currPositionPointer];
    }

    public void setCoordinates(Pair<Integer, Integer> shapeCenter) {
        synchronized(this) {
            Integer rowCenter = shapeCenter.getLeft();
            Integer columnCenter = shapeCenter.getRight();
            this.currCoordinates = new ArrayList<>();
            this.currCenter = shapeCenter;

            for (int row = 0; row < currPosition.length; row++) {
                for (int column = 0; column < currPosition[0].length; column++) {
                    if (currPosition[row][column] != 0) {
                        currCoordinates.add(Pair.of(rowCenter + row, column + columnCenter));
                    }
                }
            }
        }
    }

    public String getSymbol() {
        return "*";
    }

    public Pair<Integer, Integer> getCurrCenter() {
        return currCenter;
    }

    public List<Pair<Integer, Integer>> getCurrCoordinates() {
        return currCoordinates;
    }

    public abstract String getColor();

    public boolean isWall(){
        return false;
    }

    public boolean isBottom(){
        return false;
    }

    public boolean isCeiling(){
        return false;
    }
}
