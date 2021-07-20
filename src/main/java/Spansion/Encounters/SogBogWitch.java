package Spansion.Encounters;

import Spansion.Spansion;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


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
        this.loadAnimation("conspire/images/monsters/FuzzyDie/skeleton.atlas", "conspire/images/monsters/FuzzyDie/skeleton.json", 1.0f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);

        e.setTime(e.getEndTime() * MathUtils.random());

    }

    @Override
    public void takeTurn() {

    }

    @Override
    protected void getMove(int i) {
        if(!curseCast){

        }else{

        }
    }
}
