package sts.caster.cards.spells;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.actions.ModifyCastingSpellsEffectAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.RecurringSpellCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.interfaces.ICardWasFrozenSubscriber;
import sts.caster.patches.delayedCards.CastingQueueGroupEnum;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static sts.caster.core.CasterMod.makeCardPath;

public class IceAge extends CasterCard implements ICardWasFrozenSubscriber {

    public static final String ID = CasterMod.makeID("IceAge");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ashes.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_DELAY = 1;
    private static final int BASE_BLOCK = 1;
    private static final int EXTRA_BLOCK = 1;
    private static final int UPGR_EXTRA_BLOCK = 1;
    private static final int RECURRENCE = 99;

    public IceAge() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseSpellBlock = spellBlock = BASE_BLOCK;
        baseMagicNumber = magicNumber = EXTRA_BLOCK;
        exhaust = true;
        setCardElement(MagicElement.ICE);
    }

    @Override
    protected List<AbstractCardModifier> getInitialModifiers() {
        List<AbstractCardModifier> mods = new ArrayList<>();
        mods.add(new RecurringSpellCardMod(RECURRENCE));
        return mods;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasDebuff = m.powers.stream().anyMatch(power -> power.type == AbstractPower.PowerType.DEBUFF);
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent, UUID originalUUID) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            actionsList.add(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, c.spellBlock));
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGR_EXTRA_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public void cardWasFrozen(CardGroup.CardGroupType thisCardGroup) {
        if (thisCardGroup == CastingQueueGroupEnum.CASTER_CASTING_QUEUE) {
            addToBot(new ModifyCastingSpellsEffectAction(this, 0, magicNumber));
        }
    }
}
