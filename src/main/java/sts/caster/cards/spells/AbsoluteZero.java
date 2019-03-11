package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;

public class AbsoluteZero extends CasterCard {

    public static final String ID = CasterMod.makeID("AbsoluteZero");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("absolutezero.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_DAMAGE = 4;
    private static final int UPGR_DAMAGE = 2;
    private static final int FROZEN_MULTIPLIER = 2;


    public AbsoluteZero() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = FROZEN_MULTIPLIER;
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        baseDelayTurns = delayTurns = BASE_DELAY;
        setCardElement(MagicElement.ICE);
    }
    
    @Override
    public void onFrozen() {
    	flash();
    	baseSpellDamage *= FROZEN_MULTIPLIER;
    	isSpellDamageModified = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListMaker getActionsMaker(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
    		actionsList.add(new DamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage), AttackEffect.SLASH_HEAVY));
    		return actionsList;
    	};
    }
    
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeSpellDamage(UPGR_DAMAGE);
        }
    }
}
