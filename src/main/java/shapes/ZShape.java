/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package shapes;

import org.apache.commons.lang3.tuple.Pair;


public class ZShape extends AbstractShape {


    public ZShape(Pair<Integer, Integer> shapeCenter) {
        this.positions = new int[][][] { {
            { 0, 1, 1 },
            { 1, 1, 0 } }, {
            /////
            { 1, 0, 0 },
            { 1, 1, 0 },
            { 0, 1, 0 } } };
        currPosition = positions[0];
        setCoordinates(shapeCenter);
    }

    @Override
    public String getColor() {
        return "\u001b[41;1m";
    }
}
