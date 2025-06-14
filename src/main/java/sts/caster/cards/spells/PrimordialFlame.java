package sts.caster.cards.spells;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.IgniteSpecificCardAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.actions.RepeatHighestHPDamageAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.IgnitedCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;
import java.util.List;

import static sts.caster.core.CasterMod.makeCardPath;

public class PrimordialFlame extends CasterCard {

    public static final String ID = CasterMod.makeID("PrimordialFlame");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("conflagrate.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 3;
    private static final int UPG_DELAY = -1;
    private static final int BASE_DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;
    private static final int HIT_TIMES = 3;


    public PrimordialFlame() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = HIT_TIMES;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    protected List<AbstractCardModifier> getInitialModifiers() {
        ArrayList<AbstractCardModifier> mods = new ArrayList<AbstractCardModifier>();
        mods.add(new IgnitedCardMod());
        return mods;
    }


    @Override
    public void triggerOnExhaust() {
        AbstractCard copy = this.makeStatEquivalentCopy();
        addToBot(new IgniteSpecificCardAction(copy));
        addToBot(new MakeTempCardInDrawPileAction(copy, 1, true, true));
        super.triggerOnExhaust();
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            AbstractPlayer p = AbstractDungeon.player;
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            actionsList.add(new RepeatHighestHPDamageAction(this, magicNumber));
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
            upgradeDelayTurns(UPG_DELAY);
            initializeDescription();
        }
    }

    @Override
    public int getIntentNumber() {
        this.applyPowers();
        return spellDamage * magicNumber;
    }
}
