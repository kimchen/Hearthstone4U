package st.kimsmik.hearthstone4u;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by chenk on 2015/9/23.
 */
public class CardInfoBuilder {

    private  String id = "";
    private String cardClass = "";
    private String type = "";
    private String rarity = "";
    private String set = "";
    private int cost = 0;
    private int atk = 0;
    private int hp = 0;
    private String attributes = "";

    public CardInfoBuilder(){};

    public CardInfoBuilder setCardId(String s){
        if(s!=null)
            this.id = s;
        return this;
    }
    public CardInfoBuilder setCardClass(String s){
        if(s!=null)
            this.cardClass = s;
        return this;
    }
    public CardInfoBuilder setCardType(String s){
        if(s!=null)
            this.type = s;
        return this;
    }
    public CardInfoBuilder setCardRarity(String s){
        if(s!=null)
            this.rarity = s;
        return this;
    }
    public CardInfoBuilder setCardSet(String s){
        if(s!=null)
            this.set = s;
        return this;
    }
    public CardInfoBuilder setCardCost(int i){
        this.cost = i;
        return this;
    }
    public CardInfoBuilder setCardAtk(int i){
        this.atk = i;
        return this;
    }
    public CardInfoBuilder setCardHp(int i){
        this.hp = i;
        return this;
    }
    public CardInfoBuilder setCardAttributes(String s){
        if(s!=null)
            this.attributes = s;
        return this;
    }

    public CardInfo build(){
        CardInfo info = new CardInfo();
        if(!this.id.equals("")){
            info.id = this.id;
        }
        if(!this.cardClass.equals("")){
            for(CardInfo.CARD_CALSS temp : CardInfo.CARD_CALSS.values()){
                if(this.cardClass.equals(temp.getName())){
                    info.cardClass = temp;
                    break;
                }
            }
        }
        if(!this.type.equals("")){
            for(CardInfo.CARD_TYPE temp : CardInfo.CARD_TYPE.values()){
                if(this.type.equals(temp.getName())){
                    info.type = temp;
                    break;
                }
            }
        }
        if(!this.rarity.equals("")){
            for(CardInfo.CARD_RARITY temp : CardInfo.CARD_RARITY.values()){
                if(this.rarity.equals(temp.getName())){
                    info.rarity = temp;
                    break;
                }
            }
        }
        if(!this.set.equals("")){
            for(CardInfo.CARD_SET temp : CardInfo.CARD_SET.values()){
                if(this.set.equals(temp.getName())){
                    info.set = temp;
                    break;
                }
            }
        }
        info.cost = this.cost;
        info.atk = this.atk;
        info.hp = this.hp;

        if(!this.attributes.equals("")) {
            String[] atts = this.attributes.split(",");
            for(String att : atts){
                for(CardInfo.CARD_ATTRIBUTE temp : CardInfo.CARD_ATTRIBUTE.values()) {
                    if (this.set.equals(temp.getName())) {
                        info.attributes.add(temp);
                        break;
                    }
                }
            }
        }
        return info;
    }
}
