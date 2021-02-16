package sts.caster.cards.spells;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.util.TextureHelper;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;
import static sts.caster.core.CasterMod.makeVFXPath;

public class Meteor extends CasterCard {

    public static final String ID = CasterMod.makeID("Meteor");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("meteor.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DELAY_TURNS = 2;
    private static final int BASE_DAMAGE = 15;
    private static final int UPGRADE_DAMAGE = 5;


    public Meteor() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns = DELAY_TURNS;
        cardElement = MagicElement.NEUTRAL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();

            Texture meteor = TextureHelper.getTexture(makeVFXPath("meteor.png"));
            float screenTop = Settings.HEIGHT * Settings.scale;
            float charX = AbstractDungeon.player.drawX;
            AbstractGameEffect meteorvfx = new VfxBuilder(meteor, charX, screenTop, 1f)
                    .setAngle(15f)
                    .setScale(0.6f)
                    .moveX(charX, t.hb.cX, VfxBuilder.Interpolations.EXP5IN)
                    .moveY(screenTop, t.hb.y, VfxBuilder.Interpolations.EXP5IN)
                    .playSoundAt(0.1f, "ATTACK_FLAME_BARRIER")
                    .andThen(0.5f)
                    .fadeOut(0.3f)
                    .triggerVfxAt(1f, 1,
                            (x2, y2) -> {
                                return new FireballEffect(x2, y2, x2 + 10f, y2 + 10f);
                            }
                    )
                    .playSoundAt(.6f, "BLUNT_HEAVY")
                    .build();

            actions.add(new VFXAction(meteorvfx, 1f));
            actions.add(new DamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage, DamageType.NORMAL), AttackEffect.FIRE));
            return actions;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeSpellDamage(UPGRADE_DAMAGE);
        }
    }
}
