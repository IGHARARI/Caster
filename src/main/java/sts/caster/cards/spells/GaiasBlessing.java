package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;

import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;

public class GaiasBlessing extends CasterCard {

    public static final String ID = CasterMod.makeID("GaiasBlessing");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("gaiasblessing.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 2;
    private static final int BASE_REGEN = 2;
    private static final int UPG_DELAY = -1;


    public GaiasBlessing() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        magicNumber = baseMagicNumber = BASE_REGEN;
        setCardElement(MagicElement.EARTH);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RegenPower(p, magicNumber), magicNumber));
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, null));
    }

    
    @Override
    public ActionListMaker getActionsMaker(Integer energySpent) {
    	return (c, t) -> {
    		AbstractPlayer p = AbstractDungeon.player;
    		ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
    		actionsList.add(new ApplyPowerAction(p, p, new RegenPower(p, c.magicNumber), c.magicNumber));
    		return actionsList;
    	};
    }
    
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeDelayTurns(UPG_DELAY);
        }
    }
}
