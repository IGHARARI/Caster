package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class Superconduct extends CasterCard {

    public static final String ID = CasterMod.makeID("Superconduct");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("superconduct.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = -1;
    private static final int DELAY_TURNS = 1;
    private static final int UPGRADE_EXTRA_CARDS = 1;


    public Superconduct() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        delayTurns = baseDelayTurns = DELAY_TURNS;
        magicNumber = baseMagicNumber = 0;
        cardsToPreview = new Jolt();
        setCardElement(MagicElement.ELECTRIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (energyOnUse < EnergyPanel.totalCount) {
            energyOnUse = EnergyPanel.totalCount;
        }
        int effect = energyOnUse + magicNumber;
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
        }
        addToBot(new QueueDelayedCardAction(this, delayTurns, effect, null));
        if (!freeToPlayOnce) {
            AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            Jolt jolt = new Jolt();
            if (this.upgraded) jolt.upgrade();
            actionsList.add(new MakeTempCardInHandAction(jolt, energySpent));
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_EXTRA_CARDS);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            Jolt joltPlus = new Jolt();
            joltPlus.upgrade();
            cardsToPreview = joltPlus;
            initializeDescription();
        }
    }

    @Override
    public int getIntentNumber() {
        return spellDamage * magicNumber;
    }
}
