package st.kimsmik.hearthstone4u;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenk on 2015/11/4.
 */
public class RecommandDeckFragment extends Fragment implements IMenuFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity act = (MainActivity) getActivity();
        act.setTitle(getString(R.string.recommand_deck));

        View rootView = inflater.inflate(R.layout.fragment_recommand_deck, container, false);
        return rootView;
    }
    @Override
    public int getTitleId() {
        return R.string.recommand_deck;
    }
}
