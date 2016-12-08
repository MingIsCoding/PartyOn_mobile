package edu.sjsu.cmpe.partyon.utilities;

import android.graphics.Color;

/**
 * Created by Ming on 12/8/16.
 */

public class BadgeTool {
    public static int getLevelColor(int points){
        if(points < 100){
            return Color.WHITE;
        }else if (points >= 100 && points < 200){
            return Color.GRAY;
        }else if (points >= 200 && points < 300){
            return Color.GREEN;
        }else if (points >= 300 && points < 400){
            return Color.BLUE;
        }else if (points >= 400){
            return Color.RED;
        }
        return Color.LTGRAY;
    }
}
