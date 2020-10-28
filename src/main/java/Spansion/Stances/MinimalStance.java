package Spansion.Stances;

import Spansion.Spansion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;

public class MinimalStance extends AbstractStance {
    public static final String STANCE_ID = Spansion.makeID(MinimalStance.class.getSimpleName());
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static final String NAME = stanceString.NAME;
    public static final String[] DESCRIPTIONS = stanceString.DESCRIPTION;

    private static long sfxId = -1L;

    public MinimalStance() {
        this.ID = STANCE_ID;
        this.name = NAME;
        updateDescription();
        Spansion.logger.info("Drunken ID: " + STANCE_ID);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}