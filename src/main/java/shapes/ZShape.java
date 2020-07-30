/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package shapes;

import java.util.ArrayList;
import java.util.List;
import com.sun.tools.javac.util.Pair;


public class ZShape implements Shape {

    private List<Pair<Integer, Integer>> currCoordinates;

    private int[][][] positions = new int[][][]
            { { { 0, 0, 0 },
                    { 0, 1, 1 },
                    { 1, 1, 0 } },
            { { 1, 0, 0 },
                    { 1, 1, 0 },
                    { 0, 1, 0 } } };

    private int[][] currPosition;

    public ZShape(Pair<Integer, Integer> shapeCenter) {
        currPosition = positions[0];
        setCoordinates(shapeCenter);
    }

    @Override
    public List<Pair<Integer, Integer>> getCoordinates() {
        return currCoordinates;
    }

    @Override
    public int[][] rotateLeft() {
        return new int[0][];
    }

    @Override
    public int[][] rotateRight() {
        return new int[0][];
    }

    @Override
    public List<Pair<Integer, Integer>> setCoordinates(Pair<Integer, Integer> shapeCenter) {
        Integer rowCenter = shapeCenter.fst;
        Integer columnCenter = shapeCenter.snd;
        this.currCoordinates = new ArrayList<>();

        for (int row = 0; row < currPosition.length; row++)
            for (int column = 0; column < currPosition[0].length; column++) {
                if (currPosition[row][column] != 0) {
                    currCoordinates.add(Pair.of(rowCenter + row, column + columnCenter));
                }
            }

        return currCoordinates;
    }

}
