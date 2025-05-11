package sts.caster.variables;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import sts.caster.cards.CasterCard;

import static sts.caster.core.CasterMod.makeID;

public class SecondMagicNumber extends DynamicVariable {
    @Override
    public String key() {
        return makeID("M2");
    }

    @Override
    public boolean isModified(AbstractCard card) {
    	if (!(card instanceof CasterCard)) return false;
        return ((CasterCard) card).isM2Modified;

    }

    @Override
    public int value(AbstractCard card) {
    	if (!(card instanceof CasterCard)) return 0;
        return ((CasterCard) card).m2;
    }

    @Override
    public int baseValue(AbstractCard card) {
    	if (!(card instanceof CasterCard)) return 0;
        return ((CasterCard) card).baseM2;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
    	if (!(card instanceof CasterCard)) return false;
        return ((CasterCard) card).upgradedM2;
    }
    
    @Override
    public Color getDecreasedValueColor() {
    	return Settings.RED_TEXT_COLOR.cpy();
    }
    
    @Override
    public Color getIncreasedValueColor() {
    	return Settings.GREEN_TEXT_COLOR.cpy();
    }
}