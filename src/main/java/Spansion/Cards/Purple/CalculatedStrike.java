package Spansion.Cards.Purple;

import Spansion.Powers.StaggerPower;
import Spansion.Spansion;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;

import static Spansion.Spansion.makeCardPath;

public class CalculatedStrike extends CustomCard {

    public static final String ID = Spansion.makeID(CalculatedStrike.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Attack.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.PURPLE;

    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DAMAGE = 3;

    private static final int COST = 1;

    public CalculatedStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = 2;
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        int stagger = 0;

        for (AbstractPower pwr:abstractPlayer.powers) {
            StaggerPower stag = (StaggerPower)pwr;
            if(stag != null){
                stagger = stag.amount;
            }
        }
        int mark = 0;
        for (AbstractPower pwr:abstractMonster.powers) {
            MarkPower mk = (MarkPower) pwr;
            if(mk != null){
                mark = mk.amount;
            }
        }

        int damageToDeal = baseDamage + stagger/baseMagicNumber + mark;

        AbstractDungeon.actionManager.addToBottom(new DamageAction(abstractMonster
                , new DamageInfo(abstractPlayer, damageToDeal), AbstractGameAction.AttackEffect.BLUNT_HEAVY));

    }
}
