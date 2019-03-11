package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.DelayedDamageAllEnemiesAction;
import sts.caster.actions.FreezeCardAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;

public class StormGust extends CasterCard {

    public static final String ID = CasterMod.makeID("StormGust");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("stormgust.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_DELAY = 2;
    private static final int BASE_DAMAGE = 30;
    private static final int BASE_CARDS_FROZEN = 2;
    private static final int UPG_CARDS_FROZEN = 1;


    public StormGust() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = BASE_CARDS_FROZEN;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	AbstractDungeon.actionManager.addToBottom(new FreezeCardAction(magicNumber, false, false, false));
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, null));
    }
    
    @Override
    public ActionListMaker getActionsMaker(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
        	actions.add(new DelayedDamageAllEnemiesAction(AbstractDungeon.player, c.spellDamage, AttackEffect.SMASH));
    		return actions;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            upgradeMagicNumber(UPG_CARDS_FROZEN);
        }
    }
}
