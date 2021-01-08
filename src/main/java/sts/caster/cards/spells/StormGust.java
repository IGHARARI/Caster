package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;
import static sts.caster.core.CasterMod.makeVFXPath;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;
import com.megacrit.cardcrawl.vfx.combat.LightBulbEffect;
import sts.caster.actions.*;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.interfaces.MonsterToActionInterface;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.powers.FrostPower;
import sts.caster.util.TextureHelper;

public class StormGust extends CasterCard {

    public static final String ID = CasterMod.makeID("StormGust");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("stormgust.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_DELAY = 2;
    private static final int BASE_DAMAGE = 32;
    private static final int BASE_FROST = 3;
    private static final int UPG_FROST = 2;
    private static final int BASE_CARDS_FROZEN = 2;
    private static final int UPG_CARDS_FROZEN = -1;


    public StormGust() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = BASE_CARDS_FROZEN;
        m2 = baseM2 = BASE_FROST;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new FreezeCardAction(magicNumber, false, false, false));
		addToBot(new QueueDelayedCardAction(this, delayTurns, null));
    }
    
    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
//        AbstractGameEffect fallingShard = new VfxBuilder(iceShard, randStartingPoint, 0,  0.5f)
//                .setAngle(45)
//                .setScale(1f/8)
//                .moveX(randStartingPoint, randStartingPoint + 200f, VfxBuilder.Interpolations.LINEAR)
//                .moveY(0, AbstractDungeon.player.drawX).build();
        Random shardRandom = new Random();
        BiFunction<Float, Float, AbstractGameEffect> iceShardBuilder = (randStartingPoint, randEndingPoint) -> {
            Texture iceShard = TextureHelper.getTexture(makeVFXPath("iceshard.png"));
            float screenTop = Settings.HEIGHT * Settings.scale;
            float screenBot = AbstractDungeon.player.drawY;
            // Have triangle formed by:
            // v1 (randStartingPoint, screenTop)
            // v2 (randStartingPoint, screenBot)
            // v3 (randEndingPoint, screenBot)
            // -> take v1 to origin
            // v2 (randStartingPoint - randStartingPoint, screenBot - screenTop) = (x1, y1)
            // v3 (randEndingPoint - randStartingPoint, screenBot - screenTop) = (x2, y2)
            double xv2 = randEndingPoint - randStartingPoint;
            double yv2 = screenBot - screenTop;
            double randAngle = Math.toDegrees(Math.atan2(yv2, xv2)) + 90f;
            float randomScaling =  shardRandom.nextFloat()*2;
            return new VfxBuilder(iceShard, randStartingPoint, screenTop, 0.33f / (1+randomScaling))
                    .setAngle((float) randAngle)
                    .setScale((1f + randomScaling) / 8)
                    .moveX(randStartingPoint, randEndingPoint, VfxBuilder.Interpolations.LINEAR)
                    .moveY(screenTop, screenBot, VfxBuilder.Interpolations.LINEAR)
                    .andThen(0.1f)
                    .fadeOut(0.1f)
                    .triggerVfxAt(0.1f, 1,
                            (x2, y2) -> {
                                return new DamageImpactCurvyEffect(x2, y2, Color.BLUE.cpy(), true);
                            }
                    )
                    .build();
        };
        AbstractGameEffect storm = new VfxBuilder(2)
                .emitEvery(
                    (x,y) -> {
                        float halfScreen = Settings.WIDTH * Settings.scale / 2;
                        float randStartingPoint = halfScreen * (1 + shardRandom.nextFloat());
                        float randEndingPoint = halfScreen * (1 + shardRandom.nextFloat());
                        return iceShardBuilder.apply(randStartingPoint, randEndingPoint);
                    },
                    0.02f
                )
                .playSoundAt(.60f, "ATTACK_DAGGER_4")
                .playSoundAt(.75f, "ATTACK_DAGGER_4")
                .playSoundAt(.90f, "ATTACK_DAGGER_4")
                .playSoundAt(1.05f, "ATTACK_DAGGER_4")
                .playSoundAt(1.25f, "ATTACK_DAGGER_4")
                .build();

    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
    		actions.add(new VFXAction(storm));
    		actions.add(new NonSkippableWaitAction(1f));
        	actions.add(new DelayedDamageAllEnemiesAction(AbstractDungeon.player, c.spellDamage, c.cardElement, AttackEffect.SMASH));
            MonsterToActionInterface frostApply = (mon) -> {
                return new ApplyPowerAction(mon, AbstractDungeon.player, new FrostPower(mon, AbstractDungeon.player, c.m2), c.m2);
            };
            actions.add(new DelayedActionOnAllEnemiesAction(frostApply));
            return actions;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeM2(UPG_FROST);
            upgradeMagicNumber(UPG_CARDS_FROZEN);
            initializeDescription();
        }
    }
}
