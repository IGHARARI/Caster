package sts.caster.cards.spells;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.PurgeField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.actions.TransmuteSoulAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.util.TextureHelper;

import java.util.ArrayList;
import java.util.UUID;

import static sts.caster.core.CasterMod.makeCardPath;
import static sts.caster.core.CasterMod.makeVFXPath;

public class TransmuteSoul extends CasterCard {

    public static final String ID = CasterMod.makeID("TransmuteSoul");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ashes.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_DELAY = 5;
    private static final int BASE_DAMAGE = 9;
    private static final int UPGRADE_DELAY = -2;

    public TransmuteSoul() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns = BASE_DELAY;
        cardElement = MagicElement.NEUTRAL;
        PurgeField.purge.set(this, true);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CasterMod.logger.info("Used transmute soul with card with uuid {}", this.uuid);
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent, UUID originalUUID) {
        TransmuteSoul thisCard = this;
        return (c, t) -> {
            ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
            AbstractMonster target = findHighestHPEnemy();
            actions.add(new VFXAction(createExplosionCircle(target), 1.4f));
            CasterMod.logger.info("Creating actions for transmute soul with card with uuid {}", originalUUID);
            actions.add(new TransmuteSoulAction(target, magicNumber, (TransmuteSoul) c, originalUUID));
            return actions;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeDelayTurns(UPGRADE_DELAY);
        }
    }


    private AbstractGameEffect createExplosionCircle(AbstractMonster m) {
        Texture demicircle = TextureHelper.getTexture(makeVFXPath("whitecircle.png"));
        float scaleRatio = Math.max(m.hb.height / demicircle.getHeight(), m.hb.width / demicircle.getWidth()) * 1.2f;
        AbstractGameEffect frontGlow = new VfxBuilder(demicircle, m.hb.cX, m.hb.cY, 0.4f)
                .setScale(0.1f)
                .setAlpha(0.5f)
                .scale(0.1f, scaleRatio*0.9f, VfxBuilder.Interpolations.CIRCLE)
//                .setColor(Color.YELLOW.cpy())
                .andThen(0.8f)
                .oscillateAlpha(0.3f, 0.7f,20f)
                .scale(0.9f, 1.2f, VfxBuilder.Interpolations.CIRCLEOUT)
                .andThen(0.8f)
                .fadeOutFromOriginalAlpha(0.8f)
                .scale(1.2f, 0.0f, VfxBuilder.Interpolations.ELASTICIN)
                .build();
        frontGlow.renderBehind = false;
        AbstractGameEffect behindGlow = new VfxBuilder(demicircle, m.hb.cX, m.hb.cY, 0.4f)
                .triggerVfxAt(0.0f, 1, (x, y) -> {
                    return frontGlow;
                })
                .setScale(0.1f)
                .fadeIn(0.1f)
                .scale(0.1f, scaleRatio*0.95f, VfxBuilder.Interpolations.CIRCLE)
//                .setColor(Color.WHITE.cpy())
                .playSoundAt(0.2f, -0.3f, "TINGSHA")
                .playSoundAt(0.25f, -0.8f, "TINGSHA")
                .andThen(0.8f)
                .oscillateScale(scaleRatio*0.95f, scaleRatio*1.05f,50f)
                .andThen(0.8f)
                .fadeOutFromOriginalAlpha(0.8f)
                .scale(1.2f, 0.3f, VfxBuilder.Interpolations.ELASTICIN)
                .build();
        behindGlow.renderBehind = true;
        return behindGlow;
    }

    private AbstractMonster findHighestHPEnemy() {
        AbstractMonster highestHPMon = null;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                if (highestHPMon == null || highestHPMon.currentHealth < m.currentHealth) highestHPMon = m;
            }
        }
        return highestHPMon;
    }
}
