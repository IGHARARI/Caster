package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.delayedCards.RewindingCodexManager;
import sts.caster.util.BattleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RewindingCodexAction extends AbstractGameAction {
    private UUID codexId;
    private List<AbstractCard> remainingSpells;
    private boolean isRecurring;

    public RewindingCodexAction(UUID codexId) {
        this.codexId = codexId;
        this.isRecurring = false;
    }

    protected RewindingCodexAction(List<AbstractCard> remaining) {
        this.remainingSpells = remaining;
        this.isRecurring = true;
    }

    
    @Override
    public void update() {
        if (!isRecurring) {
            ArrayList<AbstractCard> priorSpellsRewound = RewindingCodexManager.spellsRecastedThisCombat.get(codexId);
            if (priorSpellsRewound == null) {
                priorSpellsRewound = new ArrayList<>();
                RewindingCodexManager.spellsRecastedThisCombat.put(codexId, priorSpellsRewound);
            }
            priorSpellsRewound.add(RewindingCodexManager.lastSpellUsed.makeSameInstanceOf());
            addToBot(new RewindingCodexAction(priorSpellsRewound));
        } else if (this.remainingSpells != null && this.remainingSpells.size() > 0) {
            AbstractCard spellToPlay = this.remainingSpells.get(this.remainingSpells.size() -1);
            AbstractMonster m = BattleHelper.getRandomAliveMonster(AbstractDungeon.getMonsters(), AbstractDungeon.cardRandomRng);
            AbstractCard tmp = spellToPlay.makeSameInstanceOf();
            addToBot(new ShowCardVeryBrieflyAction(tmp, 1f, 1f, false));
            if (m != null)
                tmp.calculateCardDamage(m);
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, tmp.energyOnUse, true, true), true);
            List<AbstractCard> restOfSpells = this.remainingSpells.subList(0, this.remainingSpells.size() - 1);
            if (restOfSpells.size() > 0) {
                addToBot(new RewindingCodexAction(restOfSpells));
            }
        }
        this.isDone = true;
    }

}
