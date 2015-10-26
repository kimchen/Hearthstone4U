package st.kimsmik.hearthstone4u;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kim on 2015/9/28.
 */
public class CustomDeckFragment extends Fragment implements IMenuFragment {

    ListView listView = null;
    public CustomDeckFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_custom_deck, container, false);
        listView = (ListView)rootView.findViewById(R.id.ListView);
        showList();

        Button addDeckBtn = (Button)rootView.findViewById(R.id.addDeckBtn);
        addDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDeckDialog addDialog = new AddDeckDialog(getActivity());
                addDialog.show();
                addDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        AddDeckDialog addDialog = (AddDeckDialog)dialog;
                        if(addDialog.result){
                            showList();
                        }
                    }
                });
            }
        });
        return rootView;
    }
    private void showList(){
        List<CustomeDeck> deckList = CustomDeckManager.ins().getList();
        DeckListAdapter dla = new DeckListAdapter(getActivity(),deckList);
        dla.setOnDeleteDeckListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deckName = (String)v.getTag();
                if(deckName == null || deckName.equals(""))
                    return;
                CustomDeckManager.ins().deleteDeck(deckName);
                showList();
            }
        });
        listView.setAdapter(dla);
    }
    @Override
    public int getTitleId() {
        return R.string.custom_deck;
    }
}
