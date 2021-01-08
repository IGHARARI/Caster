package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;
import static sts.caster.core.CasterMod.makeVFXPath;

import java.util.ArrayList;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sts.caster.actions.ModifyCardInBattleSpellDamageAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.util.TextureHelper;

public class Explosion extends CasterCard {

    public static final String ID = CasterMod.makeID("Explosion");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("explosion.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_DAMAGE = 33;
    private static final int BASE_DOWNGRADE = 9;
    private static final int UPG_DOWNGRADE = -4;


    public Explosion() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseSpellDamage = spellDamage =  BASE_DAMAGE;
        magicNumber = baseMagicNumber = BASE_DOWNGRADE;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new QueueDelayedCardAction(this, delayTurns, m));
		addToBot(new ModifyCardInBattleSpellDamageAction(this, -magicNumber));
    }
    
    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();

            Texture magicCircle = TextureHelper.getTexture(makeVFXPath("magiccircle.png"));
            Texture shine = TextureHelper.getTexture(makeVFXPath("explosionshine.png"));
            float scaleRatio = Math.max(t.hb.height / magicCircle.getHeight(), t.hb.width / magicCircle.getWidth());
            AbstractGameEffect explosionVfx = new VfxBuilder(magicCircle, t.hb.cX,t.hb.cY, 1.8f)
                    .fadeIn(.5f)
                    .setScale(scaleRatio)
                    .rotate(100f)
                    .playSoundAt(0.2f, -.5f, "ORB_SLOT_GAIN")
//                    .playSoundAt(0.33f, .5f, "TINGSHA")
//                    .playSoundAt(0.66f, .5f, "TINGSHA")
//                    .playSoundAt(1f, .5f, "TINGSHA")
                    .playSoundAt(1.4f, -.3f, "ATTACK_FIRE")
                    .playSoundAt(1.4f, -.1f, "ATTACK_FIRE")
                    .playSoundAt(1.6f, .3f, "ATTACK_FIRE")
                    .emitEvery(
                            (x,y) -> {
                                float randX = MathUtils.random(-magicCircle.getWidth()/2, magicCircle.getWidth()/2);
                                float randY = MathUtils.random(-magicCircle.getHeight()/2, magicCircle.getHeight()/2);
                                float randScale = MathUtils.random(1f/4, 1f);
                                AbstractGameEffect littleBall = new VfxBuilder(shine, t.hb.cX + randX, t.hb.cY + randY, 1f)
                                        .setScale(randScale)
                                        .fadeIn(.2f)
                                        .fadeOut(.5f)
                                        .build();
                                littleBall.renderBehind = false;
                                return littleBall;
                            },
                            0.1f
                    )
                    .build();
            explosionVfx.renderBehind = true;

            actionsList.add(new VFXAction(explosionVfx, 1.5f));
    		actionsList.add(new DamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage), AttackEffect.FIRE));
    		return actionsList;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeMagicNumber(UPG_DOWNGRADE);
        }
    }
}
