package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ArbitraryCardAction;
import sts.caster.actions.ModifyCastingSpellCastTimeAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class DivertMana extends CasterCard {

    public static final String ID = CasterMod.makeID("DivertMana");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("channeling.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_BLOCK = 9;
    private static final int UPG_BLOCK = 3;
    private static final int EXTRA_BLOCK_DIFF = 3;
    private static final int UPG_EXTRA_BLOCK = 1;

    private static final int CAST_TIME_INCREASE = 1;

    public DivertMana() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BASE_BLOCK;
        magicNumber = baseMagicNumber = EXTRA_BLOCK_DIFF;
    }

    @Override
    public void applyPowers() {
        int cardBaseBlock = this.baseBlock;
        this.baseBlock = this.baseMagicNumber;
        super.applyPowers();
        magicNumber = block;
        this.baseBlock = cardBaseBlock;
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<CastingSpellCard> castingCards = SpellCardsArea.spellCardsBeingCasted;
        int spellsBeingCasted = castingCards != null ? castingCards.size() : 0;
        int blockToGain = block + magicNumber * spellsBeingCasted;
        addToBot(new GainBlockAction(p, p, blockToGain));
        addToBot(new ArbitraryCardAction(this, (c) -> {
            if (SpellCardsArea.spellCardsBeingCasted != null) {
                for (CastingSpellCard delayCard : SpellCardsArea.spellCardsBeingCasted) {
                    addToBot(new ModifyCastingSpellCastTimeAction(delayCard, CAST_TIME_INCREASE));
                }
            }
        }));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeBlock(UPG_BLOCK);
            upgradeMagicNumber(UPG_EXTRA_BLOCK);
        }
    }
}
