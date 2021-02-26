package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Spansion.Spansion.makeEventPath;

public class MaxHPEvent extends AbstractImageEvent {
    public static final String ID = Spansion.makeID("MaxHPEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("IdentityCrisisEvent.png");

    private enum CUR_SCREEN{ INTRO, RESULT }

    private MaxHPEvent.CUR_SCREEN screen = MaxHPEvent.CUR_SCREEN.INTRO;

    private static final int HP_GAIN_HEAL = 5;
    private static final int HP_GAIN_NO_HEAL = 5;
    private static final int HP_LOSE = 5;

    public MaxHPEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        imageEventText.setDialogOption(OPTIONS[0] + HP_GAIN_HEAL + OPTIONS[1]);
        //imageEventText.setDialogOption(OPTIONS[0] + HP_GAIN_NO_HEAL + OPTIONS[2]);
        imageEventText.setDialogOption(OPTIONS[3] + HP_LOSE + OPTIONS[2]);

    }

    @Override
    protected void buttonEffect(int i) {
        switch (screen){
            case INTRO:
                switch(i){
                    case 0: // Gain max hp
                        AbstractDungeon.player.increaseMaxHp(HP_GAIN_HEAL, false);
                        imageEventText.updateBodyText(DESCRIPTIONS[1] + HP_GAIN_HEAL + DESCRIPTIONS[3]);
                        break;
                    case 1: // Lose max hp
                        AbstractDungeon.player.decreaseMaxHealth(HP_LOSE);
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        CardCrawlGame.sound.play("BLUNT_FAST");
                        imageEventText.updateBodyText(DESCRIPTIONS[2] + HP_LOSE + DESCRIPTIONS[5]);
                        break;
                }
                imageEventText.updateDialogOption(0, OPTIONS[4]);
                imageEventText.clearRemainingOptions();
                screen = CUR_SCREEN.RESULT;
                break;
            case RESULT:
                openMap();
                break;

        }
    }
}

