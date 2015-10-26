package st.kimsmik.hearthstone4u;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

/**
 * Created by chenk on 2015/9/24.
 */
public class CardAtlasFragment extends Fragment implements IMenuFragment {

    ListView listView = null;
    FilterDialog fDialog = null;
    public CardAtlasFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_atlas, container, false);
        listView = (ListView)rootView.findViewById(R.id.ListView);
        CardListAdapter cla = new CardListAdapter(getActivity(),CardManager.ins().getAllCards());
        listView.setAdapter(cla);

        Button filterBtn = (Button)rootView.findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fDialog == null)
                    fDialog = new FilterDialog(getActivity());
                fDialog.setOnSearchListener(new FilterDialog.OnSearchListener() {
                    @Override
                    public void onSearch(List<CardInfo> list) {
                        CardListAdapter cla = new CardListAdapter(getActivity(),list);
                        listView.setAdapter(cla);
                    }
                });
                fDialog.show();
            }
        });
        return rootView;
    }

    @Override
    public int getTitleId() {
        return R.string.card_atlas;
    }
}
