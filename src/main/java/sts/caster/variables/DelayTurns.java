package sts.caster.variables;

import basemod.abstracts.DynamicVariable;
import sts.caster.cards.CasterCard;

import static sts.caster.core.CasterMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class DelayTurns extends DynamicVariable {

    @Override
    public String key() {
        return makeID("Delay");
    }

    @Override
    public boolean isModified(AbstractCard card) {
    	if (!(card instanceof CasterCard)) return false;
        return ((CasterCard) card).isDelayTurnsModified;
    }

    @Override
    public int value(AbstractCard card) {
    	if (!(card instanceof CasterCard)) return 0;
        return ((CasterCard) card).delayTurns;
    }

    @Override
    public int baseValue(AbstractCard card) {
    	if (!(card instanceof CasterCard)) return 0;
        return ((CasterCard) card).baseDelayTurns;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
    	if (!(card instanceof CasterCard)) return false;
        return ((CasterCard) card).upgradedDelayTurns;
    }
    
    @Override
    public Color getDecreasedValueColor() {
    	return Settings.GREEN_TEXT_COLOR.cpy();
    }
    
    @Override
    public Color getIncreasedValueColor() {
    	return Settings.RED_TEXT_COLOR.cpy();
    }
}