package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;

import static Spansion.Spansion.makeEventPath;

public class PotionEvent extends AbstractImageEvent {

    public static final String ID = Spansion.makeID("PotionEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Diary.png");

    private enum CUR_SCREEN{ INTRO, COMPLETE}

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private AbstractPotion PotionToLose;

    public PotionEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.noCardsInRewards = true;

        imageEventText.setDialogOption(OPTIONS[0]);
        if(AbstractDungeon.player.hasAnyPotions()) {
            PotionToLose = AbstractDungeon.player.getRandomPotion();
            imageEventText.setDialogOption(OPTIONS[1] + PotionToLose.name + OPTIONS[2]);
        }
        else{
            imageEventText.setDialogOption(OPTIONS[3], true);
        }
    }

    @Override
    protected void buttonEffect(int i) {
        switch(screen){
            case INTRO:
                switch (i){
                    case 0:
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(PotionHelper.getRandomPotion()));
                        AbstractDungeon.combatRewardScreen.open();
                        break;
                    case 1:
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        AbstractDungeon.player.removePotion(PotionToLose);
                        break;
                }

                imageEventText.updateDialogOption(0, OPTIONS[4]);
                imageEventText.clearRemainingOptions();
                screen = CUR_SCREEN.COMPLETE;
                break;
            case COMPLETE:
                openMap();
                break;
        }
    }
}
