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
 * Created by Kim on 2015/9/28.
 */
public class DeckListAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater mInflater = null;
    List<CustomeDeck> mList = new ArrayList<>();
    private View.OnClickListener deleteListener = null;

    public DeckListAdapter(Context c, List<CustomeDeck> list){
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
            convertView = mInflater.inflate(R.layout.layout_deck_list_info, null);
            lc = new LayoutComponent((ImageView)convertView.findViewById(R.id.imageView),
                    (TextView)convertView.findViewById(R.id.nameView),
                    (TextView)convertView.findViewById(R.id.numberView),
                    (ImageView)convertView.findViewById(R.id.cancelBtn));
            convertView.setTag(lc);
        }else{
            lc = (LayoutComponent)convertView.getTag();
        }

        CustomeDeck info = mList.get(position);
        lc.name.setText(info.name);
        lc.img.setImageDrawable(Utility.getResDrawableByName(context, info.deckClass.getName()));
        lc.number.setText("(" + info.cardIdList.size() + "/30)");
        lc.deleteView.setTag(info.name);
        lc.deleteView.setOnClickListener(deleteListener);

        return convertView;
    }

    class LayoutComponent{
        public ImageView img = null;
        public TextView name = null;
        public TextView number = null;
        public ImageView deleteView = null;
        public LayoutComponent(ImageView imgV,TextView nameV,TextView numberV,ImageView deleteV){
            this.name = nameV;
            this.img = imgV;
            this.number = numberV;
            this.deleteView = deleteV;
        }
    }
}
