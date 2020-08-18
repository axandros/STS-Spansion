package Spansion.Cards;

import Spansion.Powers.DamagedCountPower;
import Spansion.Spansion;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Spansion.Spansion.makeCardPath;

public class WrathfulStrike extends CustomCard{
    // "Wrathful Strike" - Attack - 2 - Deal 1 damage.  Increase this value by 1 every time you take damage (on your turn).

    public static final String ID = Spansion.makeID(WrathfulStrike.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("WrathfulStrike.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.RED;

    private static final int COST = 1;
    private static final int DAMAGE = 1;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int DAMAGE_FROM_DAMAGE_TAKEN = 1;

    public WrathfulStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_FROM_DAMAGE_TAKEN;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int damageToDeal = damage;
        if(p.hasPower(DamagedCountPower.POWER_ID)) {
            damageToDeal +=
                    ((DamagedCountPower)p.getPower(DamagedCountPower.POWER_ID)).DamageTakenOnTurn()
                    * magicNumber;
        }
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damageToDeal), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
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
