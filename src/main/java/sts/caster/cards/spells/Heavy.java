package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.DamageAllEnemiesForBlockLostAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;
import java.util.UUID;

import static sts.caster.core.CasterMod.makeCardPath;

public class Heavy extends CasterCard {

    public static final String ID = CasterMod.makeID("Heavy");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("heavy.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_BLOCK = 8;


    public Heavy() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        spellBlock = baseSpellBlock = BASE_BLOCK;
        baseDelayTurns = delayTurns = BASE_DELAY;
        setCardElement(MagicElement.EARTH);
        upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, spellBlock));
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent, UUID originalUUID) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            actionsList.add(new DamageAllEnemiesForBlockLostAction());
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            ++this.timesUpgraded;
            this.upgraded = true;
            this.initializeTitle();
        }
    }

    @Override
    public int getIntentNumber() {
        Integer blockLost = CasterMod.blockLostPerTurn.get(GameActionManager.turn -1);
        if (blockLost != null && blockLost != 0) {
            return blockLost;
        }
        return 0;
    }
}
