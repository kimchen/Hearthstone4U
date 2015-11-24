package st.kimsmik.guidehearthstone4u;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by chenk on 2015/11/4.
 */
public class RecommandDeckFragment extends CustomDeckFragment implements IMenuFragment {
    private Date lastLoadTime = null;
    private List<CustomeDeck> deckList = new ArrayList<>();
    @Override
    protected void showDeckList() {
        editDeckSet.setVisibility(View.GONE);
        addDeckBtn.setVisibility(View.GONE);
        Date nowDate = new Date();
        if(lastLoadTime == null || (nowDate.getTime() > (lastLoadTime.getTime()+1000*60*5))){
            LoadDeckTask task = new LoadDeckTask();
            task.execute();
        }else{
            showDeckListNow();
        }
    }

    private void showDeckListNow(){
        super.showDeckList();
    }

    @Override
    protected void showDeckListDetail() {
        editDeckSet.setVisibility(View.GONE);
        addDeckBtn.setVisibility(View.VISIBLE);
        addDeckBtn.setText(R.string.refresh);
        addDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastLoadTime = null;
                showDeckList();
            }
        });
    }

    @Override
    protected List<CustomeDeck> getDeckList() {
        return deckList;
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

    private class LoadDeckTask extends AsyncTask<Void,Void,List<CustomeDeck>> {
        @Override
        protected List<CustomeDeck> doInBackground(Void... params) {
            List<CustomeDeck> resList = CustomDeckManager.ins().loadRecommendedDecks();
            return resList;
        }

        @Override
        protected void onPostExecute(List<CustomeDeck> customeDecks) {
            if(customeDecks.size() > 0){
                deckList = customeDecks;
                lastLoadTime = new Date();
            }else{
                Toast.makeText(getActivity(),R.string.load_deck_failed,Toast.LENGTH_LONG).show();
            }
            showDeckListNow();
        }
    }
}
