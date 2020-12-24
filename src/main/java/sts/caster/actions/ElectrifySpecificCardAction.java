package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.CasterCardTags;
import sts.caster.powers.RamuhPower;
import sts.caster.powers.StaticFieldPower;

public class ElectrifySpecificCardAction extends AbstractGameAction {
    AbstractCard card;
    
    public ElectrifySpecificCardAction(AbstractCard card) {
        this.card = card;
    }
    
    @Override
    public void update() {
       	card.flash();
    	card.tags.add(CasterCardTags.ELECTRIFIED);
    	if (AbstractDungeon.player.hasPower(RamuhPower.POWER_ID)) {
    		card.setCostForTurn(0);
    	}
    	if (AbstractDungeon.player.hasPower(StaticFieldPower.POWER_ID)) {
    		AbstractDungeon.player.getPower(StaticFieldPower.POWER_ID).onSpecificTrigger();
    	}
    	this.isDone = true;
    }
}
