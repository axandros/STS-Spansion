package Spansion.Stances;

import Spansion.Cards.ToxicEmissions;
import Spansion.Spansion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

public class DrunkenStance extends AbstractStance {
    public static final String STANCE_ID = Spansion.makeID(DrunkenStance.class.getSimpleName());
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static final String NAME = stanceString.NAME;
    public static final String[] DESCRIPTIONS = stanceString.DESCRIPTION;

    private static long sfxId = -1L;

    public DrunkenStance() {
        this.ID = STANCE_ID;
        this.name = NAME;
        updateDescription();
        Spansion.logger.info("Drunken ID: " + STANCE_ID);
    }


    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void updateAnimation() {

        if (!Settings.DISABLE_EFFECTS) {

            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.04F;
                AbstractDungeon.effectsQueue.add(new CalmParticleEffect());
            }
        }


        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            AbstractDungeon.effectsQueue.add(new StanceAuraEffect("Calm"));
        }

    }


    public void onEnterStance() {

        if (sfxId != -1L) {
            stopIdleSfx();
        }

        //CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        //sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SKY, true));


    }


    public void onExitStance() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player,player, new AngryPower(player, 2)));
        stopIdleSfx();
    }


    public void stopIdleSfx() {
        if (sfxId != -1L) {
            //CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
            sfxId = -1L;
        }
    }
}