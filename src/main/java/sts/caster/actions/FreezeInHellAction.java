package sts.caster.actions;

import basemod.devcommands.hand.Hand;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sts.caster.core.CasterMod;
import sts.caster.core.frozenpile.FrozenPileManager;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class FreezeInHellAction extends AbstractGameAction {

	AbstractPlayer p;

	public FreezeInHellAction(AbstractPlayer p) {
		this.p = p;
	}

	@Override
    public void update() {
    	if (!isDone && !FrozenPileManager.frozenPile.group.isEmpty()) {
			for (AbstractCard cardToPlay : FrozenPileManager.frozenPile.group) {
				AbstractDungeon.actionManager.addToBottom(new ArbitraryCardAction(cardToPlay, (c) -> playCard(c)));
			}
    	}
        isDone = true;
    }

	private void playCard(AbstractCard card) {
//		AbstractDungeon.getCurrRoom().souls.remove(card);
		AbstractDungeon.player.limbo.group.add(card);
		card.current_y = -200.0F * Settings.scale;
		card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
		card.target_y = (float)Settings.HEIGHT / 2.0F;
		card.targetAngle = 0.0F;
		card.lighten(false);
		card.drawScale = 0.12F;
		card.targetDrawScale = 0.75F;
		card.applyPowers();
		AbstractMonster targetMonster = AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
		card.exhaustOnUseOnce = true;
		FrozenPileManager.frozenPile.removeCard(card);
		this.addToTop(new NewQueueCardAction(card, targetMonster, true, true));
//		this.addToTop(new UnlimboAction(card));
		if (!Settings.FAST_MODE) {
			this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
		} else {
			this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
		}
	}
}
