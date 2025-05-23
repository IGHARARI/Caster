package sts.caster.interfaces;

import com.megacrit.cardcrawl.cards.CardGroup;

public interface ICardWasElectrifiedSubscriber {
    public void cardWasElectrified(CardGroup.CardGroupType thisCardGroup);
}
