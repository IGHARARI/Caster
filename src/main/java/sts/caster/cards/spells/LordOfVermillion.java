package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;

import sts.caster.actions.LordOfVermillionAction;
import sts.caster.actions.RandomTargetLightningDamageAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

public class LordOfVermillion extends CasterCard {

    public static final String ID = CasterMod.makeID("LordOfVermillion");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("lordofvermillion.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = -1;
    private static final int DELAY_TURNS = 3;
    private static final int BASE_DAMAGE = 5;
    private static final int HIT_TIMES = 4;
    private static final int UPGR_HIT_TIMES = 2;

    private int energyUsed;

    public LordOfVermillion() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns = DELAY_TURNS;
        magicNumber = baseMagicNumber = HIT_TIMES;
        setCardElement(MagicElement.ELECTRIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LordOfVermillionAction(this, energyOnUse));
        energyUsed = energyOnUse;
//        if (energyOnUse < EnergyPanel.totalCount) {
//            energyOnUse = EnergyPanel.totalCount;
//        }
//        int effect = energyOnUse;
//        if (p.hasRelic(ChemicalX.ID)) {
//            effect += 2;
//        }
//    	addToBot(new QueueDelayedCardAction(this, delayTurns, effect, null));
//    	if (!freeToPlayOnce) {
//    		AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
//    	}
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer spentEnergy) {
        return (c, t) -> {
            int energyMultiplier = spentEnergy;
            if (AbstractDungeon.player.hasRelic(ChemicalX.ID)) energyMultiplier += 2;
            ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
            for (int i = 0; i < energyMultiplier * c.magicNumber; i++) {
                actions.add(new RandomTargetLightningDamageAction(new DamageInfo(AbstractDungeon.player, c.spellDamage, DamageType.NORMAL), AttackEffect.NONE));
            }
            return actions;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeMagicNumber(UPGR_HIT_TIMES);
        }
    }

    @Override
    public int getIntentNumber() {
        int energyMultiplier = energyUsed;
        return spellDamage * energyMultiplier * magicNumber;
    }
}
