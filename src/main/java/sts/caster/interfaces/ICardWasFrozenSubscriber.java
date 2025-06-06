package sts.caster.interfaces;

import com.megacrit.cardcrawl.cards.CardGroup;

public interface ICardWasFrozenSubscriber {
    public void cardWasFrozen(CardGroup.CardGroupType thisCardGroup);
}
