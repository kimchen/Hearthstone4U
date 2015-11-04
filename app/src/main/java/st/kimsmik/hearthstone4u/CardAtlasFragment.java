package st.kimsmik.hearthstone4u;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenk on 2015/9/24.
 */
public class CardAtlasFragment extends Fragment implements IMenuFragment {

    List<CardInfo> nowCardList = new ArrayList<>();
    ListView listView = null;
    FilterDialog fDialog = null;
    Spinner filterCostSpinner = null;
    Spinner filterClassSpinner = null;
    int filterCost = -1;
    int filterClass = -1;

    public CardAtlasFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity act = (MainActivity)getActivity();
        act.setTitle(getString(R.string.card_atlas));

        View rootView = inflater.inflate(R.layout.fragment_card_atlas, container, false);
        listView = (ListView)rootView.findViewById(R.id.ListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cardId = view.getContentDescription().toString();
                if (cardId == null || cardId.equals(""))
                    return;
                CardInfo card = CardManager.ins().getCardById(cardId);
                if (card == null)
                    return;
                CardImgDialog imgDialog = new CardImgDialog(getActivity());
                imgDialog.setImg(Utility.getResDrawableByName(getActivity(), card.id));
                imgDialog.show();
            }
        });
        nowCardList = CardManager.ins().getAllCards();
        updateCardList();

        Button filterBtn = (Button)rootView.findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fDialog == null)
                    fDialog = new FilterDialog(getActivity(), CardManager.ins().getAllCards());
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

        filterCostSpinner = (Spinner)rootView.findViewById(R.id.filterCostSpinner);
        List<String> filterStringList = new ArrayList<>();
        filterStringList.add(getString(R.string.cost));
        filterStringList.add("  0");
        filterStringList.add("  1");
        filterStringList.add("  2");
        filterStringList.add("  3");
        filterStringList.add("  4");
        filterStringList.add("  5");
        filterStringList.add("  6");
        filterStringList.add("  7+");

        ArrayAdapter sa = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, filterStringList);
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

        filterClassSpinner = (Spinner)rootView.findViewById(R.id.filterClassSpinner);
        List<String> filterStringList2 = new ArrayList<>();
        filterStringList2.add(getString(R.string.cardClass));
        for(CardInfo.CARD_CLASS temp : CardInfo.CARD_CLASS.values()){
            filterStringList2.add(Utility.getResStringByName(getActivity(), temp.getName()));
        }

        ArrayAdapter sa2 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, filterStringList2);
        filterClassSpinner.setAdapter(sa2);
        filterClassSpinner.setSelected(false);
        filterClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterClass = position - 1;
                updateCardList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
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
        if(filterClass >= 0){
            tempList2 = new ArrayList<>();
            CardInfo.CARD_CLASS cardCalss = CardInfo.CARD_CLASS.values()[filterClass];
            for(CardInfo info : tempList){
                if(info.cardClass.equals(cardCalss)){
                    tempList2.add(info);
                }
            }

        }else{
            tempList2 = tempList;
        }

        CardListAdapter cla = new CardListAdapter(getActivity(),tempList2);
        listView.setAdapter(cla);

    }
    @Override
    public int getTitleId() {
        return R.string.card_atlas;
    }
}
