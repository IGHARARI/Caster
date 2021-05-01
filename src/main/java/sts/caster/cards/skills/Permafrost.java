package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import sts.caster.actions.ArbitraryCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

import static sts.caster.core.CasterMod.makeCardPath;

public class Permafrost extends CasterCard {

    public static final String ID = CasterMod.makeID("Permafrost");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("permafrost.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;

    public Permafrost() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = block = 0;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        addToBot(new ArbitraryCardAction(this, (c) -> {
            if (SpellCardsArea.spellCardsBeingCasted != null) {
                for (CastingSpellCard delayCard : SpellCardsArea.spellCardsBeingCasted) {
                    delayCard.increaseCastingDelay(1);
                }
            }
            SpellCardsArea.repositionMiniCards();
        }));
        addToBot(new GainBlockAction(p, block));
        addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, block / 2), block / 2));
    }

    @Override
    public void applyPowers() {
        baseBlock = sumCastingTimeInCastArea() * 2;
        block = baseBlock;
        isBlockModified = true;
        super.applyPowers();
    }

    private int sumCastingTimeInCastArea() {
        int sum = 0;
        if (SpellCardsArea.spellCardsBeingCasted != null) {
            for (CastingSpellCard delayCard : SpellCardsArea.spellCardsBeingCasted) {
                sum += delayCard.turnsUntilFire + 1;
            }
        }
        return sum;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.selfRetain = true;
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
