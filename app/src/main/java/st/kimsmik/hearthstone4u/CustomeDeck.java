package st.kimsmik.hearthstone4u;

import java.util.ArrayList;
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
}
