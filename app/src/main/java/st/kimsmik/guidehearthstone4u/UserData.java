package st.kimsmik.guidehearthstone4u;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kim on 2015/9/28.
 */
public class UserData {
    private static UserData mIns = null;
    private UserData(){}
    public static UserData ins(){
        if(mIns == null)
            mIns = new UserData();
        return mIns;
    }

    private Context mContext = null;

    public void init(Context c){
        mContext = c;
    }

    public SharedPreferences getData(String dataName) {
        SharedPreferences nowSp = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        return nowSp;
    }

}
