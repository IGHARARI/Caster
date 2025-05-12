package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

@Deprecated
public class DeprecatedFreezeInHellAction extends AbstractGameAction {
    @Override
    @Deprecated
    public void update() {

    }
//
//	AbstractPlayer p;
//
//	public DeprecatedFreezeInHellAction(AbstractPlayer p) {
//		this.p = p;
//	}
//
//	@Override
//    public void update() {
//    	if (!isDone && !DeprecatedFrozenPileManager.frozenPile.group.isEmpty()) {
//			for (AbstractCard cardToPlay : DeprecatedFrozenPileManager.frozenPile.group) {
//				AbstractDungeon.actionManager.addToBottom(new ArbitraryCardAction(cardToPlay, (c) -> playCard(c)));
//			}
//    	}
//        isDone = true;
//    }
//
//	private void playCard(AbstractCard card) {
////		AbstractDungeon.getCurrRoom().souls.remove(card);
//		AbstractDungeon.player.limbo.group.add(card);
//		card.current_y = -200.0F * Settings.scale;
//		card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
//		card.target_y = (float)Settings.HEIGHT / 2.0F;
//		card.targetAngle = 0.0F;
//		card.lighten(false);
//		card.drawScale = 0.12F;
//		card.targetDrawScale = 0.75F;
//		card.applyPowers();
//		AbstractMonster targetMonster = AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
//		card.exhaustOnUseOnce = true;
//		DeprecatedFrozenPileManager.frozenPile.removeCard(card);
//		this.addToTop(new NewQueueCardAction(card, targetMonster, true, true));
////		this.addToTop(new UnlimboAction(card));
//		if (!Settings.FAST_MODE) {
//			this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
//		} else {
//			this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
//		}
//	}
}
