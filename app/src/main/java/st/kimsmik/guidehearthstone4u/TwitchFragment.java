package st.kimsmik.guidehearthstone4u;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;

/**
 * Created by chenk on 2016/1/19.
 */
public class TwitchFragment extends Fragment implements IMenuFragment {
    private static String TWITCH_CLIENT_ID = "ouiodiyhxhjprqiv0nq9k7lkqyiky73";
    @Override
    public int getTitleId() {
        return 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
