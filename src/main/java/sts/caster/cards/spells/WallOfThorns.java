package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class WallOfThorns extends CasterCard {

    public static final String ID = CasterMod.makeID("WallOfThorns");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("wallofthorns.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_BLOCK = 3;
    private static final int UPG_BLOCK = 1;
    private static final int BASE_THORNS = 3;


    public WallOfThorns() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_THORNS;
        baseSpellBlock = spellBlock = BASE_BLOCK;
        baseDelayTurns = delayTurns = BASE_DELAY;
        setCardElement(MagicElement.EARTH);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, spellBlock));
        addToBot(new GainBlockAction(p, p, spellBlock));
        addToBot(new QueueDelayedCardAction(this, delayTurns, null));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            AbstractPlayer p = AbstractDungeon.player;
            ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
            actions.add(new GainBlockAction(p, p, c.spellBlock));
            actions.add(new ApplyPowerAction(p, p, new ThornsPower(p, c.magicNumber), c.magicNumber));
            return actions;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeSpellBlock(UPG_BLOCK);
        }
    }
}
