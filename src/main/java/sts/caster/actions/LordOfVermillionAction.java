package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import sts.caster.cards.spells.LordOfVermillion;

public class LordOfVermillionAction extends AbstractGameAction {

    LordOfVermillion card;
    int energyOnUse;

    public LordOfVermillionAction(LordOfVermillion card, int energyOnUse) {
        this.card = card;
        this.energyOnUse = energyOnUse;
    }

    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (AbstractDungeon.player.hasRelic(ChemicalX.ID)) {
            effect += 2;
            AbstractDungeon.player.getRelic(ChemicalX.ID).flash();
        }
        addToBot(new QueueDelayedCardAction(card, card.delayTurns, effect, null));
        if (!card.freeToPlayOnce) {
            AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
        }
        this.isDone = true;
    }
}
