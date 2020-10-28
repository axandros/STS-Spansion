package Spansion.Cards.Blue;

import Spansion.Powers.ArchaicFuelPower;
import Spansion.Spansion;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Spansion.Spansion.makeCardPath;

public class ArchaicFuel extends CustomCard {
    // "Aspect of the Crow" - Power - Every time you play a card, gain 1 temporary strength and 1 temporary dexterity.
    public static final String ID = Spansion.makeID(ArchaicFuel.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ArchaicFuel.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 2;

    private static final int UPGRADE_PLUS_STACK = 1;
    private static final int UPGRADED_COST = 1;

    public ArchaicFuel() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = 1;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new ArchaicFuelPower(p,p, baseMagicNumber), baseMagicNumber)
        );
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            //upgradeMagicNumber(UPGRADE_PLUS_STACK);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}