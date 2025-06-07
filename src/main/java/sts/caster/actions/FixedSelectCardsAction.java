package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// The oriringal STS SelectCardsAction class is bugged when anyNumber=false
public class FixedSelectCardsAction extends AbstractGameAction {
    private Consumer<List<AbstractCard>> callback;
    private String text;
    private boolean anyNumber;
    private CardGroup selectGroup;

    public FixedSelectCardsAction(ArrayList<AbstractCard> group, int amount, String textForSelect, boolean anyNumber, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.text = textForSelect;
        this.anyNumber = anyNumber;
        this.callback = callback;
        this.selectGroup = new CardGroup(CardGroupType.UNSPECIFIED);
        this.selectGroup.group.addAll((Collection)group.stream().distinct().filter(cardFilter).collect(Collectors.toList()));
    }

    public FixedSelectCardsAction(ArrayList<AbstractCard> group, String textForSelect, boolean anyNumber, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this(group, 1, textForSelect, anyNumber, cardFilter, callback);
    }

    public FixedSelectCardsAction(ArrayList<AbstractCard> group, String textForSelect, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this(group, 1, textForSelect, false, cardFilter, callback);
    }

    public FixedSelectCardsAction(ArrayList<AbstractCard> group, String textForSelect, Consumer<List<AbstractCard>> callback) {
        this(group, 1, textForSelect, false, (c) -> true, callback);
    }

    public FixedSelectCardsAction(ArrayList<AbstractCard> group, int amount, String textForSelect, Consumer<List<AbstractCard>> callback) {
        this(group, amount, textForSelect, false, (c) -> true, callback);
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.selectGroup.size() == 0 || this.callback == null) {
                this.isDone = true;
                return;
            }

            if (this.selectGroup.size() <= this.amount && !this.anyNumber) {
                this.callback.accept(this.selectGroup.group);
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(this.selectGroup, this.amount, this.anyNumber, this.text);
            AbstractDungeon.gridSelectScreen.forClarity = false; // This line is missing and causes a bug
            this.tickDuration();
        }

        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            this.callback.accept(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
            this.isDone = true;
        } else {
            this.tickDuration();
        }
    }
}
