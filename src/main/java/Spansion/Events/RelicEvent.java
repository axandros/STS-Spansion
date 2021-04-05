package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Spansion.Spansion.makeEventPath;

public class RelicEvent extends AbstractImageEvent {
    public static final String ID = Spansion.makeID("RelicEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("IdentityCrisisEvent.png");

    private enum CUR_SCREEN{ INTRO, RESULT }

    private RelicEvent.CUR_SCREEN screen = RelicEvent.CUR_SCREEN.INTRO;

    private static final String REQUIRED_RELIC = "Golden Idol";
    private AbstractRelic RELIC_TO_GAIN_RANDOM;
    private AbstractRelic RELIC_TO_LOSE_RANDOM;

    public RelicEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        RELIC_TO_GAIN_RANDOM = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.UNCOMMON);
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        relics.addAll(AbstractDungeon.player.relics);
        Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));
        RELIC_TO_LOSE_RANDOM = relics.get(0);

        boolean HasIdol = AbstractDungeon.player.hasRelic(REQUIRED_RELIC);

        Spansion.logger.info("Player has  "+REQUIRED_RELIC + ": " +AbstractDungeon.player.hasRelic(REQUIRED_RELIC));

        imageEventText.setDialogOption(OPTIONS[0] + REQUIRED_RELIC + OPTIONS[2], HasIdol, RelicLibrary.getRelic(REQUIRED_RELIC));
        imageEventText.setDialogOption(OPTIONS[0] + RELIC_TO_GAIN_RANDOM.name + OPTIONS[2], RELIC_TO_GAIN_RANDOM);
        imageEventText.setDialogOption(OPTIONS[1] + REQUIRED_RELIC + OPTIONS[2], !HasIdol, RelicLibrary.getRelic(REQUIRED_RELIC));
        imageEventText.setDialogOption(OPTIONS[1] + RELIC_TO_LOSE_RANDOM.name + OPTIONS[2], RELIC_TO_LOSE_RANDOM);
        imageEventText.setDialogOption(OPTIONS[1] + RELIC_TO_LOSE_RANDOM.name + OPTIONS[4] + RELIC_TO_GAIN_RANDOM + OPTIONS[2], RELIC_TO_GAIN_RANDOM);
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screen){
            case INTRO:
                switch(i){
                    case 0:
                        // Gain Specific Relic
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                                (Settings.WIDTH / 2), (Settings.HEIGHT / 2), RelicLibrary.getRelic(REQUIRED_RELIC).makeCopy());
                        imageEventText.updateBodyText(DESCRIPTIONS[1] + REQUIRED_RELIC + DESCRIPTIONS[3]);
                        break;
                    case 1:
                        // Gain Random Relic
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                                (Settings.WIDTH / 2), (Settings.HEIGHT / 2), RELIC_TO_GAIN_RANDOM.makeCopy());
                        imageEventText.updateBodyText(DESCRIPTIONS[1] + REQUIRED_RELIC + DESCRIPTIONS[4]);
                        break;
                    case 2:
                        // Lose Specific Relic
                        AbstractDungeon.player.loseRelic(REQUIRED_RELIC);
                        imageEventText.updateBodyText(DESCRIPTIONS[2] + REQUIRED_RELIC + DESCRIPTIONS[3]);
                        break;
                    case 3:
                        // Lose Random Relic
                        AbstractDungeon.player.loseRelic(RELIC_TO_LOSE_RANDOM.relicId);
                        imageEventText.updateBodyText(DESCRIPTIONS[2] + REQUIRED_RELIC + DESCRIPTIONS[4]);
                        break;
                    case 4:
                        int relicAtIndex = 0;
                        for (int j = 0; j < AbstractDungeon.player.relics.size(); j++) {
                            if ((AbstractDungeon.player.relics.get(j)).relicId.equals(RELIC_TO_LOSE_RANDOM.relicId)) {
                                relicAtIndex = j;
                                break;
                            }
                        }
                        AbstractDungeon.player.relics.get(relicAtIndex).onUnequip();
                        RELIC_TO_GAIN_RANDOM.instantObtain(AbstractDungeon.player, relicAtIndex, false);
                        break;
                }
                this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                this.imageEventText.clearRemainingOptions();
                this.screen = RelicEvent.CUR_SCREEN.RESULT;
                break;
            case RESULT:
                openMap();
                break;

        }
    }
}
