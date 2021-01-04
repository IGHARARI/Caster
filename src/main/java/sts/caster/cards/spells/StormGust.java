package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.DelayedActionOnAllEnemiesAction;
import sts.caster.actions.DelayedDamageAllEnemiesAction;
import sts.caster.actions.FreezeCardAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.interfaces.MonsterToActionInterface;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.powers.FrostPower;

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
    private static final int BASE_DAMAGE = 32;
    private static final int BASE_FROST = 3;
    private static final int UPG_FROST = 2;
    private static final int BASE_CARDS_FROZEN = 2;
    private static final int UPG_CARDS_FROZEN = -1;


    public StormGust() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = BASE_CARDS_FROZEN;
        m2 = baseM2 = BASE_FROST;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new FreezeCardAction(magicNumber, false, false, false));
		addToBot(new QueueDelayedCardAction(this, delayTurns, null));
    }
    
    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
        	actions.add(new DelayedDamageAllEnemiesAction(AbstractDungeon.player, c.spellDamage, c.cardElement, AttackEffect.SMASH));
            MonsterToActionInterface frostApply = (mon) -> {
                return new ApplyPowerAction(mon, AbstractDungeon.player, new FrostPower(mon, AbstractDungeon.player, c.m2), c.m2);
            };
            actions.add(new DelayedActionOnAllEnemiesAction(frostApply));
            return actions;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeM2(UPG_FROST);
            upgradeMagicNumber(UPG_CARDS_FROZEN);
            initializeDescription();
        }
    }
}
