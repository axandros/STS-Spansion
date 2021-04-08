package Spansion.Potions;

import Spansion.Orbs.ToxicOrb;
import Spansion.Spansion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import java.nio.channels.Channel;

public class ToxicOrbPotion extends AbstractPotion {
    // Channel Toxic Orbs,
    public static final String POTION_ID = Spansion.makeID("ToxicOrbPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final Color LIQUID_COLOR = CardHelper.getColor(10.0f, 250.0f, 64.0f);
    public static final Color HYBRID_COLOR = CardHelper.getColor(10.0f, 30.0f, 20.0f);
    public static final Color SPOTS_COLOR = CardHelper.getColor(100.0f, 25.0f, 10.0f);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public ToxicOrbPotion(){
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.M, PotionColor.FRUIT);
        potency = getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        isThrown = false;
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        int potency = getPotency();
        for(int i = 0; i < potency; i++){
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(new ToxicOrb()));
        }
    }

    @Override
    public int getPotency(int i) {
        //AbstractDungeon.
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return null;
    }
}
