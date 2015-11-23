package st.kimsmik.guidehearthstone4u;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenk on 2015/11/4.
 */
public class ArenaFragment extends Fragment implements IMenuFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity act = (MainActivity) getActivity();
        act.setTitle(getString(R.string.arena_simulator));

        View rootView = inflater.inflate(R.layout.fragment_arena, container, false);
        return rootView;
    }
    @Override
    public int getTitleId() {
        return R.string.arena_simulator;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
