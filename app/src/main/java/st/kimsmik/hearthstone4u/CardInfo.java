package st.kimsmik.hearthstone4u;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenk on 2015/9/23.
 */
public class CardInfo {
    public enum CARD_TYPE{
        MINION("minion"),
        SPELL("spell"),
        WEAPON("weapon");
        private String name="";
        CARD_TYPE(String n){
            this.name = n;
        }
        public String getName(){
            return this.name;
        }
    }
    public enum CARD_CALSS{
        NORMAL("normal"),
        WORRIOR("worrior"),
        HUNTER("hunter"),
        MAGE("mage"),
        WARLOCK("warlock"),
        ROGUE("rogue"),
        SHAMAN("shaman"),
        PRIEST("priest"),
        DRUID("druid"),
        PALADIN("paladin");
        private String name="";
        CARD_CALSS(String n){
            this.name = n;
        }
        public String getName(){
            return this.name;
        }
    }
    public enum CARD_RARITY{
        BASIC("basic"),
        NORMAL("normal"),
        RARE("rare"),
        EPIC("epic"),
        LEGENDARY("legendary");
        private String name="";
        CARD_RARITY(String n){
            this.name = n;
        }
        public String getName(){
            return this.name;
        }
    }
    public enum CARD_SET{
        BASIC("basic"),
        CLASSIC("classic"),
        NAXX("naxx"),
        BRM("brm"),
        GVG("gvg"),
        TGT("tgt");
        private String name="";
        CARD_SET(String n){
            this.name = n;
        }
        public String getName(){
            return this.name;
        }
    }
    public enum CARD_ATTRIBUTE{
        BATTLE_CRY("battlecry"),
        STEALTH("stealth"),
        CHARGE("charge"),
        COMBO("combo"),
        DEATH_RATTLE("deathrattle"),
        DIVINE_SHIELD("divineshield"),
        ENRAGE("enrage"),
        FREEZE("freeze"),
        OVER_LOAD("overload"),
        SECRET("secret"),
        SILENCE("silence"),
        SPELL_DAMAGE("spelldamage"),
        SUMMON("summon"),
        WINDFURY("windfury"),
        TAUNT("taunt"),
        IMMUNE("immune"),
        INSPIRE("inspire"),
        JOUST("joust"),
        DRAW("draw");
        private String name="";
        CARD_ATTRIBUTE(String n){
            this.name = n;
        }
        public String getName(){
            return this.name;
        }
    }

    public String id ="";
    public CARD_CALSS cardClass = CARD_CALSS.NORMAL;
    public CARD_TYPE type = CARD_TYPE.MINION;
    public CARD_RARITY rarity = CARD_RARITY.BASIC;
    public CARD_SET set = CARD_SET.BASIC;
    public int cost = 0;
    public int atk = 0;
    public int hp = 0;
    public List<CARD_ATTRIBUTE> attributes = new ArrayList<>();

    public int getRarityColor(){
        if(rarity == CARD_RARITY.BASIC)
            return Color.GRAY;
        if(rarity == CARD_RARITY.NORMAL)
            return Color.BLACK;
        if(rarity == CARD_RARITY.RARE)
            return Color.BLUE;
        if(rarity == CARD_RARITY.EPIC)
            return  Color.parseColor("#FFDE00F");
        if(rarity == CARD_RARITY.LEGENDARY)
            return Color.parseColor("#FF7F00");
        return Color.BLACK;
    }

    public int getSetColor(){

        if(set == CARD_SET.BASIC)
            return Color.GRAY;
        if(set == CARD_SET.CLASSIC)
            return Color.BLACK;
        if(set == CARD_SET.NAXX)
            return Color.GREEN;
        if(set == CARD_SET.BRM)
            return  Color.RED;
        if(set == CARD_SET.GVG)
            return Color.parseColor("#FF7F00");
        if(set == CARD_SET.TGT)
            return Color.BLUE;
        return Color.BLACK;
    }
}
