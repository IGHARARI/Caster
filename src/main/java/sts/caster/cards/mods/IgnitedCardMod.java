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

import java.util.ArrayList;

public class IgnitedCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("IgnitedCardMod");
    public static final String ID = CasterMod.makeID("IgnitedCardMod");
    public static final int PER_IGNITE_DAMAGE = 2;
    private int ignitedAmount;
    private boolean modifiedDamage;
    private boolean modifiedSpellDamage;

    public IgnitedCardMod() {
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return uiStrings.TEXT[0] +" x"+ ignitedAmount + " NL " + rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
//        addToBot(new ExhaustSpecificCardAction(card));
        action.exhaustCard = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        removeIgnited(card);
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    private void increaseIgnited(AbstractCard card, IgnitedCardMod other) {
        other.ignitedAmount += 1;
        if (card.type == AbstractCard.CardType.ATTACK && card.baseDamage > 0) {
            card.baseDamage += PER_IGNITE_DAMAGE;
        }
        if (card instanceof CasterCard && ((CasterCard) card).baseSpellDamage > 0) {
            ((CasterCard) card).baseSpellDamage += PER_IGNITE_DAMAGE;
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
        ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, IgnitedCardMod.ID);
        for (AbstractCardModifier other : list) {
            increaseIgnited(card, (IgnitedCardMod)other);
            card.initializeDescription();
            card.applyPowers();
            return false;
        }
        increaseIgnited(card, this);
        card.initializeDescription();
        card.applyPowers();
        card.flash(Color.RED.cpy());
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new IgnitedCardMod();
    }
}
