package sts.caster.interfaces;

import com.megacrit.cardcrawl.cards.CardGroup;

public interface ICardWasIgnitedSubscriber {
    public void cardWasIgnited(CardGroup.CardGroupType thisCardGroup);
}
