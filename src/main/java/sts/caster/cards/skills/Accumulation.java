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
import sts.caster.delayedCards.DelayedCardEffect;
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

    private static final int COST = 1;
    private static final int EXTRA_BLOCK = 4;
	private boolean descriptionChanged = false;


    public Accumulation() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = 0;
        baseBlock = block = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }
    
    @Override
    public void atTurnStart() {
    	updateDescription();
    }
    
    private void updateDescription() {
    	if (!descriptionChanged ) {
    		if (!upgraded) {
    			rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
    		} else {
    			rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
    		}
    		initializeDescription();
    	}
    	descriptionChanged = true;
	}
    
    @Override
    public void applyPowers() {
    	super.applyPowers();
    	baseBlock = countAggregateCastTime();
    	baseBlock += magicNumber;
    	block = baseBlock;
    	isBlockModified = true;
    	updateDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(EXTRA_BLOCK);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            descriptionChanged = false;
        }
    }
    
    public static int countAggregateCastTime() {
    	int count = 0;
		if (DelayedCardsArea.delayedCards != null) {
			for (DelayedCardEffect card : DelayedCardsArea.delayedCards) {
				count += card.turnsUntilFire;
			}
		}
		return count;
    }
}
