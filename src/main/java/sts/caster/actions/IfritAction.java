package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.powers.BlazedPower;

public class IfritAction extends AbstractGameAction {

	public IfritAction() {
        actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;
	}

	final private int IFRIT_BLAZE_AMOUNT = 5;

	@Override
    public void update() {
		if (duration == Settings.ACTION_DUR_MED) {
			AbstractPlayer p = AbstractDungeon.player;
			if (p.hand.size() == 0) {
				isDone = true;
				return;
			}
            AbstractCard randomCard = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);

            AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
            addToBot(new ApplyPowerAction(m, p, new BlazedPower(m, p, IFRIT_BLAZE_AMOUNT), IFRIT_BLAZE_AMOUNT));
            addToBot(new NonSkippableWaitAction(0.2f));
            
            AbstractMonster m2 = AbstractDungeon.getMonsters().getRandomMonster(true);
            final AbstractCard tmp = randomCard.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = randomCard.current_x;
            tmp.current_y = randomCard.current_y;
            tmp.target_x = Settings.WIDTH / 2.0f - 300.0f * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0f;
            tmp.freeToPlayOnce = true;
            tmp.calculateCardDamage(m2);
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m2));
            addToBot(new BurnAction(randomCard, p.hand));
            
		}
        tickDuration();
    }
}
