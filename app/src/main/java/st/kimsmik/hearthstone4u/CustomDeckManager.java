package st.kimsmik.hearthstone4u;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kim on 2015/9/28.
 */
public class CustomDeckManager {
    private static CustomDeckManager mIns = null;
    private CustomDeckManager(){}
    public static CustomDeckManager ins(){
        if(mIns == null)
            mIns = new CustomDeckManager();
        return mIns;
    }

    private HashMap<String,CustomeDeck> deckMap = new HashMap<>();
    private final String DECK_DATA_KEY = "CustomDeck";
    private final String DECK_NAME_LIST_KEY = "DeckList";

    public List<CustomeDeck> getList(){
        List<CustomeDeck> resList = new ArrayList<>();
        SharedPreferences sp = UserData.ins().getData(DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(DECK_NAME_LIST_KEY, new HashSet<String>());
        for(String deckName : deckList ){
            CustomeDeck deck = new CustomeDeck();
            String info = sp.getString(deckName,"");
            String[] infos = info.split(",");
            if(infos.length==0)
                continue;
            deck.name = deckName;
            int classIndex = Integer.parseInt(infos[0]);
            deck.deckClass = CardInfo.CARD_CLASS.values()[classIndex];
            for(int i=1; i<infos.length; i++){
                deck.cardIdList.add(infos[i]);
            }
            resList.add(deck);
        }
        return resList;
    }

    public boolean checkNameExisted(String name){
        SharedPreferences sp = UserData.ins().getData(DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(DECK_NAME_LIST_KEY, new HashSet<String>());
        if(deckList.contains(name))
            return true;
        return false;
    }

    public CustomeDeck addNewDeck(CardInfo.CARD_CLASS deckClass,String name){
        SharedPreferences sp = UserData.ins().getData(DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(DECK_NAME_LIST_KEY, new HashSet<String>());
        if(deckList.contains(name))
            return null;
        CustomeDeck deck = new CustomeDeck();
        deck.deckClass = deckClass;

        deckList.add(name);
        sp.edit().putStringSet(DECK_NAME_LIST_KEY,deckList).putString(name,deckClass.ordinal()+"").commit();
        return deck;
    }

    public boolean saveDeck(CustomeDeck deck){
        SharedPreferences sp = UserData.ins().getData(DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(DECK_NAME_LIST_KEY, new HashSet<String>());
        if(deckList.contains(deck.name))
            return false;
        String info = deck.deckClass.ordinal()+"";
        for(String cardId : deck.cardIdList) {
            info += "," + cardId;
        }
        deckList.add(deck.name);
        sp.edit().putStringSet(DECK_NAME_LIST_KEY,deckList).putString(deck.name,info).commit();
        return true;
    }

    public void deleteDeck(String name){
        SharedPreferences sp = UserData.ins().getData(DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(DECK_NAME_LIST_KEY, new HashSet<String>());
        if(!deckList.contains(name))
            return;
        deckList.remove(name);
        sp.edit().putStringSet(DECK_NAME_LIST_KEY,deckList).remove(name).commit();
    }
}
