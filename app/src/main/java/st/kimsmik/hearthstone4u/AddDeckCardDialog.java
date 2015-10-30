package st.kimsmik.hearthstone4u;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

    interface OnAddDeckCardListener{
        void onAddDeckCard(CardInfo card);
    }

    public void SetOnAddDeckCardListener(OnAddDeckCardListener l){
        listener = l;
    }

    public AddDeckCardDialog(Context context,final CardInfo.CARD_CLASS cardClass) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_card_atlas);

        listView = (ListView)findViewById(R.id.ListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= nowCardList.size())
                    return;
                CardInfo card = nowCardList.get(position);
                if (card == null)
                    return;
                if (listener != null)
                    listener.onAddDeckCard(card);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= nowCardList.size())
                    return true;
                CardInfo card = nowCardList.get(position);
                if (card == null)
                    return true;
                CardImgDialog imgDialog = new CardImgDialog(getContext());
                imgDialog.setImg(Utility.getResDrawableByName(getContext(),card.id));
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
                        CardListAdapter cla = new CardListAdapter(getContext(), nowCardList);
                        listView.setAdapter(cla);
                    }
                });
                fDialog.show();
            }
        });
    }
}
