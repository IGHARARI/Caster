package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;

import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;

public class Slick extends CasterCard {

    public static final String ID = CasterMod.makeID("Slick");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("slick.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int BASE_DELAY = 1;
    private static final int BASE_BLUR = 1;
    private static final int BASE_BLOCK = 5;

    public Slick() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellBlock = spellBlock = BASE_BLOCK;
        baseDelayTurns = delayTurns = BASE_DELAY;
        magicNumber = baseMagicNumber = BASE_BLUR;
        setCardElement(MagicElement.ICE);
        upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new ApplyPowerAction(p, p, new BlurPower(p, magicNumber), magicNumber));
        addToBot(new DrawCardAction(p, 2));
		addToBot(new QueueDelayedCardAction(this, delayTurns, null));
    }
    
    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
    		actionsList.add(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, c.spellBlock));
    		return actionsList;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            ++this.timesUpgraded;
            this.upgraded = true;
            this.initializeTitle();
        }
    }
}
