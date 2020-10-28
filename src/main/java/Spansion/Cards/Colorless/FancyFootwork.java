package Spansion.Cards.Colorless;

import Spansion.Spansion;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Spansion.Spansion.makeCardPath;

public class FancyFootwork extends CustomCard {
    // "Fancy Footwork" - Skill - 1 - If the enemy intends to attack, gain 8 block.  Otherwise draw a card.

    public static final String ID = Spansion.makeID(FancyFootwork.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Skill.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_PLUS_BLOCK = 4;
    private static final int DRAW = 2;
    private static final int UPGRADE_DRAW = 1;

    public FancyFootwork() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        baseBlock = block = BLOCK;
        baseMagicNumber = magicNumber = DRAW;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(m.intent == AbstractMonster.Intent.ATTACK || m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
                m.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||m.intent == AbstractMonster.Intent.ATTACK_DEFEND){
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        }
        else {AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p,magicNumber));}


    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_DRAW);
            initializeDescription();
        }
    }
}