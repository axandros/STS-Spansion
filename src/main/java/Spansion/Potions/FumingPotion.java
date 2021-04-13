package Spansion.Potions;

import Spansion.Spansion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.WeakPower;

public class FumingPotion extends AbstractPotion {

    public static final String POTION_ID = Spansion.makeID("FumingPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = CardHelper.getColor(125.0f, 183.0f, 64.0f);
    public static final Color HYBRID_COLOR = CardHelper.getColor(100.0f, 30.0f, 200.0f);
    public static final Color SPOTS_COLOR = CardHelper.getColor(20.0f, 25.0f, 10.0f);

    public FumingPotion(){
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.M, PotionColor.FRUIT);
        potency = getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        isThrown = true;
        targetRequired = true;
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(abstractCreature, AbstractDungeon.player,
                        new WeakPower(abstractCreature, getPotency(), false), getPotency()));
    }

    @Override
    public int getPotency(int i) {
        return 2;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FumingPotion();
    }
}
