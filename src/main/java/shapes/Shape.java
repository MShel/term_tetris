/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package shapes;

import java.util.List;

import com.sun.tools.javac.util.Pair;

public interface Shape {
    List<Pair<Integer, Integer>> getCoordinates();
    int[][] rotateLeft();
    int[][] rotateRight();
    List<Pair<Integer, Integer>> setCoordinates(Pair<Integer, Integer> shapeCenter);
}
