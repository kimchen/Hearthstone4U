package st.kimsmik.hearthstone4u;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
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

    private List<CardInfo> cardList = new ArrayList<>();

    public void initCards(Context c){
        XmlResourceParser parser = c.getResources().getXml(R.xml.card_info);
        try{
            while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if(parser.getEventType() == XmlResourceParser.START_TAG){
                    if(parser.getName().equals("Card")){
                        String id = parser.getAttributeValue(null,"id");
                        String cardClass = parser.getAttributeValue(null,"class");
                        String type = parser.getAttributeValue(null, "type");
                        String rarity = parser.getAttributeValue(null, "rarity");
                        String set = parser.getAttributeValue(null, "set");
                        int cost = parser.getAttributeIntValue(null, "cost", 0);
                        int atk = parser.getAttributeIntValue(null,"atk",0);
                        int hp = parser.getAttributeIntValue(null,"hp",0);
                        String attributes = parser.getAttributeValue(null, "attributes");

                        CardInfoBuilder builder = new CardInfoBuilder();
                        builder.setCardId(id).setCardClass(cardClass).setCardType(type).setCardRarity(rarity).setCardSet(set)
                                .setCardCost(cost).setCardAtk(atk).setCardHp(hp).setCardAttributes(attributes);
                        CardInfo info = builder.build();
                        cardList.add(info);
                    }
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CardInfo> getAllCards(){
        return this.cardList;
    }
}