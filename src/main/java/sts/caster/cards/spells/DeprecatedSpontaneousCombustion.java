package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.actions.SpontaneousCombustionAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class DeprecatedSpontaneousCombustion extends CasterCard {

    public static final String ID = CasterMod.makeID("DeprecatedSpontaneousCombustion");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("spontaneouscomb.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 3;
    private static final int BASE_DAMAGE = 7;
    private static final int UPG_DMG = 2;
    private static final int HIT_TIMES = 3;


    public DeprecatedSpontaneousCombustion() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseSpellDamage = spellDamage =  BASE_DAMAGE;
        baseMagicNumber = magicNumber = HIT_TIMES;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new DamageAction(m, new DamageInfo(m, spellDamage), AttackEffect.FIRE));
		addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }
    
    @Override
    public float getTitleFontSize() {
    	return 17;
    }
    
    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
    		if (c instanceof DeprecatedSpontaneousCombustion) actionsList.add(new SpontaneousCombustionAction((DeprecatedSpontaneousCombustion) c, magicNumber));
    		return actionsList;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSpellDamage(UPG_DMG);
            initializeDescription();
        }
    }

    @Override
    public int getIntentNumber() {
        return spellDamage * magicNumber;
    }
}
