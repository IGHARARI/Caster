package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.delayedCards.DelayedCardsArea;

public class Accumulation extends CasterCard {

    public static final String ID = CasterMod.makeID("Accumulation");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("accumulate.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int BLOCK_PER_SPELL = 3;
    private static final int UPG_BLOCK_PER_SPELL = 1;


    public Accumulation() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BLOCK_PER_SPELL;
        baseBlock = block = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new GainBlockAction(p, p, block));
    }
    
    @Override
    public void applyPowers() {
    	baseBlock = countCardsInCastArea() * magicNumber;
    	block = baseBlock;
    	isBlockModified = true;
    	super.applyPowers();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_BLOCK_PER_SPELL);
            initializeDescription();
        }
    }
    
    public static int countCardsInCastArea() {
		return (DelayedCardsArea.delayedCards != null) ? DelayedCardsArea.delayedCards.size() : 0;
    }
}
