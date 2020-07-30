/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package shapes;

import java.util.ArrayList;
import java.util.List;
import com.sun.tools.javac.util.Pair;


public class ZShape extends AbstractShape {

    public ZShape(Pair<Integer, Integer> shapeCenter) {
        this.positions = new int[][][] { { { 0, 0, 0 }, { 0, 1, 1 }, { 1, 1, 0 } },
                { { 1, 0, 0 }, { 1, 1, 0 }, { 0, 1, 0 } } };
        currPosition = positions[0];
        setCoordinates(shapeCenter);
    }
}
