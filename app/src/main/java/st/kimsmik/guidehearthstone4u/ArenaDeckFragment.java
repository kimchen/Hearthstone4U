package st.kimsmik.guidehearthstone4u;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenk on 2015/11/4.
 */
public class ArenaDeckFragment extends CustomDeckFragment implements IMenuFragment {

    private List<CustomeDeck> deckList = new ArrayList<>();
    private List<CardInfo> lastRandomedCards = null;
    @Override
    protected void showDeckListDetail() {
        editDeckSet.setVisibility(View.GONE);
        addDeckBtn.setVisibility(View.VISIBLE);
        addDeckBtn.setText(R.string.new_simulate);
        addDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewArenaSimulateDialog addDialog = new NewArenaSimulateDialog(getActivity());
                addDialog.show();
                addDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        NewArenaSimulateDialog addDialog = (NewArenaSimulateDialog) dialog;
                        if (addDialog.resDeck != null) {
                            showDeckCardList(addDialog.resDeck);
                        }
                    }
                });
            }
        });
        lastRandomedCards = null;
    }

    @Override
    protected List<CustomeDeck> getDeckList() {
        return CustomDeckManager.ins().getArenaList();
    }

    @Override
    protected View.OnClickListener getOnDeleteDeckListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String deckName = (String)v.getTag();
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                String title = String.format(getString(R.string.delte_deck_alert), deckName);
                builder.setTitle(title).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(deckName == null || deckName.equals(""))
                            return;
                        CustomDeckManager.ins().deleteArenaDeck(deckName);
                        showDeckList();
                    }
                }).setNegativeButton(R.string.no, null).show();
            }
        };
    }
    @Override
    protected View.OnClickListener getOnRenameDeckListener() {
        return null;
    }

    Handler addCardHandler = new Handler();
    private boolean isAddingCard = false;
    private void ArenaAddCard(final CustomeDeck deck){
        if(isAddingCard)
            return;
        isAddingCard = true;
        final ArenaAddCardDialog addDialog = new ArenaAddCardDialog(getActivity(), deck.deckClass,lastRandomedCards);
        lastRandomedCards = addDialog.randomedCards;
        addDialog.setCardNum(deck.getCardNum());
        addDialog.SetOnAddDeckCardListener(new ArenaAddCardDialog.OnAddDeckCardListener() {
            @Override
            public void onAddDeckCard(CardInfo card) {
                deck.forceAddCard(card);
                String msg = String.format(getString(R.string.add_deck_card_info), card.name);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                showDeckCardList(deck);
                addDialog.setCardNum(deck.getCardNum());
                addDialog.dismiss();
                lastRandomedCards = null;
                if (deck.getCardNum() >= 30) {
                    addDeckCardBtn.setVisibility(View.GONE);
                    doneDeckBtn.setVisibility(View.VISIBLE);
                } else {
                    addCardHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ArenaAddCard(deck);
                        }
                    }, 500);
                }
            }
        });
        addDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                addDialog.release();
                isAddingCard = false;
            }
        });
        addDialog.show();
    }
    @Override
    protected void showDeckCardDetail(final CustomeDeck deck) {
        final int cardNum = deck.getCardNum();
        MainActivity act = (MainActivity)getActivity();
        act.setTitle(deck.name + "(" + cardNum + "/30)");

        editDeckSet.setVisibility(View.VISIBLE);
        addDeckBtn.setVisibility(View.GONE);
        if(cardNum>=30){
            addDeckCardBtn.setVisibility(View.GONE);
            doneDeckBtn.setVisibility(View.VISIBLE);
        }else{
            addDeckCardBtn.setVisibility(View.VISIBLE);
            doneDeckBtn.setVisibility(View.GONE);
            addDeckCardBtn.setText(R.string.pick_card);
            addDeckCardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArenaAddCard(deck);
                }
            });
        }
        doneDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDeckManager.ins().saveArenaDeck(deck);
                showDeckList();
            }
        });
    }

    @Override
    protected View.OnClickListener getOnDeleteDeckCardListener(CustomeDeck deck) {
        return null;
    }

    @Override
    public int getTitleId() {
        return R.string.arena_simulator;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(showingDeckCards && keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.cancel_arena_alert).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDeckList();
                }
            }).setNegativeButton(R.string.no, null).show();
            return true;
        }
        return false;
    }
}
