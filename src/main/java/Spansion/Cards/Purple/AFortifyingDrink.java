package Spansion.Cards.Purple;

import Spansion.Powers.StaggerPower;
import Spansion.Spansion;
import Spansion.Stances.DrunkenStance;
import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Random;

import static Spansion.Spansion.makeCardPath;

public class AFortifyingDrink extends CustomCard {

    public static final String ID = Spansion.makeID(AFortifyingDrink.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Skill.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.PURPLE;

    private static final int COST = 1;
    private static final int BLOCK = 4;
    private static final int BLOCK_PLUS_UPGRADE = 8;

    public AFortifyingDrink() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        //this.exhaust = true;
        //this.selfRetain = true;
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeBlock(BLOCK_PLUS_UPGRADE);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer plr, AbstractMonster abstractMonster) {

        if(plr.stance.ID == DrunkenStance.STANCE_ID) {
            AbstractDungeon.actionManager.addToBottom( new GainBlockAction(plr,baseBlock));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(plr, plr,new StaggerPower(plr, plr,
                    10)));
        } else {
            Spansion.ActionManagerDebug();
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new DrunkenStance()));
            Spansion.logger.info("Pushed Change Stance to Drunken");
            Spansion.ActionManagerDebug();
        }
    }
}
