package st.kimsmik.guidehearthstone4u;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chenk on 2015/11/4.
 */
public class RecommandDeckFragment extends CustomDeckFragment implements IMenuFragment {

    @Override
    protected void showDeckListDetail() {
        editDeckSet.setVisibility(View.GONE);
        addDeckBtn.setVisibility(View.GONE);
    }

    @Override
    protected List<CustomeDeck> getDeckList() {
        CustomDeckManager.ins().loadRecommendedDecks();
        return CustomDeckManager.ins().getRecommendedDecks();
    }

    @Override
    protected View.OnClickListener getOnDeleteDeckListener() {
        return null;
    }

    @Override
    protected void showDeckCardDetail(CustomeDeck deck) {
        MainActivity act = (MainActivity)getActivity();
        act.setTitle(deck.name);
        editDeckSet.setVisibility(View.VISIBLE);
        addDeckBtn.setVisibility(View.GONE);
        addDeckCardBtn.setVisibility(View.GONE);
        doneDeckBtn.setVisibility(View.GONE);
    }

    @Override
    protected View.OnClickListener getOnDeleteDeckCardListener(CustomeDeck deck) {
        return null;
    }

    @Override
    public int getTitleId() {
        return R.string.recommand_deck;
    }
}
