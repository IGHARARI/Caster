package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.CasterCardTags;
import sts.caster.powers.RamuhPower;
import sts.caster.powers.StaticFieldPower;

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
		}
    	if (AbstractDungeon.player.hasPower(RamuhPower.POWER_ID)) {
    		card.setCostForTurn(card.costForTurn -1);
    	}
    	this.isDone = true;
    }
}
