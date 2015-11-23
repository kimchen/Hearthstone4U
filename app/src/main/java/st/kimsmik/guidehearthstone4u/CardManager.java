package st.kimsmik.guidehearthstone4u;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by chenk on 2015/9/23.
 */
public class CardManager {

    private static CardManager mIns = null;
    private CardManager(){}
    public static CardManager ins(){
        if(mIns == null){
            mIns = new CardManager();
        }
        return mIns;
    }
    private Hashtable<String,CardInfo> cardTable = new Hashtable<>();
    //private List<CardInfo>  cardList = new ArrayList<>();
    private Hashtable<String,List<CardInfo>> classCardTable = new Hashtable<>();
    //private List<CardInfo> cardList = new ArrayList<>();

    public void initCards(Context c){
        cardTable = new Hashtable<>();
        classCardTable = new Hashtable<>();
        XmlResourceParser parser = c.getResources().getXml(R.xml.card_info);
        try{
            while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if(parser.getEventType() == XmlResourceParser.START_TAG){
                    if(parser.getName().equals("Card")){
                        String id = parser.getAttributeValue(null,"id");
                        String name = "";
                        if(id != null)
                            name = Utility.getResStringByName(c, id);
                        String cardClass = parser.getAttributeValue(null,"class");
                        String type = parser.getAttributeValue(null, "type");
                        String rarity = parser.getAttributeValue(null, "rarity");
                        String race = parser.getAttributeValue(null, "race");
                        String set = parser.getAttributeValue(null, "set");
                        int cost = parser.getAttributeIntValue(null, "cost", 0);
                        int atk = parser.getAttributeIntValue(null,"atk",0);
                        int hp = parser.getAttributeIntValue(null, "hp", 0);
                        String attributes = parser.getAttributeValue(null, "attributes");

                        CardInfoBuilder builder = new CardInfoBuilder();
                        builder.setCardId(id).setCardName(name).setCardClass(cardClass).setCardType(type).setCardRarity(rarity).setCardSet(set)
                                .setCardRace(race).setCardCost(cost).setCardAtk(atk).setCardHp(hp).setCardAttributes(attributes);
                        CardInfo info = builder.build();
                        cardTable.put(id, info);
                        if(classCardTable.containsKey(cardClass)){
                            classCardTable.get(cardClass).add(info);
                        }else{
                            List<CardInfo> list = new ArrayList<>();
                            list.add(info);
                            classCardTable.put(cardClass,list);
                        }
                        //cardList.add(info);
                    }
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(CardInfo.CARD_CLASS cardClass : CardInfo.CARD_CLASS.values()){
            Collections.sort(classCardTable.get(cardClass.getName()), new Comparator<CardInfo>() {
                @Override
                public int compare(CardInfo lhs, CardInfo rhs) {
                    return lhs.cost - rhs.cost;
                }
            });
        }

    }

    public CardInfo getCardById(String id){
        if(this.cardTable.size() == 0 || !this.cardTable.containsKey(id))
            return null;
        return this.cardTable.get(id);
    }
    public List<CardInfo> getAllCards(){
        List<CardInfo> resList = new ArrayList<CardInfo>();
        for(CardInfo.CARD_CLASS cardClass : CardInfo.CARD_CLASS.values()){
            resList.addAll(classCardTable.get(cardClass.getName()));
        }
        return resList;
    }
    public List<CardInfo> getCardsByClass(CardInfo.CARD_CLASS cardClass){
        List<CardInfo> resList = new ArrayList<>();
        resList.addAll(classCardTable.get(cardClass.getName()));
        resList.addAll(classCardTable.get(CardInfo.CARD_CLASS.NORMAL.getName()));
        return resList;
    }
}
