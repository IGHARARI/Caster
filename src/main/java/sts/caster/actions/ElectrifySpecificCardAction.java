package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.CasterCard;
import sts.caster.cards.CasterCardTags;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.powers.GainPower;
import sts.caster.powers.RamuhPower;
import sts.caster.powers.StaticFieldPower;
import sts.caster.util.PowersHelper;

public class ElectrifySpecificCardAction extends AbstractGameAction {
    AbstractCard card;
    int amount;
    
    public ElectrifySpecificCardAction(AbstractCard card) {
        this(card, 1);
    }

    public ElectrifySpecificCardAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    // This is so bad why are these here and not in their respective powers or something? ugh.
    @Override
    public void update() {
       	card.flash();
       	for (int i = 0; i < amount; i++) {
			card.tags.add(CasterCardTags.ELECTRIFIED);
			if (AbstractDungeon.player.hasPower(StaticFieldPower.POWER_ID)) {
				AbstractDungeon.player.getPower(StaticFieldPower.POWER_ID).onSpecificTrigger();
			}
            if (AbstractDungeon.player.hasPower(RamuhPower.POWER_ID)) {
                card.setCostForTurn(card.costForTurn -1);
            }

            if (card instanceof CasterCard && card.type == CasterCardType.SPELL) {
                int gainPowerAmount = PowersHelper.getPlayerPowerAmount(GainPower.POWER_ID);
                if(gainPowerAmount > 0 && ((CasterCard) card).baseSpellDamage > 0) {
                    ((CasterCard) card).upgradeSpellDamage(gainPowerAmount);
                    ((CasterCard) card).isSpellDamageModified = true;
                }
            }
		}
    	this.isDone = true;
    }
}
