package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;

public class WallOfSwords extends CasterCard {

    public static final String ID = CasterMod.makeID("WallOfSwords");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("wallofswords.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int UPG_COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_MAX_THORNS = 8;
    private static final int UPG_MAX_THORNS = 3;
    


    public WallOfSwords() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseMagicNumber = magicNumber = BASE_MAX_THORNS;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new QueueDelayedCardAction(this, delayTurns, null));
    }

    
    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
    	return (c, t) -> {
    		AbstractPlayer p = AbstractDungeon.player;
    		ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
    		if (p.currentBlock > 0) {
    			int blockToLose = Math.min(magicNumber, p.currentBlock);
    			actionsList.add(new LoseBlockAction(p, p, blockToLose));
    			actionsList.add(new ApplyPowerAction(p, p, new ThornsPower(p, blockToLose), blockToLose));
    		}
    		return actionsList;
    	};
    }
    
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPG_COST);
//            upgradeMagicNumber(UPG_MAX_THORNS);
            initializeDescription();
        }
    }
}
