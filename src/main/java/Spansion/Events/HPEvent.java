package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Spansion.Spansion.makeEventPath;

public class HPEvent  extends AbstractImageEvent {
    public static final String ID = Spansion.makeID("HPEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("IdentityCrisisEvent.png");

    private enum CUR_SCREEN{ INTRO, RESULT };

    private HPEvent.CUR_SCREEN screen = HPEvent.CUR_SCREEN.INTRO;

    private static final int HP_GAIN = 5;
    private static final int HP_LOSE = 5;

    public HPEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screen){
            case INTRO:
                switch(i){
                    case 0:
                        // Gain Specific Gold
                        AbstractDungeon.player.heal(HP_GAIN);
                        break;
                    case 1:
                        // Gain Random Gold
                        AbstractDungeon.player.damage(new DamageInfo(null, HP_LOSE, DamageInfo.DamageType.HP_LOSS));
                        break;

                }
                break;
            case RESULT:
                openMap();
                break;

        }
    }
}
