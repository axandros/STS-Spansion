package Spansion.Cards;

import Spansion.Orbs.ToxicOrb;
import Spansion.Spansion;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;

import static Spansion.Spansion.makeCardPath;

public class ToxicEmissions extends CustomCard {
    // "Toxic Emissions" - Skill - 1 - Apply 1 weak and 1 poison to all enemies.

    public static final String ID = Spansion.makeID(ToxicEmissions.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ToxicEmissions.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.BLUE;

    private static final int COST = 1;
    private static final int POISON = 1;
    private static final int UPGRADE_POISON = 1;
    private static final int WEAK = 1;
    private static final int UPGRADE_WEAK = 1;

    public ToxicEmissions() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        baseMagicNumber = magicNumber = WEAK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractMonster> mons = AbstractDungeon.getCurrRoom().monsters.monsters;
        for(int i = 0; i < mons.size(); i++) {
            m = mons.get(i);
            if(m.currentHealth > 0 && !(m.isPlayer)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new ToxicOrb()));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_WEAK);
            initializeDescription();
        }
    }
}