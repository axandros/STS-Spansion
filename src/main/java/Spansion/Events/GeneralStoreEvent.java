package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Spansion.Spansion.makeEventPath;

public class GeneralStoreEvent extends AbstractImageEvent {
    public static final String ID = Spansion.makeID("GeneralStoreEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("IdentityCrisisEvent.png");

    public GeneralStoreEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
    }

    @Override
    protected void buttonEffect(int i) {

    }
}
