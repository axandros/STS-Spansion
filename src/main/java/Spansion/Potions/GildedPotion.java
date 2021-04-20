package Spansion.Potions;

import Spansion.Spansion;
import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.EverythingFix;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;

public class GildedPotion extends AbstractPotion {
    // Gain 30 Gold.  Can use outside of combat.

    public static final String POTION_ID = Spansion.makeID("GildedPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    // Liquid color a=0 is black.
    public static final Color LIQUID_COLOR = new Color(0.85f, 0.0f, 0.15f, 1);//CardHelper.getColor(215.0f, 183.0f, 64.0f).;
    // Hybrid/Spots color a=0 is transparent.
    public static final Color HYBRID_COLOR = new Color(0f, 1f, 0f, 1);
    public static final Color SPOTS_COLOR = new Color(0f, 1f, 0f, 1);

    public GildedPotion(){
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.M, null);
        //initializeData();
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractDungeon.player.gainGold(getPotency());
        AbstractDungeon.effectList.add( new RainingGoldEffect(getPotency()));
    }

    @Override
    public int getPotency(int i) {
        return 30;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new GildedPotion();
    }

    @Override
    public boolean canUse() {
        if (AbstractDungeon.actionManager.turnHasEnded &&
           (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
           return false;
         }
         if ((AbstractDungeon.getCurrRoom()).event != null &&
           (AbstractDungeon.getCurrRoom()).event instanceof com.megacrit.cardcrawl.events.shrines.WeMeetAgain) {
           return false;
         }

         return true;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        isThrown = false;
        this.tips.clear();
        tips.add(new PowerTip(name, description));
    }
}
