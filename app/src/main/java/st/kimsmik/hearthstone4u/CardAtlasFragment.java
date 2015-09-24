package st.kimsmik.hearthstone4u;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by chenk on 2015/9/24.
 */
public class CardAtlasFragment extends Fragment implements IMenuFragment {

    public CardAtlasFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_atlas, container, false);
        ListView list = (ListView)rootView.findViewById(R.id.ListView);
        CardListAdapter cla = new CardListAdapter(getActivity(),CardManager.ins().getAllCards());
        list.setAdapter(cla);
        return rootView;
    }

    @Override
    public int getTitleId() {
        return R.string.card_atlas;
    }
}
