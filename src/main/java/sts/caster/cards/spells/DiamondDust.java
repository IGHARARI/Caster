package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.ArbitraryCardAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.powers.FrostPower;

public class DiamondDust extends CasterCard {

    public static final String ID = CasterMod.makeID("DiamondDust");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("glacialshield.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 2;
    private static final int UPG_DELAY = -1;
    private static final int FREEZE_AMOUNT = 4;
    


    public DiamondDust() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        magicNumber = baseMagicNumber = FREEZE_AMOUNT;
        m2 = baseM2 = 0;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	m2 = AbstractDungeon.player.currentBlock;
    	if (m2 != 0) isM2Modified = true;
    	rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
    	initializeDescription();
    	addToBot(new ApplyPowerAction(m, p, new FrostPower(m, p, magicNumber), magicNumber));
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
		addToBot(new ArbitraryCardAction(this, (c) -> {
            c.rawDescription = cardStrings.DESCRIPTION;
            c.initializeDescription();
        }));
    }
    
    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
    		actionsList.add(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, c.m2));
    		return actionsList;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDelayTurns(UPG_DELAY);
            initializeDescription();
        }
    }
}
