package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import sts.caster.powers.BlazedPower;

public class IncinerateAction extends AbstractGameAction {
    
	AbstractPlayer p;
    private boolean anyNumber;
    private boolean canPickZero;
    public static int numExhausted;
    private int energyBurnt;
    private int burnMultiplier;
	
    public IncinerateAction(AbstractCreature target, final int amount, final int burnMultiplier) {
        this.anyNumber = true;
        this.canPickZero = true;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
        this.p = AbstractDungeon.player;
        energyBurnt = 0;
        this.target = target;
        this.burnMultiplier = burnMultiplier;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            if (!this.anyNumber && this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                for (int i = 0; i < p.hand.size(); i++) {
                    final AbstractCard c = p.hand.getTopCard();
                    addToBot(new BurnAction(c, p.hand));
                    if (c.cost == -1) {
                    	energyBurnt += EnergyPanel.totalCount;
                    } else if (c.cost >= 0) {
                    	energyBurnt += c.cost;
                    }
                }
                if (energyBurnt > 0) {
                	addToBot(new ApplyPowerAction(target, p, new BlazedPower(target, p, energyBurnt*burnMultiplier), energyBurnt*burnMultiplier));
                }
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(ExhaustAction.TEXT[0], this.amount, this.anyNumber, this.canPickZero);
            this.tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (final AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
            	addToBot(new BurnAction(c, AbstractDungeon.handCardSelectScreen.selectedCards));
                if (c.cost == -1) {
                	energyBurnt += EnergyPanel.totalCount;
                } else if (c.cost >= 0) {
                	energyBurnt += c.cost;
                }
            }
            if (energyBurnt > 0) {
            	addToBot(new ApplyPowerAction(target, p, new BlazedPower(target, p, energyBurnt*burnMultiplier), energyBurnt*burnMultiplier));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        this.tickDuration();
    }
}
