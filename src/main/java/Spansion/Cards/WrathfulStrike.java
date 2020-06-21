package Spansion.Cards;

import Spansion.Spansion;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.TagBasicCards;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Spansion.Spansion.makeCardPath;

public class WrathfulStrike extends CustomCard{
    // "Wrathful Strike" - Attack - 2 - Deal 1 damage.  Increase this value by 1 every time you take damage (on your turn).
    // TODO: How to count the number of times damaged on own turn?

    public static final String ID = Spansion.makeID(WrathfulStrike.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("WrathfulStrike.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.RED;

    private static final int COST = 2;
    private static final int DAMAGE = 1;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int INCREASE_PER_HIT = 1;
    private static final int UPGRADE_INCREASE_PER_HIT = 2;

    public WrathfulStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = INCREASE_PER_HIT;

        tags.add(CardTags.STRIKE);
    }



    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }

}
