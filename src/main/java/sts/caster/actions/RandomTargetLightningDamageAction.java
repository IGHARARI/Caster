package sts.caster.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.ImpactSparkEffect;
import sts.caster.cards.CasterCard;
import sts.caster.core.MagicElement;

public class RandomTargetLightningDamageAction extends AbstractGameAction {

	private DamageInfo info;
    private static final float DURATION = 0.1f;
    private static final float POST_ATTACK_WAIT_DUR = 0.1f;
    private boolean skipWait;
    private boolean muteSfx;
    
    public RandomTargetLightningDamageAction(final DamageInfo info, final AttackEffect effect) {
        this.skipWait = false;
        this.muteSfx = false;
        this.info = info;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
    }
    
    @Override
    public void update() {
    	target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS) {
            this.isDone = true;
            return;
        }
        if (this.duration == DURATION) {
            if (this.info.type != DamageInfo.DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead)) {
                this.isDone = true;
                return;
            }
//            this.target.damageFlash = true;
//            this.target.damageFlashFrames = 5;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, this.muteSfx));
            CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.15f);
            for (int i = 0; i < 20; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(new ImpactSparkEffect(this.target.hb.cX + MathUtils.random(-20.0f, 20.0f) * Settings.scale + 150.0f * Settings.scale, this.target.hb.cY + MathUtils.random(-20.0f, 20.0f) * Settings.scale));
            }
        }
        this.tickDuration();
        if (this.isDone) {
            this.target.tint.color = Color.YELLOW.cpy();
            this.target.tint.changeColor(Color.WHITE.cpy());
            CasterCard.customApplyEnemyPowersToSpellDamage(info, MagicElement.ELECTRIC, target);
            this.target.damage(this.info);
//            addToBot(new ApplyElementalEffectChanceAction(AbstractDungeon.player, target, MagicElement.ELECTRIC, 1, 1, 1));
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            if (!this.skipWait && !Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(POST_ATTACK_WAIT_DUR));
            }
        }
    }
}
