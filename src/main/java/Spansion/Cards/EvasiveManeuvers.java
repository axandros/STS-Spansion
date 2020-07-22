package Spansion.Cards;

import Spansion.Spansion;
import Spansion.util.Shortcuts;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static Spansion.Spansion.makeCardPath;

public class EvasiveManeuvers extends CustomCard {
    // "Evasive Maneuvers" - Gain 10 block.  If this is discarded by a card effect, gain 1 Dexterity.

    public static final String ID = Spansion.makeID(EvasiveManeuvers.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("EvasiveManeuvers.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.GREEN;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int DEXTERITY_TO_GAIN = 1;

    public EvasiveManeuvers() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        baseBlock = block = BLOCK;
        baseMagicNumber = magicNumber = DEXTERITY_TO_GAIN;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p, p, baseBlock));
        //AbstractDungeon.actionManager.addToBottom(
        //            new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), 1)
        //        );
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();
        AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player
                            , new DexterityPower(AbstractDungeon.player, magicNumber), 1)
                );
    }
}