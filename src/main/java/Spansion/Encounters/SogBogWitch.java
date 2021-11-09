package Spansion.Encounters;

import Spansion.Spansion;
import basemod.abstracts.CustomMonster;
import basemod.animations.AbstractAnimation;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.logging.Logger;


/// The SogBog Witch
// For her first move, she'll Cast a curse.
// This is a debuff on the player and herself.
// If she dies with the curse active, A Curse will be added to the player's deck.
public class SogBogWitch extends CustomMonster {
    public static final String ID = Spansion.makeID("SogBogWitch");
    public static final String ENCOUNTER_NAME = ID;
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    //public SpriterAnimation anim;

    // location
    private static final float HB_X = 0.0f;
    private static final float HB_Y = -15.0f;
    private static final float HB_W = 320.0f;
    private static final float HB_H = 240.0f;
    // Stats
    public static final int maxHealth = 70;

    // Moves
    private boolean curseCast = false;
    private static final int WEAK_ATTACK_POWER = 4;
    private static final int WEAK_ATTACK_TIMES = 2;
    private static final int DEBUFF_STACKS = 2;


    public SogBogWitch() {
        super(NAME,
                ID,
                maxHealth,
                HB_X,
                HB_Y,
                HB_W,
                HB_H,
                null);

        animation = new SpriterAnimation(Spansion.makeCharPath("Cultist.scml"));
        //this.state.addAnimation(0,"Idle",true, 0.0f);
        //AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        //e.setTime(e.getEndTime() * MathUtils.random());
        byte b = this.nextMove;
        Spansion.logger.info("Default next move byte: " + b );
    }

    /*
    public SogBogWitch(float x, float y) {
        super(NAME, ID, maxHealth, HB_X, HB_Y, HB_W, HB_H, null, x, y);
        // Animation stuff.
        //String resourceDirectory = "SpansionResources/images/char/defaultCharacter/";
        //this.loadAnimation(resourceDirectory + "skeleton.atlas", resourceDirectory+"skeleton.json", 1.0f);
        //AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);

        //e.setTime(e.getEndTime() * MathUtils.random());
    }

     */

    @Override
    public void takeTurn() {
        Spansion.logger.info("Entering \"Take Turn\"");
        switch(this.nextMove){
            case 0: // Cast the Curse
                AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(
                        this, this,
                        new StrengthPower(this, 1)
                ));
                curseCast = true;
                break;
            case 1: // Debuff Player
                AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(
                        AbstractDungeon.player, this,
                        new WeakPower(AbstractDungeon.player, DEBUFF_STACKS, true)

                ));
                break;
            case 2: // Attack
            default:
                for(int i = 0; i < WEAK_ATTACK_TIMES; i++) {
                    AbstractDungeon.actionManager.addToBottom(
                            new DamageAction(
                                    AbstractDungeon.player
                                    , new DamageInfo(this, WEAK_ATTACK_POWER)
                                    , AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
        }
        Spansion.logger.info("Ending Switch.");
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        Spansion.logger.info("Exiting \"Take Turn\"");
    }

    @Override
    protected void getMove(int i) {
        if(!curseCast){
            this.setMove((byte) 0, Intent.BUFF);

        }
        else{
            byte b = this.nextMove;
            if(this.nextMove == 0) {
                b = (byte) (i/99 * 2 +1);
                Spansion.logger.info("Switch on Byte: " + b);
            }
            switch(b){
                case 1: this.setMove((byte) 2, Intent.ATTACK, WEAK_ATTACK_POWER, WEAK_ATTACK_TIMES, true);
                    break;
                case 2: this.setMove((byte)1, Intent.DEBUFF);
                default:
                    break;
            }
        }
    }


}
