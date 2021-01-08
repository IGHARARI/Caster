package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
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
import sts.caster.core.frozenpile.FrozenPileManager;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.interfaces.MonsterToActionInterface;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.powers.FrostPower;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class Cool extends CasterCard {

    public static final String ID = CasterMod.makeID("Cool");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("cool.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int UPG_COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_DAMAGE = 1;
    private static final int BASE_CARDS_FROZEN = 1;


    public Cool() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseM2 = m2 = BASE_DAMAGE;
        baseSpellDamage = spellDamage = 0;
        magicNumber = baseMagicNumber = BASE_CARDS_FROZEN;
        cardsToPreview = new Frostbite();
        cardsToPreview.applyPowers();
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new FreezeCardAction(magicNumber, false, false, false));
		addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public void applyPowers() {
        baseSpellDamage = FrozenPileManager.getFrozenCount() * m2;
        super.applyPowers();
    }

    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
    		Frostbite frostbite = new Frostbite();
            frostbite.applyPowers();
            actions.add(new DamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage), AttackEffect.BLUNT_HEAVY));
            actions.add(new QueueDelayedCardAction(frostbite, frostbite.delayTurns, t));
            return actions;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPG_COST);
            initializeDescription();
        }
    }
}