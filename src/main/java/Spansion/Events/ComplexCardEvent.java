package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Spansion.Spansion.makeEventPath;

public class ComplexCardEvent extends AbstractImageEvent {

    public static final String ID = Spansion.makeID("ComplexCardEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Diary.png");

    private enum CUR_SCREEN{ INTRO, COMPLETE}

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private boolean chooseUpgrade = false;
    private boolean chooseTransform = false;
    private boolean chooseDuplicate = false;

    public ComplexCardEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int i) {
        switch(screen){
            case INTRO:
                switch (i){
                    case 0: // Add a card to your deck
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        break;
                    case 1: // Remove a card
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        break;
                    case 2: // Add a skill
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        break;
                    case 3: // Remove a skill
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
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
