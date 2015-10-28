package st.kimsmik.hearthstone4u;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    Button addDeckBtn = null;
    List<CustomeDeck> nowDeckList = null;

    public CustomDeckFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_custom_deck, container, false);
        listView = (ListView)rootView.findViewById(R.id.ListView);
        addDeckBtn = (Button)rootView.findViewById(R.id.addDeckBtn);
        showDeckList();

        return rootView;
    }
    private void showDeckList(){
        nowDeckList = CustomDeckManager.ins().getList();
        DeckListAdapter dla = new DeckListAdapter(getActivity(),nowDeckList);
        dla.setOnDeleteDeckListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deckName = (String)v.getTag();
                if(deckName == null || deckName.equals(""))
                    return;
                CustomDeckManager.ins().deleteDeck(deckName);
                showDeckList();
            }
        });

        listView.setAdapter(dla);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= nowDeckList.size())
                    return;
                CustomeDeck deck = nowDeckList.get(position);
                showDeckCardList(deck);
            }
        });
        addDeckBtn.setText(R.string.add_deck);
        addDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDeckDialog addDialog = new AddDeckDialog(getActivity());
                addDialog.show();
                addDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        AddDeckDialog addDialog = (AddDeckDialog) dialog;
                        if (addDialog.result) {
                            showDeckList();
                        }
                    }
                });
            }
        });
    }
    private void showDeckCardList(final CustomeDeck deck){
        if(deck == null)
            return;
        DeckCardListAdapter dcla = new DeckCardListAdapter(getActivity(),deck.cardList);
        dcla.setOnDeleteDeckListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardId = (String)v.getTag();
                if(cardId == null || cardId.equals(""))
                    return;
                if(deck.deleteCard(cardId)){
                    CustomDeckManager.ins().saveDeck(deck);
                    showDeckCardList(deck);
                }
            }
        });

        listView.setAdapter(dcla);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= deck.cardList.size())
                    return;
                DeckCardInfo info = deck.cardList.get(position);

            }
        });
        addDeckBtn.setText(R.string.add_card);
        addDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo
            }
        });
    }
    @Override
    public int getTitleId() {
        return R.string.custom_deck;
    }
}
