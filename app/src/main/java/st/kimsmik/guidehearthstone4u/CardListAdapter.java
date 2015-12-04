package st.kimsmik.guidehearthstone4u;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenk on 2015/9/23.
 */
public class CardListAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater mInflater = null;
    List<CardInfo> mList = new ArrayList<>();

    public CardListAdapter(Context c, List<CardInfo> list){
        context = c;
        mInflater = LayoutInflater.from(c);
        mList = list;
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
            convertView = mInflater.inflate(R.layout.layout_card_list_info, null);
            lc = new LayoutComponent((ImageView)convertView.findViewById(R.id.imageView),
                    (TextView)convertView.findViewById(R.id.nameView),
                    (TextView)convertView.findViewById(R.id.rarityView),
                    (TextView)convertView.findViewById(R.id.typeView),
                    (TextView)convertView.findViewById(R.id.raceView),
                    (TextView)convertView.findViewById(R.id.setView),
                    (TextView)convertView.findViewById(R.id.attributeView));
            convertView.setTag(lc);
        }else{
            lc = (LayoutComponent)convertView.getTag();
        }

        CardInfo info = mList.get(position);
        lc.name.setText(info.name);
        lc.name.setTextColor(info.getRarityColor());
        lc.RunImageTask(info.id);

        lc.rarity.setText(Utility.getResStringByName(context, info.rarity.getName()));
        lc.race.setText(Utility.getResStringByName(context, info.race.getName()));
        if(lc.race.getText().equals(""))
            lc.race.setVisibility(View.GONE);
        else
            lc.race.setVisibility(View.VISIBLE);
        lc.type.setText(Utility.getResStringByName(context, info.type.getName()));
        lc.set.setText(Utility.getResStringByName(context, info.set.getName()));
        lc.set.setTextColor(info.getSetColor());

        String attributes = "";
        for(CardInfo.CARD_ATTRIBUTE attribute : info.attributes )
        {
            attributes+= "[" + Utility.getResStringByName(context, attribute.getName()) + "]";
        }
        lc.attribute.setText(attributes);
        convertView.setContentDescription(info.id);
        return convertView;
    }

    public void releaseImgView(ViewGroup parent){
        for(int i=0; i<parent.getChildCount();i++){
            View view = parent.getChildAt(i);
            if(view == null)
                continue;
            LayoutComponent lc = (LayoutComponent)view.getTag();
            if(lc == null)
                continue;
            BitmapDrawable drawable = (BitmapDrawable)lc.img.getDrawable();
            if(drawable != null && drawable.getBitmap() != null)
                drawable.getBitmap().recycle();
        }
    }

    class LayoutComponent{
        public ImageView img = null;
        public TextView name = null;
        public TextView rarity = null;
        public TextView type = null;
        public TextView race = null;
        public TextView set = null;
        public TextView attribute = null;
        public ImageTask task = null;

        public LayoutComponent(ImageView imgV,TextView nameV,TextView rarityV,TextView typeV,TextView raceView,TextView setV,TextView attributeV){
            this.name = nameV;
            this.img = imgV;
            this.rarity = rarityV;
            this.type = typeV;
            this.race = raceView;
            this.set = setV;
            this.attribute = attributeV;
        }

        public void RunImageTask(String id){
            BitmapDrawable drawable = (BitmapDrawable)img.getDrawable();
            if(drawable != null && drawable.getBitmap() != null)
                drawable.getBitmap().recycle();
            img.setImageDrawable(null);
            if(task != null){
                task.cancel(true);
                task = null;
            }
            task = new ImageTask(this,id);
            task.execute();
        }
    }

    private class ImageTask extends AsyncTask<Void,Void,Bitmap> {

        private LayoutComponent mLC = null;
        private String cardId = "";
        public ImageTask(LayoutComponent lc,String id){
            mLC = lc;
            cardId = id;
        };
        @Override
        protected Bitmap doInBackground(Void... params) {
//            Drawable drawable = Utility.getResDrawableByName(context, cardId);
//            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
            return Utility.getBitmapByName(context, cardId);
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            mLC.img.setImageDrawable(new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 4, bmp.getHeight() / 4, true)));
        }
    }
}
