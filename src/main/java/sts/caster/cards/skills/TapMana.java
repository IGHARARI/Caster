package sts.caster.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ArbitraryCardAction;
import sts.caster.actions.ModifyAllCastingSpellsEffectAction;
import sts.caster.actions.ModifyCastingSpellCastTimeAction;
import sts.caster.actions.QueueRedrawMiniCardsAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

import static sts.caster.core.CasterMod.makeCardPath;

public class TapMana extends CasterCard {

    public static final String ID = CasterMod.makeID("TapMana");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("channeling.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int SPELL_DOWNGRADE_AMOUNT = 1;


    public TapMana() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SPELL_DOWNGRADE_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ModifyAllCastingSpellsEffectAction(-magicNumber, -magicNumber));
        addToBot(new ArbitraryCardAction(this, (c) -> {
            if (SpellCardsArea.spellCardsBeingCasted != null) {
                for (CastingSpellCard delayCard : SpellCardsArea.spellCardsBeingCasted) {
                    addToBot(new ModifyCastingSpellCastTimeAction(delayCard, -magicNumber));
                }
            }
            addToBot(new QueueRedrawMiniCardsAction());
        }));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
