package sts.caster.cards.spells;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.FreezeCardAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.RecurringSpellCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;
import java.util.List;

import static sts.caster.core.CasterMod.makeCardPath;

public class GlacialShield extends CasterCard {

    public static final String ID = CasterMod.makeID("GlacialShield");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("glacialshield.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_BLOCK = 4;
    private static final int UPG_BLOCK = 2;
    private static final int FREEZE_AMOUNT = 1;
    private static final int BASE_RECUR = 2;

    public GlacialShield() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellBlock = spellBlock = BASE_BLOCK;
        baseDelayTurns = delayTurns = BASE_DELAY;
        magicNumber = baseMagicNumber = FREEZE_AMOUNT;
        setCardElement(MagicElement.ICE);
    }

    @Override
    protected List<AbstractCardModifier> getInitialModifiers() {
        List<AbstractCardModifier> mods = new ArrayList<>();
        mods.add(new RecurringSpellCardMod(BASE_RECUR));
        return mods;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FreezeCardAction(magicNumber, !upgraded));
        addToBot(new QueueDelayedCardAction(this, delayTurns, null));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            actionsList.add(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, c.spellBlock));
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            upgradeSpellBlock(UPG_BLOCK);
        }
    }
}
