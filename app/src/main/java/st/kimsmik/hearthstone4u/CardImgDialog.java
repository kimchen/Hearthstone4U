package st.kimsmik.hearthstone4u;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by chenk on 2015/10/27.
 */
public class CardImgDialog extends Dialog {
    ImageView cardImg = null;
    public CardImgDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_card_img);
        cardImg = (ImageView)findViewById(R.id.cardImg);
    }
    public void setImg(Drawable drawable){
        cardImg.setImageDrawable(drawable);
    }
}
