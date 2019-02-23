package sts.caster.variables;

import basemod.abstracts.DynamicVariable;
import sts.caster.cards.CasterCard;

import static sts.caster.core.CasterMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class SpellDamage extends DynamicVariable {

    @Override
    public String key() {
        return makeID("D");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((CasterCard) card).isSpellDamageModified;

    }

    @Override
    public int value(AbstractCard card) {
        return ((CasterCard) card).spellDamage;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((CasterCard) card).baseSpellDamage;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((CasterCard) card).upgradedSpellDamage;
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