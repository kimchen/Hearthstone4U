package st.kimsmik.guidehearthstone4u;

import android.view.KeyEvent;

/**
 * Created by chenk on 2015/9/24.
 */
public interface IMenuFragment {
    int getTitleId();
    boolean onKeyDown(int keyCode, KeyEvent event);
}
