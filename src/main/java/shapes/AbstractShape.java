/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package shapes;

import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.util.Pair;

public abstract class AbstractShape {
    protected int[][][] positions;
    protected List<Pair<Integer, Integer>> currCoordinates;
    protected int[][] currPosition;
    protected int currPositionPointer;
    protected Pair<Integer, Integer> currCenter;

    public void rotate() {
        if(currPositionPointer < positions.length){
            currPositionPointer+=1;
        } else {
            currPositionPointer = 0;
        }

        currPosition = positions[currPositionPointer];
    }

    public void setCoordinates(Pair<Integer, Integer> shapeCenter) {
        synchronized(this) {
            Integer rowCenter = shapeCenter.fst;
            Integer columnCenter = shapeCenter.snd;
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

    public Pair<Integer, Integer> getCurrCenter() {
        return currCenter;
    }

    public List<Pair<Integer, Integer>> getCurrCoordinates() {
        return currCoordinates;
    }
}
