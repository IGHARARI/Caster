package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import sts.caster.powers.BlazedPower;

public class IfritAction extends AbstractGameAction {

	public IfritAction() {
        actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;
	}

	@Override
    public void update() {
		if (duration == Settings.ACTION_DUR_MED) {
			AbstractPlayer p = AbstractDungeon.player;
            AbstractCard randomCard = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
            p.hand.moveToExhaustPile(randomCard);
            int energyBurnt = 0;
            if (randomCard.cost == -1) {
            	energyBurnt += EnergyPanel.totalCount;
            } else if (randomCard.cost >= 0) {
            	energyBurnt += randomCard.cost;
            }
            AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new BlazedPower(m, p, energyBurnt), energyBurnt));
            
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
            
		}
        tickDuration();
    }
}
