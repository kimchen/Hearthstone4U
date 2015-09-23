package st.kimsmik.hearthstone4u;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by chenk on 2015/9/23.
 */
public class Utility {
    public static String getResStringByName(Context c,String name){
        int id = c.getResources().getIdentifier(name,"string",c.getPackageName());
        return id<=0?"":c.getResources().getString(id);
    }

    public static Drawable getResDrawableByName(Context c,String name){
        int id = c.getResources().getIdentifier(name,"drawable",c.getPackageName());
        return id<=0?null:c.getResources().getDrawable(id);
    }
}
