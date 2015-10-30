package st.kimsmik.hearthstone4u;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenk on 2015/10/28.
 */
public class DeckCardListAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater mInflater = null;
    private List<DeckCardInfo> mList = new ArrayList<>();
    private View.OnClickListener deleteListener = null;

    public DeckCardListAdapter(Context c, List<DeckCardInfo> list){
        context = c;
        mInflater = LayoutInflater.from(c);
        mList = list;
    }

    public void setOnDeleteDeckListener(View.OnClickListener l){
        deleteListener = l;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutComponent lc = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.layout_deck_card_list, null);
            lc = new LayoutComponent((TextView)convertView.findViewById(R.id.nameView),
                    (TextView)convertView.findViewById(R.id.costView),
                    (TextView)convertView.findViewById(R.id.numView),
                    (ImageView)convertView.findViewById(R.id.deleteBtn));
            convertView.setTag(lc);
        }else{
            lc = (LayoutComponent)convertView.getTag();
        }

        DeckCardInfo deckCardInfo = mList.get(position);
        CardInfo cardInfo = CardManager.ins().getCardById(deckCardInfo.id);
        if(cardInfo!=null){
            lc.name.setText(cardInfo.name);
            lc.cost.setText("" + cardInfo.cost);
        }
        lc.num.setText("" + deckCardInfo.num);
        lc.deleteView.setTag(deckCardInfo.id);
        lc.deleteView.setOnClickListener(deleteListener);
        return convertView;
    }

    class LayoutComponent{
        public TextView name = null;
        public TextView cost = null;
        public TextView num = null;
        public ImageView deleteView = null;
        public LayoutComponent(TextView nameV,TextView costV,TextView numV,ImageView deleteV){
            this.name = nameV;
            this.cost = costV;
            this.num = numV;
            this.deleteView = deleteV;
        }
    }
}

