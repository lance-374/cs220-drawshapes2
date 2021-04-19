package knox.drawshapes;

import java.awt.Color;

public class Util
{
    public static String colorToString(Color color) {
        if (color == Color.RED) {
            return "RED";
        } else if (color == Color.BLUE) {
            return "BLUE";
        } else if (color==Color.GREEN) {
        	return "GREEN";
        }
        throw new UnsupportedOperationException("Unexpected color: "+color);
    }
    
    public static Color stringToColor(String color) {
        if (color.equals("RED")) {
            return Color.RED;
        } else if (color.equals("BLUE")) {
            return Color.BLUE;
        } else if (color.equals("GREEN")) {
        	return Color.GREEN;
        }
        throw new UnsupportedOperationException("Unexpected color: "+color);
    }
}
