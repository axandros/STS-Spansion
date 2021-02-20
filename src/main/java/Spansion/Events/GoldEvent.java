package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;

import static Spansion.Spansion.makeEventPath;

public class GoldEvent extends AbstractImageEvent {
    public static final String ID = Spansion.makeID("GoldEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("IdentityCrisisEvent.png");

    private enum CUR_SCREEN{ INTRO, RESULT }

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private static final int GOLD_GAIN = 50;
    private static final int GOLD_LOSE = 50;

    private int RandGoldGain;
    private int RandGoldLose;

    public GoldEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        RandGoldGain = AbstractDungeon.miscRng.random(0,100);
        RandGoldLose = AbstractDungeon.miscRng.random(0,
                AbstractDungeon.player.gold);

        imageEventText.setDialogOption(OPTIONS[0] + GOLD_GAIN + OPTIONS[2]);
        imageEventText.setDialogOption(OPTIONS[0] + RandGoldGain + OPTIONS[2]);
        imageEventText.setDialogOption(OPTIONS[1] + GOLD_LOSE + OPTIONS[2]);
        imageEventText.setDialogOption(OPTIONS[1] + RandGoldLose + OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screen){
            case INTRO:
                switch(i){
                    case 0:
                        // Gain Specific Gold
                        AbstractDungeon.player.gainGold(GOLD_GAIN);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1] + GOLD_GAIN + DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD_GAIN));
                        break;
                    case 1:
                        // Gain Random Gold
                        AbstractDungeon.player.gainGold(RandGoldGain);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1] + RandGoldGain + DESCRIPTIONS[4]);
                        AbstractDungeon.effectList.add(new RainingGoldEffect(RandGoldGain));
                        break;
                    case 2:
                        // Lose Specific Gold
                        AbstractDungeon.player.loseGold(GOLD_LOSE);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2] + GOLD_LOSE + DESCRIPTIONS[3]);
                        break;
                    case 3:
                        // Lose Random Gold
                        AbstractDungeon.player.loseGold(RandGoldLose);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2] + RandGoldLose + DESCRIPTIONS[4]);
                        break;
                }
                this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                this.imageEventText.clearRemainingOptions();
                this.screen = CUR_SCREEN.RESULT;
                break;
            case RESULT:
                openMap();
                break;

        }
    }
}
