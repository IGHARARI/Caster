package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.core.frozenpile.FrozenPileManager;

@Deprecated
public class DeprecatedFreezeSpecificCardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FreezeCardAction");
    public static final String[] TEXT = DeprecatedFreezeSpecificCardAction.uiStrings.TEXT;
    private AbstractPlayer p;
    private AbstractCard card;
    private CardGroup originalGroup;

    public DeprecatedFreezeSpecificCardAction(final AbstractCard card) {
        this(null, card);
    }

    public DeprecatedFreezeSpecificCardAction(final CardGroup originalGroup, final AbstractCard card) {
        this.p = AbstractDungeon.player;
        this.card = card;
        this.originalGroup = originalGroup;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (originalGroup == null) {
                if (p.hand.contains(card)){
                    originalGroup = p.hand;
                } else if (p.discardPile.contains(card)){
                    originalGroup = p.discardPile;
                } else if (p.exhaustPile.contains(card)){
                    originalGroup = p.exhaustPile;
                } else if (p.drawPile.contains(card)){
                    originalGroup = p.drawPile;
                } else if (p.limbo.contains(card)){
                    originalGroup = p.limbo;
                } else  {
                    // Something has gone awry :(
                    this.tickDuration();
                    return;
                }
            }
            FrozenPileManager.moveToFrozenPile(originalGroup, card);
        }
        this.tickDuration();
    }
}
