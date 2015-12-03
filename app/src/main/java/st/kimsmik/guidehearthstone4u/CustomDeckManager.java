package st.kimsmik.guidehearthstone4u;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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

    private final String DECK_DATA_KEY = "CustomDeck";
    private final String DECK_NAME_LIST_KEY = "DeckList";

    private final String ARENA_DECK_DATA_KEY = "ArenaCustomDeck";
    private final String ARENA_DECK_NAME_LIST_KEY = "ArenaDeckList";

    public List<CustomeDeck> getList(){
        List<CustomeDeck> resList = new ArrayList<>();
        SharedPreferences sp = UserData.ins().getData(DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(DECK_NAME_LIST_KEY, new HashSet<String>());
        for(String deckName : deckList ){
            String info = sp.getString(deckName,"");
            CustomeDeck deck = parseDeck(deckName,info);
            resList.add(deck);
        }
        return resList;
    }

    public List<CustomeDeck> getArenaList(){
        List<CustomeDeck> resList = new ArrayList<>();
        SharedPreferences sp = UserData.ins().getData(ARENA_DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(ARENA_DECK_NAME_LIST_KEY, new HashSet<String>());
        for(String deckName : deckList ){
            String info = sp.getString(deckName,"");
            CustomeDeck deck = parseDeck(deckName,info);
            resList.add(deck);
        }
        return resList;
    }

    public int getArenaDeckCount(){
        SharedPreferences sp = UserData.ins().getData(ARENA_DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(ARENA_DECK_NAME_LIST_KEY, new HashSet<String>());
        return deckList.size();
    }

    private CustomeDeck parseDeck(String deckName,String data){
        CustomeDeck deck = new CustomeDeck();
        String[] infos = data.split(",");
        if(infos.length==0)
            return null;
        deck.name = deckName;
        int classIndex = Integer.parseInt(infos[0]);
        deck.deckClass = CardInfo.CARD_CLASS.values()[classIndex];
        for(int i=1; i<infos.length; i++){
            String[] strs = infos[i].split(";");
            if(strs.length < 3)
                continue;
            DeckCardInfo cardInfo = new DeckCardInfo();
            cardInfo.id = strs[0];
            cardInfo.cost = Integer.parseInt(strs[1]);
            cardInfo.num = Integer.parseInt(strs[2]);
            deck.cardList.add(cardInfo);
        }
        return deck;
    }

    private boolean checkIsLanZH(){
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        return language.equals("zh");
    }


    public List<CustomeDeck> loadRecommendedDecks(){
        boolean isZH = checkIsLanZH();
        List<CustomeDeck> resList = new ArrayList<>();
        try {
            URL url = new URL("http://django-kimsmik.rhcloud.com/get_recommended_decks/");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            InputStream input = con.getInputStream();
            BufferedReader buf = new BufferedReader(
                    new InputStreamReader(input));
            String resData = buf.readLine();
            JSONArray dataArray = new JSONArray(resData);
            for(int i=0; i<dataArray.length(); i++){
                JSONObject data = dataArray.getJSONObject(i);
                String name = "";
                if(isZH)
                    name = data.getString("name");
                else
                    name = data.getString("name_en");
                if(data.getString("author")!=null && !data.getString("author").equals("")){
                    name+="\n-"+data.getString("author");
                }
                CustomeDeck deck = parseDeck(name,data.getString("cards"));
                resList.add(deck);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resList;
    }

    public CustomeDeck getDeckByName(String name){
        List<CustomeDeck> list = getList();
        if(list.size() == 0)
            return null;
        for(CustomeDeck deck : list){
            if(deck.name.equals(name))
                return deck;
        }
        return null;
    }

    public boolean checkNameExisted(String name){
        SharedPreferences sp = UserData.ins().getData(DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(DECK_NAME_LIST_KEY, new HashSet<String>());
        if(deckList.contains(name))
            return true;
        return false;
    }

    public boolean checkArenaNameExisted(String name){
        SharedPreferences sp = UserData.ins().getData(ARENA_DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(ARENA_DECK_NAME_LIST_KEY, new HashSet<String>());
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
        if(!deckList.contains(deck.name))
            deckList.add(deck.name);
        String info = deck.deckClass.ordinal()+"";
        for(DeckCardInfo cardInfo : deck.cardList) {
            info += "," + cardInfo.id + ";" + cardInfo.cost + ";" + cardInfo.num;
        }
        sp.edit().putStringSet(DECK_NAME_LIST_KEY,deckList).putString(deck.name,info).commit();
        return true;
    }

    public boolean saveArenaDeck(CustomeDeck deck){
        SharedPreferences sp = UserData.ins().getData(ARENA_DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(ARENA_DECK_NAME_LIST_KEY, new HashSet<String>());
        if(!deckList.contains(deck.name))
            deckList.add(deck.name);
        String info = deck.deckClass.ordinal()+"";
        for(DeckCardInfo cardInfo : deck.cardList) {
            info += "," + cardInfo.id + ";" + cardInfo.cost + ";" + cardInfo.num;
        }
        sp.edit().putStringSet(ARENA_DECK_NAME_LIST_KEY,deckList).putString(deck.name,info).commit();
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

    public void deleteArenaDeck(String name){
        SharedPreferences sp = UserData.ins().getData(ARENA_DECK_DATA_KEY);
        Set<String> deckList = sp.getStringSet(ARENA_DECK_NAME_LIST_KEY, new HashSet<String>());
        if(!deckList.contains(name))
            return;
        deckList.remove(name);
        sp.edit().putStringSet(ARENA_DECK_NAME_LIST_KEY,deckList).remove(name).commit();
    }
}
