package sts.caster.cards.spells;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.actions.ThawCardAction;
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

public class Fireball extends CasterCard {

    public static final String ID = CasterMod.makeID("Fireball");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("meteor.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 2;
    private static final int BASE_DAMAGE = 12;
    private static final int DMG_UPGRADE = 3;
    private static final int BASE_THAW = 2;


    public Fireball() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = BASE_THAW;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ThawCardAction(magicNumber, false, this.name));
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent, UUID originalUUID) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            Texture meteor = TextureHelper.getTexture(makeVFXPath("meteor.png"));
            float charX = AbstractDungeon.player.drawX + AbstractDungeon.player.hb.width * 0.5f;
            float charY = AbstractDungeon.player.drawY + AbstractDungeon.player.hb.height * 0.5f;
            float targetX = t.hb.cX - t.hb.width/4;
            float targetY = t.hb.cY;

            AbstractGameEffect fireball = new VfxBuilder(meteor, charX, charY, 0.6f) // Charge phase duration
                    .setScale(0.1f)
                    .useAdditiveBlending()
                    .fadeIn(0.1f)
                    .setColor(Color.RED.cpy())
                    .rotate(920f) // full spin while charging
                    .scale(0.1f, 0.5f, VfxBuilder.Interpolations.ELASTICOUT)
                    .playSoundAt(0.1f, "ATTACK_FLAME_BARRIER") // charging sound
                    .andThen(0.4f) // Launch phase
                    .moveX(charX, targetX, VfxBuilder.Interpolations.EXP5IN)
                    .moveY(charY, targetY, VfxBuilder.Interpolations.EXP5IN)
                    .playSoundAt(0.05f, "BLUNT_HEAVY") // impact sound
                    .andThen(0.2f)
                    .triggerVfxAt(0.2f, 1, (x, y) -> new FireballEffect(x, y, x + 10f, y + 10f)) // impact fx
                    .fadeOut(0.2f)
                    .build();

            actionsList.add(new VFXAction(fireball, 1.4f));
            actionsList.add(new DamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage), AttackEffect.FIRE));
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeSpellDamage(DMG_UPGRADE);
        }
    }

    @Override
    public int getIntentNumber() {
        return spellDamage;
    }
}
