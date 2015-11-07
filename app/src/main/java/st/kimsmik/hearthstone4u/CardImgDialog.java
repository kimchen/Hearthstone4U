package st.kimsmik.hearthstone4u;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
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
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_card_img);
        cardImg = (ImageView)findViewById(R.id.cardImg);
        cardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardImgDialog.this.cancel();
            }
        });
    }
    public void setImg(Drawable drawable){
        cardImg.setImageDrawable(drawable);
    }
}
