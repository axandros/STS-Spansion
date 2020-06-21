package Spansion.Cards;

import Spansion.Spansion;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Spansion.Spansion.makeCardPath;

public class VisionsOfPain extends CustomCard {
    //"Visions of Pain" - Skill - 1 - Take 3 Damage, Draw 2.
    public static final String ID = Spansion.makeID(VisionsOfPain.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("VisionsOfPain.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.RED;

    private static final int COST = 1;
    private static final int SELF_DAMAGE = 3;
    private static final int UPGRADED_SELF_DAMAGE = -1;
    private static final int DRAW = 2;
    private static final int UPGRADED_DRAW = 3;

    public VisionsOfPain() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = SELF_DAMAGE;
        baseMagicNumber = magicNumber = DRAW;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                p, new DamageInfo(p,damage, DamageInfo.DamageType.HP_LOSS))
        );
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));

    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeDamage(UPGRADED_SELF_DAMAGE);
            upgradeMagicNumber(UPGRADED_DRAW);
            initializeDescription();
        }
    }
}
