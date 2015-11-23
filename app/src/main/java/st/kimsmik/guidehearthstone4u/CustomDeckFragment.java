package st.kimsmik.guidehearthstone4u;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim on 2015/9/28.
 */
public class CustomDeckFragment extends Fragment implements IMenuFragment {

    protected ListView listView = null;
    protected Button addDeckBtn = null;
    protected Button addDeckCardBtn = null;
    protected Button doneDeckBtn = null;
    protected List<CustomeDeck> nowDeckList = null;
    protected LinearLayout editDeckSet = null;
    protected ImageView imageClass = null;
    protected List<ProgressBar> costBars = new ArrayList<>();
    protected List<TextView> costNums = new ArrayList<>();

    private Toast toastMsg = null;
    private boolean showingDeckCards = false;
    public CustomDeckFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_custom_deck, container, false);
        editDeckSet = (LinearLayout)rootView.findViewById(R.id.editDeckSet);
        listView = (ListView)rootView.findViewById(R.id.ListView);
        addDeckBtn = (Button)rootView.findViewById(R.id.addDeckBtn);
        addDeckCardBtn = (Button)rootView.findViewById(R.id.addDeckCardBtn);
        doneDeckBtn = (Button)rootView.findViewById(R.id.doneDeckBtn);
        imageClass = (ImageView)rootView.findViewById(R.id.imageClass);

        costBars = new ArrayList<>();
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar0));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar1));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar2));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar3));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar4));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar5));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar6));
        costBars.add((ProgressBar)rootView.findViewById(R.id.manabar7));

        costNums = new ArrayList<>();
        costNums.add((TextView)rootView.findViewById(R.id.manaNum0));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum1));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum2));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum3));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum4));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum5));
        costNums.add((TextView)rootView.findViewById(R.id.manaNum6));
        costNums.add((TextView) rootView.findViewById(R.id.manaNum7));

        showDeckList();
        return rootView;
    }

    protected void showDeckList(){
        MainActivity act = (MainActivity)getActivity();
        act.setTitle(getString(getTitleId()));

        nowDeckList = getDeckList();
        DeckListAdapter dla = new DeckListAdapter(getActivity(),nowDeckList);
        dla.setOnDeleteDeckListener(getOnDeleteDeckListener());

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
        showDeckListDetail();
        showingDeckCards = false;
    }

    protected void showDeckListDetail(){
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
        addDeckBtn.setVisibility(View.VISIBLE);
    }

    protected List<CustomeDeck> getDeckList(){
        return CustomDeckManager.ins().getList();
    }

    protected View.OnClickListener getOnDeleteDeckListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String deckName = (String)v.getTag();
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                String title = String.format(getString(R.string.delte_deck_alert), deckName);
                builder.setTitle(title).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(deckName == null || deckName.equals(""))
                            return;
                        CustomDeckManager.ins().deleteDeck(deckName);
                        showDeckList();
                    }
                }).setNegativeButton(R.string.no, null).show();
            }
        };
    }

    protected void showDeckCardList(final CustomeDeck deck){
        if(deck == null)
            return;
        DeckCardListAdapter dcla = new DeckCardListAdapter(getActivity(),deck.cardList);
        dcla.setOnDeleteDeckListener(getOnDeleteDeckCardListener(deck));

        listView.setAdapter(dcla);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= deck.cardList.size())
                    return;
                DeckCardInfo info = deck.cardList.get(position);
                CardInfo card = CardManager.ins().getCardById(info.id);
                CardImgDialog imgDialog = new CardImgDialog(getActivity());
                imgDialog.setInfo(card);
                imgDialog.show();
            }
        });

        int cardNum = deck.getCardNum();
        int maxNum = 0;
        for(int i=0; i<=7; i++){
            int persent = 0;
            int costCardNum = deck.getCardNumByCost(i);
            if(cardNum > 0)
                persent = (costCardNum*100)/cardNum;
            costBars.get(i).setProgress(persent);
            costNums.get(i).setText(""+costCardNum);
            if(costCardNum > maxNum)
                maxNum = costCardNum;
        }
        if(maxNum > 0 && maxNum < cardNum){
            for(ProgressBar bar : costBars)
                bar.setProgress(bar.getProgress() * cardNum / maxNum);
        }
        imageClass.setImageDrawable(Utility.getResDrawableByName(getActivity(), deck.deckClass.getName()));

        showDeckCardDetail(deck);
        showingDeckCards = true;
    }

    protected  void showDeckCardDetail(final CustomeDeck deck){
        int cardNum = deck.getCardNum();
        MainActivity act = (MainActivity)getActivity();
        act.setTitle(deck.name + "(" + cardNum + "/30)");

        editDeckSet.setVisibility(View.VISIBLE);
        addDeckBtn.setVisibility(View.GONE);
        addDeckCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddDeckCardDialog addDialog = new AddDeckCardDialog(getActivity(), deck.deckClass);
                addDialog.setCardNum(deck.getCardNum());
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
                            addDialog.setCardNum(deck.getCardNum());
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
        doneDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDeckManager.ins().saveDeck(deck);
                showDeckList();
            }
        });
    }

    protected View.OnClickListener getOnDeleteDeckCardListener(final CustomeDeck deck){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardId = (String) v.getTag();
                if (cardId == null || cardId.equals(""))
                    return;
                if (deck.deleteCard(cardId)) {
                    CardInfo card = CardManager.ins().getCardById(cardId);
                    String msg = String.format(getString(R.string.delete_deck_card_info), card.name);
                    if (toastMsg != null)
                        toastMsg.cancel();
                    toastMsg = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
                    toastMsg.show();
                    showDeckCardList(deck);
                }
            }
        };
    }

    @Override
    public int getTitleId() {
        return R.string.custom_deck;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(showingDeckCards && keyCode == KeyEvent.KEYCODE_BACK){
            showDeckList();
            return true;
        }
        return false;
    }
}
