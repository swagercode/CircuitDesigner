package nodes;

import java.awt.*;
import java.util.Set;

public abstract class Gate extends Placeable{

    public abstract Point getBottomInputPoint();

    public abstract Point getOutputPoint();

    public abstract Set<Point> getInputPoints();

}
