package sts.caster.variables;

import basemod.abstracts.DynamicVariable;
import sts.caster.cards.CasterCard;

import static sts.caster.CasterMod.makeID;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class DelayTurns extends DynamicVariable {

    //For in-depth comments, check the other variable(DefaultCustomVariable). It's nearly identical.

    @Override
    public String key() {
        return makeID("Delay");
        // This is what you put between "!!" in your card strings to actually display the number.
        // You can name this anything (no spaces), but please pre-phase it with your mod name as otherwise mod conflicts can occur.
        // Remember, we're using makeID so it automatically puts "theDefault:" (or, your id) before the name.
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((CasterCard) card).isDelayTurnsModified;

    }

    @Override
    public int value(AbstractCard card) {
        return ((CasterCard) card).delayTurns;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((CasterCard) card).baseDelayTurns;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((CasterCard) card).upgradedDelayTurns;
    }
}