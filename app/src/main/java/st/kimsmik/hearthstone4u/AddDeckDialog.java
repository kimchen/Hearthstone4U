package st.kimsmik.hearthstone4u;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kim on 2015/9/28.
 */
public class AddDeckDialog extends Dialog {
    private int[] classBtns = {R.id.imageClass1,R.id.imageClass2,R.id.imageClass3,
            R.id.imageClass4,R.id.imageClass5,R.id.imageClass6,
            R.id.imageClass7,R.id.imageClass8,R.id.imageClass9};
    private EditText nameText = null;
    public boolean result = false;

    public AddDeckDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_deck);

        nameText = (EditText)findViewById(R.id.editText);
        nameText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        nameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE ){
                    InputMethodManager imm = (InputMethodManager)(getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                    imm.hideSoftInputFromWindow(nameText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        for(int i=0; i<classBtns.length; i++){
            View btn = findViewById(classBtns[i]);
            btn.setTag(CardInfo.CARD_CLASS.values()[i]);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String deckName = nameText.getText().toString();
                    if (deckName == null || deckName.equals("")) {
                        Toast.makeText(getContext(), R.string.empty_deck_name_error, Toast.LENGTH_LONG).show();
                        return;
                    } else if (CustomDeckManager.ins().checkNameExisted(deckName)) {
                        Toast.makeText(getContext(), R.string.exist_deck_name_error, Toast.LENGTH_LONG).show();
                        return;
                    }
                    CustomeDeck deck = new CustomeDeck();
                    deck.name = deckName;
                    deck.deckClass = (CardInfo.CARD_CLASS)v.getTag();
                    result = CustomDeckManager.ins().saveDeck(deck);
                    AddDeckDialog.this.dismiss();
                }
            });
        }

    }
}
