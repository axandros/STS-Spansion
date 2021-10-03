package Spansion.Encounters;

import Spansion.Spansion;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.logging.Logger;


/// The SogBog Witch
// For her first move, she'll Cast a curse.
// This is a debuff on the player and herself.
// If she dies with the curse active, A Curse will be added to the player's deck.
public class SogBogWitch extends AbstractMonster {
    public static final String ID = Spansion.makeID("SogBogWitch");
    public static final String ENCOUNTER_NAME = ID;
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    // location
    private static final float HB_X = 0.0f;
    private static final float HB_Y = -15.0f;
    private static final float HB_W = 320.0f;
    private static final float HB_H = 240.0f;
    // stats
    public static final int maxHealth = 70;

    private boolean curseCast = false;

    public SogBogWitch(float x, float y) {
        super(NAME, ID, maxHealth, HB_X, HB_Y, HB_W, HB_H, null, x, y);

        // Animation stuff.
        String resourceDirectory = "SpansionResources/images/char/defaultCharacter/";
        this.loadAnimation(resourceDirectory + "skeleton.atlas", resourceDirectory+"skeleton.json", 1.0f);
        //AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);

        //e.setTime(e.getEndTime() * MathUtils.random());

    }

    @Override
    public void takeTurn() {
        Spansion.logger.info("Entering \"Take Turn\"");
        switch(this.nextMove){
            default:
                Spansion.logger.info("Entering switch-default.");
                //AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(
                                AbstractDungeon.player
                                , new DamageInfo(this, 4)
                        , AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(
                                AbstractDungeon.player
                                , new DamageInfo(this, 4)
                                , AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
        }
        Spansion.logger.info("Ending Switch.");
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        Spansion.logger.info("Exiting \"Take Turn\"");
    }

    @Override
    protected void getMove(int i) {
        this.setMove((byte) 0, Intent.ATTACK, 4, 2, true);
    }
}
