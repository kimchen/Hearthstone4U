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

/**
 * Created by chenk on 2015/10/30.
 */
public class AddDeckCardDialog extends Dialog {
    List<CardInfo> nowCardList = new ArrayList<>();
    ListView listView = null;
    FilterDialog fDialog = null;
    OnAddDeckCardListener listener = null;
    Spinner filterCostSpinner = null;
    Spinner filterClassSpinner = null;
    int filterCost = -1;
    String filterClass = "";

    interface OnAddDeckCardListener{
        void onAddDeckCard(CardInfo card);
    }

    public void SetOnAddDeckCardListener(OnAddDeckCardListener l){
        listener = l;
    }

    public AddDeckCardDialog(Context context,final CardInfo.CARD_CLASS cardClass) {
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
        nowCardList = CardManager.ins().getCardsByClass(cardClass);
        CardListAdapter cla = new CardListAdapter(context,nowCardList);
        listView.setAdapter(cla);

        Button filterBtn = (Button)findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fDialog == null)
                    fDialog = new FilterDialog(getContext(),CardManager.ins().getCardsByClass(cardClass));
                fDialog.setOnSearchListener(new FilterDialog.OnSearchListener() {
                    @Override
                    public void onSearch(List<CardInfo> list) {
                        nowCardList = list;
                        updateCardList();
                    }
                });
                fDialog.show();
            }
        });

        filterCostSpinner = (Spinner)findViewById(R.id.filterCostSpinner);
        List<String> filterStringList = new ArrayList<>();
        filterStringList.add(getContext().getString(R.string.cost));
        filterStringList.add("  0");
        filterStringList.add("  1");
        filterStringList.add("  2");
        filterStringList.add("  3");
        filterStringList.add("  4");
        filterStringList.add("  5");
        filterStringList.add("  6");
        filterStringList.add("  7+");

        ArrayAdapter sa = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, filterStringList);
        filterCostSpinner.setAdapter(sa);
        filterCostSpinner.setSelected(false);
        filterCostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterCost = position - 1;
                updateCardList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filterClassSpinner = (Spinner)findViewById(R.id.filterClassSpinner);
        List<String> filterStringList2 = new ArrayList<>();
        filterStringList2.add(getContext().getString(R.string.cardClass));
        filterStringList2.add(Utility.getResStringByName(getContext(), CardInfo.CARD_CLASS.NORMAL.getName()));
        filterStringList2.add(Utility.getResStringByName(getContext(), cardClass.getName()));
        final List<CardInfo.CARD_CLASS>nowCardClassList = new ArrayList<>();
        nowCardClassList.add(CardInfo.CARD_CLASS.NORMAL);
        nowCardClassList.add(cardClass);

        ArrayAdapter sa2 = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, filterStringList2);
        filterClassSpinner.setAdapter(sa2);
        filterClassSpinner.setSelected(false);
        filterClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                    filterClass = nowCardClassList.get(position - 1).getName();
                updateCardList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setCardNum(int cardNum){
        setTitle("("+cardNum+"/30)");
    }

    private void updateCardList(){
        List<CardInfo> tempList;
        if(filterCost >= 0){
            tempList = new ArrayList<>();
            for(CardInfo info : nowCardList){
                if(info.cost == filterCost || (filterCost == 7 && info.cost >= 7)){
                    tempList.add(info);
                }
            }
        }else{
            tempList = nowCardList;
        }
        List<CardInfo> tempList2;
        if(!filterClass.equals("")){
            tempList2 = new ArrayList<>();
            for(CardInfo info : tempList){
                if(info.cardClass.getName().equals(filterClass)){
                    tempList2.add(info);
                }
            }

        }else{
            tempList2 = tempList;
        }

        CardListAdapter cla = new CardListAdapter(getContext(),tempList2);
        listView.setAdapter(cla);
    }
}
