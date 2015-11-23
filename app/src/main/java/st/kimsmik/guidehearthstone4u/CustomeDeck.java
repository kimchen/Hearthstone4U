package st.kimsmik.guidehearthstone4u;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Kim on 2015/9/28.
 */
public class CustomeDeck {
    public String name = "";
    public CardInfo.CARD_CLASS deckClass = CardInfo.CARD_CLASS.WARRIOR;
    public List<DeckCardInfo> cardList = new ArrayList<>();
    public boolean deleteCard(String id){
        DeckCardInfo target = null;
        for(DeckCardInfo cardInfo : this.cardList){
            if(cardInfo.id.equals(id)){
                target = cardInfo;
                break;
            }
        }
        if(target == null)
            return false;
        if(target.num > 1)
            target.num --;
        else{
            this.cardList.remove(target);
        }
        return true;
    }
    public int getCardNum(){
        int number = 0;
        for(DeckCardInfo deckCardInfo : this.cardList){
            number += deckCardInfo.num;
        }
        return number;
    }
    public int getCardNumByCost(int cost){
        int number = 0;
        for(DeckCardInfo deckCardInfo : this.cardList){
            if(cost == 7 && deckCardInfo.cost >= 7)
                number += deckCardInfo.num;
            else if(deckCardInfo.cost == cost)
                number += deckCardInfo.num;
        }
        return number;
    }

    public boolean addCard(CardInfo card)
    {
        if(card == null)
            return false;
        if(getCardNum() >= 30)
            return false;
        DeckCardInfo target = null;
        for(DeckCardInfo cardInfo : this.cardList){
            if(cardInfo.id.equals(card.id)){
                target = cardInfo;
                break;
            }
        }
        if(target == null){
            target = new DeckCardInfo();
            target.id = card.id;
            target.num = 1;
            target.cost = card.cost;
            this.cardList.add(target);
            Collections.sort(this.cardList, new Comparator<DeckCardInfo>() {
                @Override
                public int compare(DeckCardInfo lhs, DeckCardInfo rhs) {
                    return lhs.cost - rhs.cost;
                }
            });
            return true;
        }else if(target.num < 1){
            target.num = 1;
            return true;
        }else if(target.num == 1){
            if(card.rarity == CardInfo.CARD_RARITY.LEGENDARY)
                return false;
            target.num = 2;
            return true;
        }else if(target.num >= 2){
            return false;
        }
        return false;
    }
}
