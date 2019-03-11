package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.CasterCardTags;
import sts.caster.powers.RamuhPower;

public class ElectrifyCardAction extends AbstractGameAction {
    AbstractCard card;
    
    public ElectrifyCardAction(AbstractCard card) {
        this.card = card;
    }
    
    @Override
    public void update() {
       	card.flash();
    	card.tags.add(CasterCardTags.ELECTRIFIED);
    	if (AbstractDungeon.player.hasPower(RamuhPower.POWER_ID)) {
    		card.modifyCostForTurn(-card.costForTurn);
    	}
    	this.isDone = true;
    }
}
