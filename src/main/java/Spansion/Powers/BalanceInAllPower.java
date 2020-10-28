package Spansion.Powers;

import Spansion.Spansion;
import Spansion.Stances.DrunkenStance;
import Spansion.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.stances.WrathStance;

import static Spansion.Spansion.makePowerPath;

public class BalanceInAllPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = Spansion.makeID(BalanceInAllPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("archaicfuel_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("archaicfuel_power32.png"));

    public BalanceInAllPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractPlayer player = AbstractDungeon.player;
        super.atEndOfTurn(isPlayer);

        if(AbstractDungeon.player.stance.ID == CalmStance.STANCE_ID) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DrawCardNextTurnPower(player, 2)));
        }
        else if(AbstractDungeon.player.stance.ID == WrathStance.STANCE_ID){
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new NeutralStance()));
            this.amount -= 1;
        }
        else if (AbstractDungeon.player.stance.ID == DrunkenStance.STANCE_ID) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, amount * 5));
        }


    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];

    }

    @Override
    public AbstractPower makeCopy() {
        return new BalanceInAllPower(owner, source, amount);
    }
}