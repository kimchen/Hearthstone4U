package st.kimsmik.guidehearthstone4u;

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
    private View.OnClickListener renameListener = null;

    public DeckListAdapter(Context c, List<CustomeDeck> list){
        context = c;
        mInflater = LayoutInflater.from(c);
        mList = list;
    }

    public void setOnDeleteDeckListener(View.OnClickListener l){
        deleteListener = l;
    }
    public void setOnRenameDeckListener(View.OnClickListener l){
        renameListener = l;
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
                    (ImageView)convertView.findViewById(R.id.cancelBtn),
                    (ImageView)convertView.findViewById(R.id.renameBtn));
            convertView.setTag(lc);
        }else{
            lc = (LayoutComponent)convertView.getTag();
        }

        CustomeDeck info = mList.get(position);
        lc.name.setText(info.name);
        lc.img.setImageDrawable(Utility.getResDrawableByName(context, info.deckClass.getName()));
        lc.number.setText("(" + info.getCardNum() + "/30)");
        if(deleteListener != null) {
            lc.deleteView.setVisibility(View.VISIBLE);
            lc.deleteView.setTag(info.name);
            lc.deleteView.setOnClickListener(deleteListener);
        }else{
            lc.deleteView.setVisibility(View.GONE);
        }
        if(renameListener != null){
            lc.renameView.setVisibility(View.VISIBLE);
            lc.renameView.setTag(info.name);
            lc.renameView.setOnClickListener(renameListener);
        }else{
            lc.renameView.setVisibility(View.GONE);
        }

        return convertView;
    }

    class LayoutComponent{
        public ImageView img = null;
        public TextView name = null;
        public TextView number = null;
        public ImageView deleteView = null;
        public ImageView renameView = null;
        public LayoutComponent(ImageView imgV,TextView nameV,TextView numberV,ImageView deleteV,ImageView renameV){
            this.name = nameV;
            this.img = imgV;
            this.number = numberV;
            this.deleteView = deleteV;
            this.renameView = renameV;
        }
    }
}
