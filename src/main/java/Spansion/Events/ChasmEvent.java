package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static Spansion.Spansion.makeEventPath;

public class ChasmEvent extends AbstractImageEvent {
    public static final String ID = Spansion.makeID("ChasmEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("IdentityCrisisEvent.png");

    private static final int DEFENSE_THRESHOLD = 15;

    private AbstractPotion potionToLose = null;
    private boolean highDefenseCard = false;

    public ChasmEvent(){
        super(NAME, DESCRIPTIONS[0], IMG);

        if(AbstractDungeon.player.potions.size() > 0){
            potionToLose = AbstractDungeon.player.getRandomPotion();
        }
        for(AbstractCard card: AbstractDungeon.player.masterDeck.group){
            if(card.block >= DEFENSE_THRESHOLD ){
                highDefenseCard = true;
            }
        }

    }

    private enum CUR_SCREEN {
        INTRO, COMPLETE;}
    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    @Override
    protected void buttonEffect(int i) {

    }
}
