package st.kimsmik.guidehearthstone4u;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chenk on 2015/12/3.
 */
public class ArenaAddCardDialog extends Dialog {
    ListView listView = null;
    OnAddDeckCardListener listener = null;
    public List<CardInfo> randomedCards = null;

    interface OnAddDeckCardListener{
        void onAddDeckCard(CardInfo card);
    }

    public void SetOnAddDeckCardListener(OnAddDeckCardListener l){
        listener = l;
    }

    public ArenaAddCardDialog(Context context,final CardInfo.CARD_CLASS cardClass,List<CardInfo> lastResCards) {
        super(context);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_card_atlas);

        listView = (ListView)findViewById(R.id.ListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cardId = view.getContentDescription().toString();
                if (cardId == null || cardId.equals(""))
                    return;
                CardInfo card = CardManager.ins().getCardById(cardId);
                if (card == null)
                    return;
                if (listener != null)
                    listener.onAddDeckCard(card);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String cardId = view.getContentDescription().toString();
                if (cardId == null || cardId.equals(""))
                    return false;
                CardInfo card = CardManager.ins().getCardById(cardId);
                if (card == null)
                    return false;
                CardImgDialog imgDialog = new CardImgDialog(getContext());
                imgDialog.setInfo(card);
                imgDialog.show();
                return true;
            }
        });
        randomedCards = lastResCards;
        if(randomedCards == null || randomedCards.size()<3)
            randomedCards = randomCards(cardClass);

        CardListAdapter cla = new CardListAdapter(context,randomedCards);
        listView.setAdapter(cla);

        Button filterBtn = (Button)findViewById(R.id.filterBtn);
        filterBtn.setVisibility(View.GONE);

        Spinner filterCostSpinner = (Spinner)findViewById(R.id.filterCostSpinner);
        filterCostSpinner.setVisibility(View.GONE);

        Spinner filterClassSpinner = (Spinner)findViewById(R.id.filterClassSpinner);
        filterClassSpinner.setVisibility(View.GONE);
    }

    public void release(){
        CardListAdapter cla = (CardListAdapter)listView.getAdapter();
        if(cla == null)
            return;
        cla.releaseImgView(listView);
    }

    public void setCardNum(int cardNum){
        setTitle("(" + cardNum + "/30)");
    }

    private List<CardInfo> randomCards(CardInfo.CARD_CLASS cardClass){
        List<CardInfo> cardList = CardManager.ins().getCardsByClass(cardClass);
        List<CardInfo> resCards = new ArrayList<>();

//        CardInfo firstCard = null;
//        while(firstCard == null){
//            firstCard = randomOneCard(cardList);
//        }
//        resCards.add(firstCard);
        CardInfo.CARD_RARITY rarity = null;
        Random random = new Random();
        int tempInt = Math.abs(random.nextInt())%30;
        if(tempInt == 0)
            rarity = CardInfo.CARD_RARITY.LEGENDARY;
        else if(tempInt <5)
            rarity = CardInfo.CARD_RARITY.EPIC;
        else if(tempInt <15)
            rarity = CardInfo.CARD_RARITY.RARE;
        else if(tempInt <25)
            rarity = CardInfo.CARD_RARITY.COMMON;
        else{
            rarity = CardInfo.CARD_RARITY.FREE;
        }

        List<CardInfo> newCardList = new ArrayList<>();
        for(CardInfo card : cardList){
            if(card.rarity.equals(rarity))
                newCardList.add(card);
        }

        while(resCards.size()<3){
            CardInfo card = randomOneCard(random,newCardList);
            if(!resCards.contains(card)){
                resCards.add(card);
            }
        }
        return resCards;
    }

    private CardInfo randomOneCard(Random random,List<CardInfo> cardList){
        int index = Math.abs(random.nextInt())%cardList.size();
        CardInfo cardInfo =  cardList.get(index);
        return cardInfo;
    }
}
