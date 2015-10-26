package st.kimsmik.hearthstone4u;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apmem.tools.layouts.FlowLayout;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Kim on 2015/9/25.
 */
public class FilterDialog extends Dialog {
    FlowLayout dialogLayout = null;
    Spinner filterContentSpinner = null;
    Spinner filterTypeSpinner = null;
    EditText searchText = null;

    HashMap<FILTER_TYPE,Integer> filterList = new HashMap<>();
    HashSet<Integer> attributeList = new HashSet<>();
    List<View> viewList = new ArrayList<>();

    OnSearchListener searchListener = null;

    public enum FILTER_TYPE{
        COST("cost"),
        ATK("attack"),
        HP("hp"),
        TYPE("cardType"),
        CLASS("cardClass"),
        RACE("cardRace"),
        RARITY("cardRarity"),
        SET("cardSet"),
        TALENT("cardTalent");

        private String name="";
        FILTER_TYPE(String n){
            this.name = n;
        }
        public String getName(){
            return this.name;
        }
    }

    public FilterDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_filter_dialog);

        dialogLayout = (FlowLayout)findViewById(R.id.dialogLayout);

        final List<String> filterStringList = new ArrayList<>();
        for(FILTER_TYPE temp : FILTER_TYPE.values()){
            filterStringList.add(Utility.getResStringByName(context,temp.getName()));
        }

        filterTypeSpinner = (Spinner)findViewById(R.id.filterTypeSpinner);
        filterContentSpinner = (Spinner)findViewById(R.id.filteContentrSpinner);
        ArrayAdapter sa = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, filterStringList);
        filterTypeSpinner.setAdapter(sa);
        filterTypeSpinner.setSelected(false);
        filterTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FILTER_TYPE type = FILTER_TYPE.values()[position];
                SetContentFilter(type);
                parent.setSelected(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchText = (EditText)findViewById(R.id.searchText);

        ImageView addFilterBtn = (ImageView)findViewById(R.id.addFilterButton);
        addFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFilter();
            }
        });

        Button searchBtn = (Button)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FilterDialog.this.searchListener != null){
                    List<CardInfo> cardInfoList = CardManager.ins().getAllCards();
                    for(Map.Entry<FILTER_TYPE,Integer> e : filterList.entrySet()){
                        cardInfoList = findMatchedCard(cardInfoList,e.getKey(),e.getValue());
                    }
                    for(Integer content : attributeList){
                        cardInfoList = findMatchedCard(cardInfoList,FILTER_TYPE.TALENT,content);
                    }
                    String searchStr = searchText.getText().toString();
                    if(searchStr!=null && !searchStr.equals(""))
                        cardInfoList = findMatchedCard(cardInfoList,searchStr);
                    FilterDialog.this.searchListener.onSearch(cardInfoList);
                }
                FilterDialog.this.dismiss();
            }
        });

        Button clearBtn = (Button)findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList.clear();
                attributeList.clear();
                for(View subView :viewList){
                    dialogLayout.removeView(subView);
                }
                viewList.clear();
            }
        });

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    public void setOnSearchListener(OnSearchListener l){
        this.searchListener = l;
    }

    private void AddFilter()
    {
        if(filterTypeSpinner.getSelectedItem()==null || filterContentSpinner.getSelectedItem()==null)
            return;
        Integer typePos = filterTypeSpinner.getSelectedItemPosition();
        FILTER_TYPE type = FILTER_TYPE.values()[typePos];
        Integer contentPos = filterContentSpinner.getSelectedItemPosition();
        if(type == FILTER_TYPE.TALENT){
            if(attributeList.contains(contentPos))
            {
                Toast.makeText(getContext(), R.string.add_same_filter_error, Toast.LENGTH_SHORT).show();
                return;
            }
            attributeList.add(contentPos);
        }
        else{
            if(filterList.containsKey(type))
            {
                Toast.makeText(getContext(), R.string.add_same_filter_error, Toast.LENGTH_SHORT).show();
                return;
            }
            filterList.put(type,contentPos);
        }

        String typeStr = (String)filterTypeSpinner.getSelectedItem();
        String contentStr = (String)filterContentSpinner.getSelectedItem();
        TextView tv = new TextView(getContext());
        tv.setTextSize(20);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        tv.setLayoutParams(lp);
        tv.setBackgroundColor(Color.parseColor("#503DACFF"));
        tv.setText(typeStr + "(" + contentStr + ")");

        LinearLayout ll = new LinearLayout(getContext());
        ll.addView(tv);
        ll.setTag(typePos + "," + contentPos);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFilter(v);
            }
        });
        viewList.add(ll);
        dialogLayout.addView(ll);
    }
    private void removeFilter(View v){
        String tag = (String)v.getTag();
        String[] strs = tag.split(",");
        if(strs.length != 2)
            return;
        Integer typePos = Integer.parseInt(strs[0]);
        FILTER_TYPE type = FILTER_TYPE.values()[typePos];
        Integer contentPos = Integer.parseInt(strs[1]);
        if(type == FILTER_TYPE.TALENT){
            if(attributeList.contains(contentPos))
            {
                attributeList.remove(contentPos);
            }
        }
        else{
            if(filterList.containsKey(type))
            {
                filterList.remove(type);
            }
        }
        viewList.remove(v);
        dialogLayout.removeView(v);
    }
    private void SetContentFilter(FILTER_TYPE type){
        //SubFilter view = new SubFilter(getContext());
        List<String> spinnerList = new ArrayList<>();
        switch (type)
        {
            case COST:
                for(Integer i=0;i<7;i++){
                    spinnerList.add(i.toString());
                }
                spinnerList.add("7+");
                break;
            case ATK:
                for(Integer i=0;i<7;i++){
                    spinnerList.add(i.toString());
                }
                spinnerList.add("7+");
                break;
            case HP:
                for(Integer i=0;i<7;i++){
                    spinnerList.add(i.toString());
                }
                spinnerList.add("7+");
                break;
            case TYPE:
                for(CardInfo.CARD_TYPE temp : CardInfo.CARD_TYPE.values()){
                    spinnerList.add(Utility.getResStringByName(getContext(),temp.getName()));
                }
                break;
            case CLASS:
                for(CardInfo.CARD_CLASS temp : CardInfo.CARD_CLASS.values()){
                    spinnerList.add(Utility.getResStringByName(getContext(),temp.getName()));
                }
                break;
            case RACE:
                for(CardInfo.CARD_RACE temp : CardInfo.CARD_RACE.values()){
                    spinnerList.add(Utility.getResStringByName(getContext(),temp.getName()));
                }
                break;
            case RARITY:
                for(CardInfo.CARD_RARITY temp : CardInfo.CARD_RARITY.values()){
                    spinnerList.add(Utility.getResStringByName(getContext(),temp.getName()));
                }
                break;
            case SET:
                for(CardInfo.CARD_SET temp : CardInfo.CARD_SET.values()){
                    spinnerList.add(Utility.getResStringByName(getContext(),temp.getName()));
                }
                break;
            case TALENT:
                for(CardInfo.CARD_ATTRIBUTE temp : CardInfo.CARD_ATTRIBUTE.values()){
                    spinnerList.add(Utility.getResStringByName(getContext(),temp.getName()));
                }
                break;
        }
        ArrayAdapter sa = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item, spinnerList);
        filterContentSpinner.setAdapter(sa);
        filterContentSpinner.setSelected(false);
    }

    private List<CardInfo> findMatchedCard(List<CardInfo> oriList,FILTER_TYPE type,Integer content){
        List<CardInfo> resList = new ArrayList<>();
        switch (type)
        {
            case COST:
                for(CardInfo card : oriList){
                    if((content >=7 && card.cost >= 7) || card.cost == content){
                        resList.add(card);
                    }
                }
                break;
            case ATK:
                for(CardInfo card : oriList){
                    if((content >=7 && card.atk >= 7) || card.atk == content){
                        resList.add(card);
                    }
                }
                break;
            case HP:
                for(CardInfo card : oriList){
                    if((content >=7 && card.hp >= 7) || card.hp == content){
                        resList.add(card);
                    }
                }
                break;
            case TYPE:
                for(CardInfo card : oriList){
                    if(card.type == CardInfo.CARD_TYPE.values()[content]){
                        resList.add(card);
                    }
                }
                break;
            case CLASS:
                for(CardInfo card : oriList){
                    if(card.cardClass == CardInfo.CARD_CLASS.values()[content]){
                        resList.add(card);
                    }
                }
                break;
            case RACE:
                for(CardInfo card : oriList){
                    if(card.race == CardInfo.CARD_RACE.values()[content]){
                        resList.add(card);
                    }
                }
                break;
            case RARITY:
                for(CardInfo card : oriList){
                    if(card.rarity == CardInfo.CARD_RARITY.values()[content]){
                        resList.add(card);
                    }
                }
                break;
            case SET:
                for(CardInfo card : oriList){
                    if(card.set == CardInfo.CARD_SET.values()[content]){
                        resList.add(card);
                    }
                }
                break;
            case TALENT:
                for(CardInfo card : oriList){
                    if(card.attributes.contains(CardInfo.CARD_ATTRIBUTE.values()[content])){
                        resList.add(card);
                    }
                }
                break;
        }
        return resList;
    }
    private List<CardInfo> findMatchedCard(List<CardInfo> oriList,String str){
        List<CardInfo> resList = new ArrayList<>();
        for(CardInfo card : oriList){
            if(card.name.contains(str)){
                resList.add(card);
            }
        }
        return resList;
    }
    public interface OnSearchListener{
        void onSearch(List<CardInfo> list);
    }
}
