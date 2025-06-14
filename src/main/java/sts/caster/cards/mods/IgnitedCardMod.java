package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.freeze.StancesHelper;

import java.util.ArrayList;

public class IgnitedCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("IgnitedCardMod");
    public static final String ID = CasterMod.makeID("IgnitedCardMod");
    public static final int PER_IGNITE_DAMAGE = 2;
    private int ignitedAmount;
    private boolean modifiedDamage;
    private boolean modifiedSpellDamage;
    private int stackImmediatelyAmount;

    public int getOnApplicationStacks(){
        return stackImmediatelyAmount;
    }

    public IgnitedCardMod() {
        this(1);
    }

    public IgnitedCardMod(int stackImmediatelyAmount) {
        this.stackImmediatelyAmount = stackImmediatelyAmount;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return uiStrings.TEXT[0] +" x"+ ignitedAmount + " NL " + rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        action.exhaustCard = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        removeIgnited(card);
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return false;
    }

    private void increaseIgnited(AbstractCard card, IgnitedCardMod other) {
        int stackAmount = this.getOnApplicationStacks();
        other.ignitedAmount += stackAmount;
        if (card.type == AbstractCard.CardType.ATTACK && card.baseDamage > 0) {
            card.baseDamage += PER_IGNITE_DAMAGE * stackAmount;
        }
        if (card instanceof CasterCard && ((CasterCard) card).baseSpellDamage > 0) {
            ((CasterCard) card).baseSpellDamage += PER_IGNITE_DAMAGE * stackAmount;
        }
    }

    private void removeIgnited(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK && card.baseDamage > 0) {
            card.baseDamage -= PER_IGNITE_DAMAGE * this.ignitedAmount;
        }
        if (card instanceof CasterCard && ((CasterCard) card).baseSpellDamage > 0) {
            ((CasterCard) card).baseSpellDamage -= PER_IGNITE_DAMAGE * this.ignitedAmount;
        }
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (StancesHelper.shouldTriggerElectroplasma(card, this)) {
            StancesHelper.triggerElectroplasma(card);
            return false;
        }
        ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, IgnitedCardMod.ID);
        for (AbstractCardModifier other : list) {
            IgnitedCardMod otherIgnite = (IgnitedCardMod) other;
            increaseIgnited(card, otherIgnite);
            card.initializeDescription();
//            if (AbstractDungeon.player!= null) card.applyPowers();
            return false;
        }
        increaseIgnited(card, this);
        card.initializeDescription();
//        if (AbstractDungeon.player!= null) card.applyPowers();
        card.flash(Color.RED.cpy());
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new IgnitedCardMod(this.ignitedAmount);
    }
}
