package Spansion.Orbs;

import Spansion.Spansion;
import Spansion.util.TextureLoader;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;
import sun.java2d.Spans;

import java.util.ArrayList;

import static Spansion.Spansion.logger;
import static Spansion.Spansion.makeOrbPath;

public class ToxicOrb extends AbstractOrb {
    //Toxic Orb
    //    Passive : At the start of your turn, apply 2 poison to a random enemy.
    //    Evoke   : Apply 3 poison to a random enemy, then trigger ALL poison.

    public static final String ORB_ID = Spansion.makeID(ToxicOrb.class.getSimpleName());
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;

    private static final Texture IMG = TextureLoader.getTexture(makeOrbPath("ToxicOrb.png"));
    //Animation Rendering Numbers
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    public ToxicOrb(){
        ID = ORB_ID;
        name = orbString.NAME;
        img = IMG;

        evokeAmount = baseEvokeAmount = 3;
        passiveAmount = basePassiveAmount = 2;

        updateDescription();

        angle = MathUtils.random(360.0f);
        channelAnimTimer = 0.05f;
    }

    @Override
    public void updateDescription() {
        applyFocus();
        description = DESC[0] + passiveAmount + DESC[1] + DESC[2] + evokeAmount;
    }

    @Override
    public void onEvoke() {
        AbstractMonster mon = AbstractDungeon.getRandomMonster();
        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction( mon, player, new PoisonPower(mon, player, evokeAmount))
        );
        // TODO: Is there a way to trigger all poison now?
        ArrayList<AbstractMonster> monGroup = AbstractDungeon.getMonsters().monsters;
        for(int i = 0; i < monGroup.size(); i++){
            AbstractMonster tar = monGroup.get(i);
            PoisonPower poison = (PoisonPower)monGroup.get(i).getPower(PoisonPower.POWER_ID);
            if(poison != null){
                logger.info(">> Hitting " + tar.name + " with " + poison.amount + " poison.");
                AbstractDungeon.actionManager.addToBottom(
                        new PoisonLoseHpAction(tar, tar, poison.amount, AbstractGameAction.AttackEffect.POISON));
            }
        }

        AbstractDungeon.actionManager.addToBottom(
                new SFXAction("STS_SFX_PoisonApply_v1.ogg")
        );
    }

    @Override
    public void onEndOfTurn() {
        // Passive
        AbstractMonster mon = AbstractDungeon.getRandomMonster();
        AbstractDungeon.actionManager.addToBottom(
                new VFXAction( new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA),0.1f)
        );

        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction( mon, player, new PoisonPower(mon, player, passiveAmount))
        );
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        angle += Gdx.graphics.getDeltaTime() * 45;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if(vfxTimer < 0.0f){
            AbstractDungeon.effectList.add(new PlasmaOrbPassiveEffect(cX, cY));
            vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, c.a / 2.0f));
        sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale + MathUtils.sin(angle/PI_4)*ORB_WAVY_DIST * Settings.scale, scale, angle, 0, 0, 96, 96, false, false);
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, this.c.a / 2.0f));
        sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale, scale + MathUtils.sin(angle/PI_4)*ORB_WAVY_DIST * Settings.scale, -angle, 0, 0, 96, 96, false, false);
        sb.setBlendFunction(770, 771);
        renderText(sb);
        hb.render(sb);

    }

    @Override
    public void triggerEvokeAnimation() {
        AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(cX, cY));
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new ToxicOrb();
    }
}
