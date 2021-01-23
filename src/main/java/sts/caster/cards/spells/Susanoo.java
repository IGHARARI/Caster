package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;
import java.util.function.Function;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import sts.caster.actions.ElectrifySpecificCardAction;
import sts.caster.actions.LightningDamageAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.actions.RemoveElectrifyAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.CasterCardTags;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;

public class Susanoo extends CasterCard {

    public static final String ID = CasterMod.makeID("Susanoo");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("susanoo.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_DAMAGE = 16;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int BONUS_DMG_PER_ELEC = 2;
    private static final int UPG_BONUS_DMG_PER_ELEC = 1;

    public Susanoo() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = BONUS_DMG_PER_ELEC;
        setCardElement(MagicElement.ELECTRIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for(int i = 0; i < 10; i++) {
            float offset = MathUtils.random(-m.hb_w/3, m.hb_w/3);
            addToBot(new VFXAction(new LightningEffect(m.drawX + offset, m.drawY), 0.1f));
            addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
        }
        addToBot(new LightningDamageAction(m, new DamageInfo(AbstractDungeon.player, damage, DamageType.NORMAL), AttackEffect.SLASH_VERTICAL));
        for (AbstractCard c : p.hand.group) {
            if (c.hasTag(CasterCardTags.ELECTRIFIED))
                addToBot(new RemoveElectrifyAction(c));
        }

        for (AbstractCard c : p.drawPile.group) {
            if (c.hasTag(CasterCardTags.ELECTRIFIED))
                addToBot(new RemoveElectrifyAction(c));
        }

        for (AbstractCard c : p.discardPile.group) {
            if (c.hasTag(CasterCardTags.ELECTRIFIED))
                addToBot(new RemoveElectrifyAction(c));
        }
    }
    
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += this.magicNumber * countElectrifiedStacks();
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += this.magicNumber * countElectrifiedStacks();
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    private int countElectrifiedStacks() {
        Function<AbstractCard, Long> countTags = (AbstractCard card) -> card.tags.stream().filter(t -> t.equals(CasterCardTags.ELECTRIFIED)).count();
        int totalStacks = 0;
        AbstractPlayer p = AbstractDungeon.player;

        for (AbstractCard c : p.hand.group) {
            totalStacks += countTags.apply(c);
        }

        for (AbstractCard c : p.drawPile.group) {
            totalStacks += countTags.apply(c);
        }

        for (AbstractCard c : p.discardPile.group) {
            totalStacks += countTags.apply(c);
        }

        return totalStacks;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPG_BONUS_DMG_PER_ELEC);
        }
    }
}
