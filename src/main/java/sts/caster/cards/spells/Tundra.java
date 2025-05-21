package sts.caster.cards.spells;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import sts.caster.actions.ActionOnAllEnemiesAction;
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

public class Tundra extends CasterCard {

    public static final String ID = CasterMod.makeID("Tundra");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("tundra.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_RECUR = 2;
    private static final int UPGR_RECUR = 4;
    private static final int BASE_VULN = 1;
    private static final int BASE_WEAK = 1;

    public Tundra() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseMagicNumber = magicNumber = BASE_VULN;
        baseM2 = m2 = BASE_WEAK;
        exhaust = true;
        setCardElement(MagicElement.ICE);
    }

    @Override
    protected List<AbstractCardModifier> getInitialModifiers() {
        CasterMod.logger.info("Receiving initial mods for "+ this.uuid);
        List<AbstractCardModifier> mods = new ArrayList<>();
        if (upgraded) {
            CasterMod.logger.info("Generating upgrader tundra with: " + UPGR_RECUR);
            mods.add(new RecurringSpellCardMod(UPGR_RECUR));
        } else {
            CasterMod.logger.info("Generating Normal tundra with: " + BASE_RECUR);
            mods.add(new RecurringSpellCardMod(BASE_RECUR));
        }
        return mods;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        addToBot(new ActionOnAllEnemiesAction(
//                mon -> new ApplyPowerAction(mon, AbstractDungeon.player, new VulnerablePower(mon, magicNumber, false), magicNumber)
//        ));
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            actionsList.add(new ActionOnAllEnemiesAction(
                    m -> new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, c.magicNumber, true), c.magicNumber)
            ));
            actionsList.add(new ActionOnAllEnemiesAction(
                    m -> new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, c.m2, false), c.m2)
            ));
//            actionsList.add(new DelayedDamageAllEnemiesAction(AbstractDungeon.player, c.spellDamage, c.cardElement, AttackEffect.SMASH));
//            actionsList.add(new QueueDelayedCardAction(c, BASE_DELAY, t));
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            ArrayList<AbstractCardModifier> mods = CardModifierManager.getModifiers(this, RecurringSpellCardMod.ID);
            mods.stream().forEach(c -> ((RecurringSpellCardMod) c).setRecurrence(UPGR_RECUR));
            upgradeName();
            initializeDescription();
        }
    }
}
