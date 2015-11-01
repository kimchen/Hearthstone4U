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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kim on 2015/9/28.
 */
public class CustomDeckFragment extends Fragment implements IMenuFragment {

    ListView listView = null;
    Button addDeckBtn = null;
    Button saveDeckBtn = null;
    Button cancelDeckBtn = null;
    List<CustomeDeck> nowDeckList = null;
    LinearLayout editDeckSet = null;
    ImageView imageClass = null;
    List<ProgressBar> costBars = new ArrayList<>();
    List<TextView> costNums = new ArrayList<>();

    Toast toastMsg = null;
    public CustomDeckFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_custom_deck, container, false);
        editDeckSet = (LinearLayout)rootView.findViewById(R.id.editDeckSet);
        listView = (ListView)rootView.findViewById(R.id.ListView);
        addDeckBtn = (Button)rootView.findViewById(R.id.addDeckBtn);
        saveDeckBtn = (Button)rootView.findViewById(R.id.saveDeckBtn);
        cancelDeckBtn = (Button)rootView.findViewById(R.id.cancelDeckBtn);
        imageClass = (ImageView)rootView.findViewById(R.id.imageClass);

        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar0));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar1));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar2));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar3));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar4));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar5));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar6));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar7));

        costNums.add((TextView)rootView.findViewById(R.id.manaNum0));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum1));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum2));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum3));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum4));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum5));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum6));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum7));


        showDeckList();

        return rootView;
    }
    private void showDeckList(){
        MainActivity act = (MainActivity)getActivity();
        act.setTitle(getString(R.string.custom_deck));

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
        editDeckSet.setVisibility(View.GONE);
    }
    private void showDeckCardList(final CustomeDeck deck){
        if(deck == null)
            return;
        int cardNum = deck.getCardNum();
        MainActivity act = (MainActivity)getActivity();
        act.setTitle(deck.name + "(" + cardNum + ")");

        DeckCardListAdapter dcla = new DeckCardListAdapter(getActivity(),deck.cardList);
        dcla.setOnDeleteDeckListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardId = (String) v.getTag();
                if (cardId == null || cardId.equals(""))
                    return;
                if (deck.deleteCard(cardId)) {
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
                CardInfo card = CardManager.ins().getCardById(info.id);
                CardImgDialog imgDialog = new CardImgDialog(getActivity());
                imgDialog.setImg(Utility.getResDrawableByName(getActivity(), card.id));
                imgDialog.show();
            }
        });
        addDeckBtn.setText(R.string.add_card);
        addDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDeckCardDialog addDialog = new AddDeckCardDialog(getActivity(), deck.deckClass);
                addDialog.SetOnAddDeckCardListener(new AddDeckCardDialog.OnAddDeckCardListener() {
                    @Override
                    public void onAddDeckCard(CardInfo card) {
                        if (deck.addCard(card)) {
                            String msg = String.format(getString(R.string.add_deck_card_info), card.name);
                            if (toastMsg != null)
                                toastMsg.cancel();
                            toastMsg = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
                            toastMsg.show();
                            showDeckCardList(deck);
                        } else {
                            if (toastMsg != null)
                                toastMsg.cancel();
                            toastMsg = Toast.makeText(getActivity(), R.string.add_deck_card_failed, Toast.LENGTH_SHORT);
                            toastMsg.show();
                        }
                    }
                });
                addDialog.show();
            }
        });

        editDeckSet.setVisibility(View.VISIBLE);
        saveDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDeckManager.ins().saveDeck(deck);
                showDeckList();
            }
        });
        cancelDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.cancel_deck_alert).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDeckList();
                    }
                }).setNegativeButton(R.string.no, null).show();
            }
        });

        imageClass.setImageDrawable(Utility.getResDrawableByName(getActivity(), deck.deckClass.getName()));

        for(int i=0; i<=7; i++){
            int persent = 0;
            int costCardNum = deck.getCardNumByCost(i);
            if(cardNum > 0)
                persent = (costCardNum*100)/cardNum;
            costBars.get(i).setProgress(persent);
            costNums.get(i).setText(""+costCardNum);
        }
    }
    @Override
    public int getTitleId() {
        return R.string.custom_deck;
    }
}
