package Spansion.Events;

import Spansion.Spansion;
import basemod.helpers.RelicType;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import static Spansion.Spansion.makeEventPath;

public class GeneralShopEvent extends AbstractImageEvent {

    public static final String ID = Spansion.makeID("GeneralShopEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("IdentityCrisisEvent.png");
    private CUR_SCREEN screen = CUR_SCREEN.INTRO;
    private boolean pickCard = false; // Do we bring up the card selection screen?
    private boolean giveCard = false;


    private AbstractRelic RelicToTrade;
    private int AttackDamage = 5;
    private int GoldStolen = 100;

    private enum CUR_SCREEN {
        INTRO, COMPLETE;}

    public GeneralShopEvent(){
        super(NAME, DESCRIPTIONS[0], IMG);

        // Pick a random relic to trade
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        relics.addAll(AbstractDungeon.player.relics);
        Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));

        RelicToTrade = relics.get(0);
        //RelicToTrade;


        // Set description and options
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2] + OPTIONS[3]);
        imageEventText.setDialogOption(OPTIONS[4]);
    }

    public void update() {
        super.update();

        if (this.pickCard
                &&
                !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
       	    AbstractCard c = (AbstractDungeon.gridSelectScreen.selectedCards.get(0)).makeCopy();
            Spansion.logger.info("Adding " + c.cardID + " to the deck.");
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
      	    AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
            this.imageEventText.updateDialogOption(0, OPTIONS[5]);
            this.imageEventText.clearRemainingOptions();
            screen = CUR_SCREEN.COMPLETE;
        } else if( giveCard
                &&!AbstractDungeon.isScreenUp
                && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.remove(0);
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (Settings.WIDTH / 2), (Settings.HEIGHT / 2)));
            AbstractDungeon.player.masterDeck.removeCard(c);
            AbstractDungeon.player.obtainPotion(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.COMMON,false));
            this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
            this.imageEventText.updateDialogOption(0, OPTIONS[5]);
            this.imageEventText.clearRemainingOptions();
            screen = CUR_SCREEN.COMPLETE;
        }

    }

    @Override
    protected void buttonEffect(int i) {
        switch(screen){
            case INTRO:
                switch(i){
                    case 0: // "Spend 20 Gold. Choose a common card.",
                        // Status: Works!
                        AbstractDungeon.player.loseGold(20);
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        int c = 0;
                        while(c < 3){
                            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON);
                            if(!group.contains(card)){
                                Spansion.logger.info("GS: Adding " + card.cardID + " to list.");
                                group.addToBottom(card);
                                c++;
                         }
                        }
                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[6], false);
                        this.pickCard = true;
                        break;
                    case 1: // "Give a card, get 1 random potion",
                        // Status: Works!
                        giveCard = true;
                        if(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0){
                            AbstractDungeon.gridSelectScreen.open(
                            CardGroup.getGroupWithoutBottledCards(
                                    AbstractDungeon.player.masterDeck.getPurgeableCards()),
                                    1, OPTIONS[7], false, false, false, true);
                        }

                        break;
                    case 2: // "Give a random, pre-selected relic, Get relic of one tier higher..",
                        // Status: Does nothing.
                        Spansion.logger.info("Update Body Text");
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        Spansion.logger.info("Update Dialog Options");
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        Spansion.logger.info("Clear Remaining Options");
                        this.imageEventText.clearRemainingOptions();
                        Spansion.logger.info("CurScreen = Complete");
                        screen = CUR_SCREEN.COMPLETE;
                        break;
                    case 3: // "Lose 5 HP, Gain Injured and 100 Gold."
                        // Status: Works!
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, AttackDamage));
                        AbstractDungeon.player.gainGold(GoldStolen);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
                                CardLibrary.getCopy(((AbstractCard)new Injury()).cardID), (Settings.WIDTH / 2), (Settings.HEIGHT / 2)));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        screen = CUR_SCREEN.COMPLETE;
                        break;
                }
                break;
            case COMPLETE:
                switch(i){
                    default:
                        openMap();
                }


        }
    }
}
