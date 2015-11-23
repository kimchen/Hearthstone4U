package st.kimsmik.guidehearthstone4u;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by chenk on 2015/10/27.
 */
public class CardImgDialog extends Dialog {
    ImageView cardImg = null;
    TextView cardDes = null;
    Context c = null;
    public CardImgDialog(Context context) {
        super(context);
        c = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_card_img);
        cardImg = (ImageView)findViewById(R.id.cardImg);
        cardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardImgDialog.this.cancel();
            }
        });
        cardDes = (TextView)findViewById(R.id.cardDes);
    }
    public void setInfo(CardInfo card){
        Drawable drawable = Utility.getResDrawableByName(c, card.id);
        cardImg.setImageDrawable(drawable);

        String description = Utility.getResStringByName(c,card.id+"_des");
        if(description != null && !description.equals("")){
            cardDes.setVisibility(View.VISIBLE);
            cardDes.setText(description);
        }else{
            cardDes.setVisibility(View.INVISIBLE);
        }
    }
}
