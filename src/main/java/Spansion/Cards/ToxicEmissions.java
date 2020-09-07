package Spansion.Cards;

import Spansion.Orbs.ToxicOrb;
import Spansion.Spansion;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;

import static Spansion.Spansion.makeCardPath;

public class ToxicEmissions extends CustomCard {
    // Toxic Emissions - Skill - 0 - Apply 1 weak and vulnerable to an enemy for each different type of orb.
    public static final String ID = Spansion.makeID(OldOneTwo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.BLUE;

    private static final int COST = 0;
    private static final int WEAK_VULN_PER_UNIQUE_ORB = 1;

    public ToxicEmissions() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        magicNumber = baseMagicNumber = WEAK_VULN_PER_UNIQUE_ORB;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<String> orbList = new ArrayList<String>();
        for(AbstractOrb o: AbstractDungeon.player.orbs){
            if(o.ID != null
                    && !o.ID.equals("Empty")
                    && !orbList.contains(o.ID)){
                orbList.add(o.ID);
            }
        }
        int amount = orbList.size() * magicNumber;
        AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(m, p, new VulnerablePower(m, amount, false)));
        AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(m, p, new WeakPower(m, amount, false)));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(WEAK_VULN_PER_UNIQUE_ORB);
            initializeDescription();
        }
    }
}