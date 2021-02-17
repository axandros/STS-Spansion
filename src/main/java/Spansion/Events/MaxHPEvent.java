package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Spansion.Spansion.makeEventPath;

public class MaxHPEvent extends AbstractImageEvent {
    public static final String ID = Spansion.makeID("MaxHPEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("IdentityCrisisEvent.png");

    private enum CUR_SCREEN{ INTRO, RESULT };

    private MaxHPEvent.CUR_SCREEN screen = MaxHPEvent.CUR_SCREEN.INTRO;

    private static final int HP_GAIN_HEAL = 5;
    private static final int HP_GAIN_NO_HEAL = 5;
    private static final int HP_LOSE = 5;

    public MaxHPEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screen){
            case INTRO:
                switch(i){
                    case 0:
                        // Gain max hp and heal
                        AbstractDungeon.player.increaseMaxHp(HP_GAIN_HEAL, false);
                        AbstractDungeon.player.heal(HP_GAIN_HEAL);
                        break;
                    case 1:
                        // Gain max hp but don't heal
                        AbstractDungeon.player.damage(new DamageInfo(null, HP_LOSE, DamageInfo.DamageType.HP_LOSS));
                        break;
                    case 2:
                        // Lose max HP
                        AbstractDungeon.player.decreaseMaxHealth(HP_LOSE);
                        break;

                }
                break;
            case RESULT:
                openMap();
                break;

        }
    }
}

