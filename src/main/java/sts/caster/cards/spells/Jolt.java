package sts.caster.cards.spells;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.LightningDamageAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.RecurringSpellCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.interfaces.ICardWasElectrifiedSubscriber;
import sts.caster.patches.delayedCards.CastingQueuePileEnum;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class Jolt extends CasterCard implements ICardWasElectrifiedSubscriber {

    public static final String ID = CasterMod.makeID("Jolt");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ashes.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int BASE_DELAY = 1;
    private static final int BASE_DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int ECHO_GAIN = 1;

    public Jolt() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns =  BASE_DELAY;
        magicNumber = baseMagicNumber = ECHO_GAIN;
        selfRetain = true;
        setCardElement(MagicElement.ELECTRIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }
    
    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
            actions.add(new LightningDamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage, DamageInfo.DamageType.NORMAL), AttackEffect.LIGHTNING));
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

    @Override
    public int getIntentNumber() {
        return spellDamage;
    }

    @Override
    public void cardWasElectrified(CardGroup.CardGroupType gType) {
        if (gType == CastingQueuePileEnum.CASTER_CASTING_QUEUE) {
            this.flash(Color.GOLD.cpy());
            RecurringSpellCardMod.addRecurrence(this, magicNumber);
        }
    }
}
