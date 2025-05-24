package sts.caster.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.ImpactSparkEffect;
import sts.caster.cards.spells.LightningBolt;

public class LightningBoltAction extends AbstractGameAction {

	private DamageInfo info;
    private static final float DURATION = 0.1f;
    private static final float POST_ATTACK_WAIT_DUR = 0.1f;
    private boolean skipWait;
    private boolean muteSfx;
    private LightningBolt sourceCard;

    public LightningBoltAction(final AbstractCreature target, final DamageInfo info, final AttackEffect effect, LightningBolt sourceCard) {
        this.skipWait = false;
        this.muteSfx = false;
        this.setValues(target, this.info = info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
        this.sourceCard = sourceCard;
    }

    @Override
    public void update() {

        if (this.duration == DURATION) {
            if (this.info.type != DamageInfo.DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead)) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, this.muteSfx));
            CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.15f);
            for (int i = 0; i < 20; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(new ImpactSparkEffect(this.target.hb.cX + MathUtils.random(-20.0f, 20.0f) * Settings.scale + 150.0f * Settings.scale, this.target.hb.cY + MathUtils.random(-20.0f, 20.0f) * Settings.scale));
            }
        }
        this.tickDuration();
        if (this.isDone && !this.shouldCancelAction()) {
            this.target.tint.color = Color.YELLOW.cpy();
            this.target.tint.changeColor(Color.WHITE.cpy());
            this.target.damage(this.info);
            if (
                target instanceof AbstractMonster
                && (
                    ((AbstractMonster) this.target).isDying || this.target.currentHealth <= 0
                )
                && !this.target.halfDead
            ) {
                sourceCard.upgradeNumberOfHits(1);
                addToTop(new QueueRecurringEffectAction(sourceCard, sourceCard.delayTurns, 0, (AbstractMonster) target));
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}
