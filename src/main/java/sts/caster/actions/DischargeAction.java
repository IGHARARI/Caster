package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.SwordBoomerangAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCardTags;

public class DischargeAction extends AbstractGameAction {
	AbstractMonster target;
	
	public DischargeAction(int damage) {
        actionType = ActionType.DAMAGE;
        amount = damage;
	}

	@Override
    public void update() {
		int elecStacks = 0;
		for (AbstractCard card : AbstractDungeon.player.hand.group) {
			if (card.hasTag(CasterCardTags.ELECTRIFIED)) {
				card.flash();
				int tagsBeforeRemove = card.tags.size();
				card.tags.removeIf((tag) -> tag == CasterCardTags.ELECTRIFIED);
				int amountElectrified = tagsBeforeRemove - card.tags.size();
				elecStacks += amountElectrified;
			}
		}
		if (elecStacks > 0) {
			addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
			addToBot(new SwordBoomerangAction(AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng), new DamageInfo(AbstractDungeon.player, amount), elecStacks));
		}
		isDone = true;
    }
}
